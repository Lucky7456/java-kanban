package model;

import model.enums.TaskStatus;

public class SubTask extends Task {
    private EpicTask epicTask;

    public SubTask(String name,
                   String description,
                   int id,
                   TaskStatus status) {
        super(name, description, id, status);
    }

    public SubTask(String name,
                   String description,
                   TaskStatus status) {
        super(name, description, status);
    }

    public EpicTask getEpicTask() {
        return epicTask;
    }

    public void setEpicTask(EpicTask epicTask) {
        this.epicTask = epicTask;
    }

    @Override
    public String toString() {
        return "SubTask{" +
                "epicTask.getName()='" + epicTask.getName() + '\'' +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                '}';
    }
}
