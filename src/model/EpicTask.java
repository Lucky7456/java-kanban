package model;

import model.enums.TaskStatus;
import model.enums.TaskType;

import java.time.ZoneId;
import java.time.LocalDateTime;
import java.util.*;

public class EpicTask extends Task {
    private final HashSet<Integer> subTaskIds;
    private LocalDateTime endTime;

    public EpicTask(String name,
                    String description,
                    int id) {
        super(name, description, id, TaskStatus.NEW, 0, null);
        this.subTaskIds = new HashSet<>();
    }

    public EpicTask(String name,
                    String description) {
        super(name, description, TaskStatus.NEW, 0);
        this.subTaskIds = new HashSet<>();
    }

    public List<Integer> getSubTaskIds() {
        return new ArrayList<>(subTaskIds);
    }

    public void addSubTask(SubTask st) {
        subTaskIds.add(st.getId());
    }

    public void removeSubTask(SubTask st) {
        subTaskIds.remove(st.getId());
    }

    public void removeAllSubTasks() {
        subTaskIds.clear();
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,0,%s,%s",
                getId(),
                TaskType.EpicTask,
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
