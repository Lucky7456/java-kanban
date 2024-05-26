package service;

import model.EpicTask;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, SubTask> subTasks;
    private final HashMap<Integer, EpicTask> epicTasks;

    public TaskManager() {
        tasks = new HashMap<>();
        subTasks = new HashMap<>();
        epicTasks = new HashMap<>();
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    public ArrayList<EpicTask> getEpicTasks() {
        return new ArrayList<>(epicTasks.values());
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public void removeAllSubTasks() {
        subTasks.clear();
        for (EpicTask et : epicTasks.values()) {
            et.removeAllSubTasks();
        }
    }

    public void removeAllEpicTasks() {
        subTasks.clear();
        epicTasks.clear();
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public SubTask getSubTaskById(int id) {
        return subTasks.get(id);
    }

    public EpicTask getEpicTaskById(int id) {
        return epicTasks.get(id);
    }

    public void createTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void createSubTask(SubTask st) {
        subTasks.put(st.getId(), st);
    }

    public void createEpicTask(EpicTask et) {
        epicTasks.put(et.getId(), et);
    }

    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void updateSubTask(SubTask st) {
        EpicTask et = subTasks.get(st.getId()).getEpicTask();
        et.replaceSubTask(st);
        st.setEpicTask(et);
        subTasks.put(st.getId(), st);
    }

    public void updateEpicTask(EpicTask et) {
        for (SubTask st : subTasks.values()) {
            if (st.getEpicTask().equals(et)) {
                st.setEpicTask(et);
                et.addSubTask(st);
            }
        }
        epicTasks.put(et.getId(), et);
    }

    public void removeTaskById(int id) {
        tasks.remove(id);
    }

    public void removeSubTaskById(int id) {
        SubTask st = getSubTaskById(id);
        st.getEpicTask().removeSubTask(st);
        subTasks.remove(id);
    }

    public void removeEpicTaskById(int id) {
        for (SubTask st : epicTasks.get(id).getSubTasks()) {
            subTasks.remove(st.getId());
        }
        epicTasks.remove(id);
    }

    public ArrayList<SubTask> getSubTasks(EpicTask epicTask) {
        return epicTasks.get(epicTask.getId()).getSubTasks();
    }
}
