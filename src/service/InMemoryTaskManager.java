package service;

import model.EpicTask;
import model.SubTask;
import model.Task;
import model.enums.TaskStatus;
import service.interfaces.HistoryManager;
import service.interfaces.TaskManager;
import util.TimeMapper;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private static final int BIT = 1;
    private static final int BIT_MASK_DONE = BIT << TaskStatus.DONE.ordinal();
    private static final int BIT_MASK_NEW = BIT << TaskStatus.NEW.ordinal();

    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, SubTask> subTasks;
    private final HashMap<Integer, EpicTask> epicTasks;
    private final HistoryManager historyManager;
    private final TreeSet<Task> priorityTasks;
    private final TimeMapper timeMapper;

    public InMemoryTaskManager(HistoryManager historyManager) {
        tasks = new HashMap<>();
        subTasks = new HashMap<>();
        epicTasks = new HashMap<>();
        this.historyManager = historyManager;
        priorityTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
        timeMapper = new TimeMapper(LocalDateTime.now());
    }

    private void addToPrioritizedTasks(Task task) {
        if (task.getStartTime() == null) return;
        if (timeMapper.hasCollision(task)) return;

        timeMapper.add(task);
        priorityTasks.add(task);
    }

    private void removeFromPrioritizedTasks(Task task) {
        if (task.getStartTime() == null) return;

        timeMapper.remove(task);
        priorityTasks.remove(task);
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(priorityTasks);
    }

    @Override
    public List<Task> getAllTasks() {
        ArrayList<Task> allTasks = new ArrayList<>();
        allTasks.addAll(tasks.values());
        allTasks.addAll(epicTasks.values());
        allTasks.addAll(subTasks.values());
        return allTasks;
    }

    @Override
    public List<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public List<EpicTask> getEpicTasks() {
        return new ArrayList<>(epicTasks.values());
    }

    @Override
    public void removeAllTasks() {
        tasks.values().forEach(t -> {
            removeFromPrioritizedTasks(t);
            historyManager.remove(t.getId());
        });
        tasks.clear();
    }

    @Override
    public void removeAllSubTasks() {
        subTasks.values().forEach(t -> {
            removeFromPrioritizedTasks(t);
            historyManager.remove(t.getId());
        });
        subTasks.clear();
        epicTasks.values().forEach(t -> {
            t.removeAllSubTasks();
            refreshEpic(t);
        });
    }

    @Override
    public void removeAllEpicTasks() {
        subTasks.values().forEach(t -> {
            removeFromPrioritizedTasks(t);
            historyManager.remove(t.getId());
        });
        subTasks.clear();

        epicTasks.values().forEach(t -> historyManager.remove(t.getId()));
        epicTasks.clear();
    }

    @Override
    public Optional<Task> getTaskById(int id) {
        Task task = tasks.get(id);
        if (task == null) return Optional.empty();

        historyManager.add(task);
        return Optional.of(task);
    }

    @Override
    public Optional<SubTask> getSubTaskById(int id) {
        SubTask task = subTasks.get(id);
        if (task == null) return Optional.empty();

        historyManager.add(task);
        return Optional.of(task);
    }

    @Override
    public Optional<EpicTask> getEpicTaskById(int id) {
        EpicTask task = epicTasks.get(id);
        if (task == null) return Optional.empty();

        historyManager.add(task);
        return Optional.of(task);
    }

    @Override
    public void createTask(Task task) {
        addToPrioritizedTasks(task);
        tasks.put(task.getId(), task);
    }

    @Override
    public void createSubTask(SubTask st) {
        addToPrioritizedTasks(st);
        subTasks.put(st.getId(), st);
        EpicTask et = epicTasks.get(st.getEpicTaskId());
        et.addSubTask(st);
        refreshEpic(et);
    }

    @Override
    public void createEpicTask(EpicTask et) {
        epicTasks.put(et.getId(), et);
    }

    @Override
    public void updateTask(Task task) {
        removeFromPrioritizedTasks(tasks.get(task.getId()));
        addToPrioritizedTasks(task);
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateSubTask(SubTask st) {
        removeFromPrioritizedTasks(subTasks.get(st.getId()));
        addToPrioritizedTasks(st);
        subTasks.put(st.getId(), st);
        refreshEpic(epicTasks.get(st.getEpicTaskId()));
    }

    @Override
    public void updateEpicTask(EpicTask et) {
        subTasks.values().stream()
                .filter(st -> epicTasks.get(st.getEpicTaskId()).equals(et))
                .peek(et::addSubTask).close();
        refreshEpic(et);
        epicTasks.put(et.getId(), et);
    }

    @Override
    public void removeTaskById(int id) {
        removeFromPrioritizedTasks(tasks.get(id));
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeSubTaskById(int id) {
        removeFromPrioritizedTasks(subTasks.get(id));
        SubTask st = subTasks.get(id);
        EpicTask et = epicTasks.get(st.getEpicTaskId());
        et.removeSubTask(st);
        refreshEpic(et);
        subTasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeEpicTaskById(int id) {
        epicTasks.get(id).getSubTaskIds().forEach(sId -> {
            removeFromPrioritizedTasks(subTasks.get(sId));
            subTasks.remove(sId);
            historyManager.remove(sId);
        });
        epicTasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public List<SubTask> getSubTasks(EpicTask epicTask) {
        return subTasks.values().stream()
                .filter(st -> epicTask.getSubTaskIds().contains(st.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void refreshEpic(EpicTask et) {
        List<SubTask> subs = getSubTasks(et);
        if (subs.isEmpty()) {
            et.setStatus(TaskStatus.NEW);
            return;
        }

        LocalDateTime startTime = LocalDateTime.MAX;
        LocalDateTime endTime = LocalDateTime.MIN;
        long duration = 0;
        int byteStatus = 0;

        for (SubTask st : subs) {
            byteStatus |= BIT << st.getStatus().ordinal();

            if (st.getStartTime() == null) continue;

            duration += st.getDuration().toMinutes();

            if (st.getStartTime().isBefore(startTime)) {
                startTime = st.getStartTime();
            }

            if (st.getEndTime().isAfter(endTime)) {
                endTime = st.getEndTime();
            }
        }

        if (byteStatus == BIT_MASK_NEW) {
            et.setStatus(TaskStatus.NEW);
        } else if (byteStatus == BIT_MASK_DONE) {
            et.setStatus(TaskStatus.DONE);
        } else {
            et.setStatus(TaskStatus.IN_PROGRESS);
        }

        et.setDuration(Duration.ofMinutes(duration));
        if (!startTime.equals(LocalDateTime.MAX)) et.setStartTime(startTime);
        if (!startTime.equals(LocalDateTime.MIN)) et.setEndTime(endTime);
    }
}
