package service;

import model.EpicTask;
import model.SubTask;
import model.Task;
import model.enums.TaskStatus;
import service.exceptions.IntersectionException;
import service.exceptions.NotFoundException;
import service.interfaces.HistoryManager;
import service.interfaces.TaskManager;
import util.TimeMapper;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

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

    private void updatePrioritizedTask(Task previousTask, Task newTask) {
        if (newTask.getStartTime() == null) {
            if (previousTask.getStartTime() != null) {
                timeMapper.remove(previousTask);
                priorityTasks.remove(previousTask);
            }
            return;
        }

        if (previousTask.getStartTime() == null) {
            if (timeMapper.hasCollision(newTask)) throw new IntersectionException();
            timeMapper.add(newTask);
            priorityTasks.add(newTask);
            return;
        }

        timeMapper.remove(previousTask);
        if (timeMapper.hasCollision(newTask)) {
            timeMapper.add(previousTask);
            throw new IntersectionException();
        }

        timeMapper.add(newTask);
        priorityTasks.add(newTask);
    }

    private void addToPrioritizedTasks(Task task) {
        if (task.getStartTime() == null) return;
        if (timeMapper.hasCollision(task)) throw new IntersectionException();

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
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task == null) throw new NotFoundException("task not found");

        historyManager.add(task);
        return task;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask task = subTasks.get(id);
        if (task == null) throw new NotFoundException("sub task not found");

        historyManager.add(task);
        return task;
    }

    @Override
    public EpicTask getEpicTaskById(int id) {
        EpicTask task = epicTasks.get(id);
        if (task == null) throw new NotFoundException("epic task not found");

        historyManager.add(task);
        return task;
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
        Task previousTask = tasks.get(task.getId());
        if (previousTask == null) throw new NotFoundException("task not found");

        updatePrioritizedTask(previousTask, task);
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateSubTask(SubTask st) {
        SubTask previousTask = subTasks.get(st.getId());
        if (previousTask == null) throw new NotFoundException("subtask not found");

        updatePrioritizedTask(previousTask, st);
        subTasks.put(st.getId(), st);
        refreshEpic(epicTasks.get(st.getEpicTaskId()));
    }

    @Override
    public void updateEpicTask(EpicTask et) {
        EpicTask previousTask = epicTasks.get(et.getId());
        if (previousTask == null) throw new NotFoundException("epic task not found");

        subTasks.values().stream()
                .filter(st -> epicTasks.get(st.getEpicTaskId()).equals(et))
                .peek(et::addSubTask).close();
        refreshEpic(et);
        epicTasks.put(et.getId(), et);
    }

    @Override
    public Task removeTaskById(int id) {
        Task task = tasks.remove(id);
        if (task == null) throw new NotFoundException("task not found");

        removeFromPrioritizedTasks(task);
        historyManager.remove(id);
        return task;
    }

    @Override
    public SubTask removeSubTaskById(int id) {
        SubTask subTask = subTasks.remove(id);
        if (subTask == null) throw new NotFoundException("subtask not found");

        removeFromPrioritizedTasks(subTask);
        EpicTask et = epicTasks.get(subTask.getEpicTaskId());
        et.removeSubTask(subTask);
        refreshEpic(et);
        historyManager.remove(id);
        return subTask;
    }

    @Override
    public EpicTask removeEpicTaskById(int id) {
        EpicTask epicTask = epicTasks.remove(id);
        if (epicTask == null) throw new NotFoundException("epic task not found");

        epicTask.getSubTaskIds().forEach(sId -> {
            removeFromPrioritizedTasks(subTasks.get(sId));
            subTasks.remove(sId);
            historyManager.remove(sId);
        });
        historyManager.remove(id);
        return epicTask;
    }

    @Override
    public List<SubTask> getSubTasks(EpicTask epicTask) {
        return new ArrayList<>(epicTask.getSubTaskIds().
                stream().map(subTasks::get).toList());
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
        if (!endTime.equals(LocalDateTime.MIN)) et.setEndTime(endTime);
    }
}
