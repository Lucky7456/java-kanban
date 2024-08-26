package model;

import model.enums.TaskStatus;
import model.enums.TaskType;

import java.time.LocalDateTime;
import java.time.ZoneId;

public class SubTask extends Task {
    private final int epicTaskId;

    public SubTask(String name,
                   String description,
                   int id,
                   TaskStatus status,
                   int epicTaskId,
                   long duration,
                   LocalDateTime startTime) {
        super(name, description, id, status, duration, startTime);
        this.epicTaskId = epicTaskId;
    }

    public SubTask(String name,
                   String description,
                   TaskStatus status,
                   int epicTaskId,
                   long duration,
                   LocalDateTime startTime) {
        super(name, description, status, duration, startTime);
        this.epicTaskId = epicTaskId;
    }

    public SubTask(String name,
                   String description,
                   TaskStatus status,
                   int epicTaskId,
                   long duration) {
        super(name, description, status, duration);
        this.epicTaskId = epicTaskId;
    }

    public int getEpicTaskId() {
        return epicTaskId;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s",
                getId(),
                TaskType.SubTask,
                getName(),
                getStatus(),
                getDescription(),
                epicTaskId,
                getDuration().toMinutes(),
                getStartTime() != null ?
                        getStartTime().atZone(ZoneId.systemDefault()).toEpochSecond()
                        : null
        );
    }
}
