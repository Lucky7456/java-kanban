package model;

import model.enums.TaskStatus;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class SubTask extends Task {
    private EpicTask epicTask;

    public SubTask(String name,
                   String description,
                   int id,
                   TaskStatus status,
                   long duration,
                   LocalDateTime startTime) {
        super(name, description, id, status, duration, startTime);
    }

    public SubTask(String name,
                   String description,
                   TaskStatus status,
                   long duration,
                   LocalDateTime startTime) {
        super(name, description, status, duration, startTime);
    }

    public SubTask(String name,
                   String description,
                   TaskStatus status,
                   long duration) {
        super(name, description, status, duration);
    }

    public EpicTask getEpicTask() {
        return epicTask;
    }

    public void setEpicTask(EpicTask epicTask) {
        this.epicTask = epicTask;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s",
                getId(),
                "SUBTASK",
                getName(),
                getStatus(),
                getDescription(),
                epicTask.getId(),
                getDuration().toMinutes(),
                getStartTime() != null ?
                        getStartTime().atZone(ZoneId.systemDefault()).toEpochSecond()
                        : null
        );
    }
}
