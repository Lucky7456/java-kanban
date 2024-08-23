package service;

import model.EpicTask;
import model.SubTask;
import model.Task;
import service.interfaces.HistoryManager;
import service.interfaces.TaskManager;
import util.TimeMapper;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
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

    private void add(Task task) {
        if (task.getStartTime() == null) return;
        if (timeMapper.hasCollision(task)) return;

        timeMapper.add(task);
        priorityTasks.add(task);
    }

    @Override
    public ArrayList<Task> getPrioritizedTasks() {
        return new ArrayList<>(priorityTasks);
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> allTasks = new ArrayList<>();
        allTasks.addAll(tasks.values());
        allTasks.addAll(epicTasks.values());
        allTasks.addAll(subTasks.values());
        return allTasks;
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    @Override
    public ArrayList<EpicTask> getEpicTasks() {
        return new ArrayList<>(epicTasks.values());
    }

    @Override
    public void removeAllTasks() {
        tasks.clear();
    }

    @Override
    public void removeAllSubTasks() {
        subTasks.clear();
        epicTasks.values().forEach(EpicTask::removeAllSubTasks);
    }

    @Override
    public void removeAllEpicTasks() {
        subTasks.clear();
        epicTasks.clear();
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task != null) historyManager.add(task);
        return task;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask task = subTasks.get(id);
        if (task != null) historyManager.add(task);
        return task;
    }

    @Override
    public EpicTask getEpicTaskById(int id) {
        EpicTask task = epicTasks.get(id);
        if (task != null) historyManager.add(task);
        return task;
    }

    @Override
    public void createTask(Task task) {
        add(task);
        tasks.put(task.getId(), task);
    }

    @Override
    public void createSubTask(SubTask st) {
        add(st);
        subTasks.put(st.getId(), st);
    }

    @Override
    public void createEpicTask(EpicTask et) {
        epicTasks.put(et.getId(), et);
    }

    @Override
    public void updateTask(Task task) {
        add(task);
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateSubTask(SubTask st) {
        add(st);
        EpicTask et = subTasks.get(st.getId()).getEpicTask();
        et.replaceSubTask(st);
        st.setEpicTask(et);
        subTasks.put(st.getId(), st);
    }

    @Override
    public void updateEpicTask(EpicTask et) {
        subTasks.values().stream()
                .filter(st -> st.getEpicTask().equals(et))
                .peek(st -> {
                    st.setEpicTask(et);
                    et.addSubTask(st);
                }).close();
        epicTasks.put(et.getId(), et);
    }

    @Override
    public void removeTaskById(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeSubTaskById(int id) {
        SubTask st = getSubTaskById(id);
        st.getEpicTask().removeSubTask(st);
        subTasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void removeEpicTaskById(int id) {
        epicTasks.get(id).getSubTasks().forEach(st -> {
            subTasks.remove(st.getId());
            historyManager.remove(st.getId());
        });
        epicTasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public ArrayList<SubTask> getSubTasks(EpicTask epicTask) {
        return epicTasks.get(epicTask.getId()).getSubTasks();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
