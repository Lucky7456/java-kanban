package model;

import model.enums.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;

public class EpicTask extends Task {

    public static final int BIT = 1;
    public static final int BIT_MASK_DONE = BIT << TaskStatus.DONE.ordinal();
    public static final int BIT_MASK_NEW = BIT << TaskStatus.NEW.ordinal();

    public ArrayList<SubTask> getSubTasks() {
        return new ArrayList<>(subTasks.values());
    }

    private final HashMap<Integer, SubTask> subTasks;

    public EpicTask(String name,
                    String description,
                    int id) {
        super(name, description, id, TaskStatus.NEW);
        this.subTasks = new HashMap<>();
    }

    public EpicTask(String name,
                    String description
    ) {
        super(name, description, TaskStatus.NEW);
        this.subTasks = new HashMap<>();
    }

    public void addSubTask(SubTask st) {
        subTasks.put(st.getId(), st);
    }

    public void replaceSubTask(SubTask st) {
        subTasks.put(st.getId(), st);
    }

    public void removeSubTask(SubTask st) {
        subTasks.remove(st.getId());
    }

    public void removeAllSubTasks() {
        subTasks.clear();
    }

    @Override
    public TaskStatus getStatus() {
        if (subTasks.isEmpty()) return TaskStatus.NEW;

        int status = 0;

        for (SubTask st : subTasks.values()) {
            status |= BIT << st.getStatus().ordinal();
        }

        if (status == BIT_MASK_NEW) {
            return TaskStatus.NEW;
        } else if (status == BIT_MASK_DONE) {
            return TaskStatus.DONE;
        } else {
            return TaskStatus.IN_PROGRESS;
        }
    }

    @Override
    public String toString() {
        return "EpicTask{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", subTasks=" + subTasks.values() +
                '}';
    }
}
