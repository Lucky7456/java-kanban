package model;

import model.enums.TaskStatus;

import java.time.ZoneId;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

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
        super(name, description, id, TaskStatus.NEW, 0, null);
        this.subTasks = new HashMap<>();
    }

    public EpicTask(String name,
                    String description) {
        super(name, description, TaskStatus.NEW, 0);
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

        int calculatedStatus = subTasks.values().stream()
                .map(subTask -> subTask.getStatus().ordinal())
                .reduce(0, (total, status) -> total | BIT << status);

        if (calculatedStatus == BIT_MASK_NEW) {
            return TaskStatus.NEW;
        } else if (calculatedStatus == BIT_MASK_DONE) {
            return TaskStatus.DONE;
        } else {
            return TaskStatus.IN_PROGRESS;
        }
    }

    @Override
    public Duration getDuration() {
        return Duration.ofMinutes(subTasks.values()
                .stream()
                .filter(subTask -> subTask.getStartTime() != null)
                .mapToLong(subTask -> subTask.getDuration().toMinutes())
                .sum()
        );
    }

    @Override
    public LocalDateTime getStartTime() {
        return subTasks.values()
                .stream()
                .map(Task::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);
    }

    @Override
    public LocalDateTime getEndTime() {
        return subTasks.values()
                .stream()
                .map(Task::getEndTime)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null);
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,0,%s,%s",
                getId(),
                "EPIC",
                getName(),
                getStatus(),
                getDescription(),
                getDuration().toMinutes(),
                getStartTime() != null ?
                        getStartTime().atZone(ZoneId.systemDefault()).toEpochSecond()
                        : null
        );
    }
}
