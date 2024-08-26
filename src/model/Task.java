package model;

import model.enums.TaskStatus;
import model.enums.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

public class Task {
    private static int uniqueIdCounter;
    private final String name;
    private final String description;
    private final int id;
    private final TaskStatus status;
    private final Duration duration;
    private final LocalDateTime startTime;

    public Task(String name, String description, int id, TaskStatus status, long duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = startTime;
    }

    public Task(String name, String description, TaskStatus status, long duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = startTime;
        this.id = uniqueIdCounter++;
        this.status = status;
    }

    public Task(String name, String description, TaskStatus status, long duration) {
        this.name = name;
        this.description = description;
        this.duration = Duration.ofMinutes(duration);
        this.id = uniqueIdCounter++;
        this.status = status;
        startTime = null;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime != null ? startTime.plus(duration) : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    public int getId() {
        return id;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return String.format("%s,%s,%s,%s,%s,0,%s,%s",
                    getId(),
                    TaskType.Task,
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
