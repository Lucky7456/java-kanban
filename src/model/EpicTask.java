package model;

import model.enums.TaskStatus;
import model.enums.TaskType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class EpicTask extends Task {
    private final HashSet<Integer> subTaskIds;
    private LocalDateTime endTime;

    public EpicTask(String name,
                    String description,
                    int id) {
        super(name, description, id, TaskStatus.NEW, 0, null);
        this.subTaskIds = new HashSet<>();
        type = TaskType.EpicTask;
    }

    public EpicTask(String name,
                    String description) {
        super(name, description, TaskStatus.NEW, 0);
        this.subTaskIds = new HashSet<>();
        type = TaskType.EpicTask;
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
}
