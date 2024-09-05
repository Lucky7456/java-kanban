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
    protected TaskType type;
    private final int id;
    private TaskStatus status;
    private Duration duration;
    private LocalDateTime startTime;

    public Task(String name, String description, int id, TaskStatus status, long duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = startTime;
        type = TaskType.Task;
    }

    public Task(String name, String description, TaskStatus status, long duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.duration = Duration.ofMinutes(duration);
        this.startTime = startTime;
        id = uniqueIdCounter++;
        this.status = status;
        type = TaskType.Task;
    }

    public Task(String name, String description, TaskStatus status, long duration) {
        this.name = name;
        this.description = description;
        this.duration = Duration.ofMinutes(duration);
        id = uniqueIdCounter++;
        this.status = status;
        startTime = null;
        type = TaskType.Task;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime == null ? null : startTime.plus(duration);
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

    public void setStatus(TaskStatus status) {
        this.status = status;
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
                type,
                getName(),
                getStatus(),
                getDescription(),
                getDuration().toMinutes(),
                getStartTime() == null ? null :
                        getStartTime().atZone(ZoneId.systemDefault()).toEpochSecond()
        );
    }
}
