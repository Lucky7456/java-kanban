package service;

import config.Managers;
import model.EpicTask;
import model.SubTask;
import model.Task;
import model.enums.TaskStatus;
import model.enums.TaskType;
import service.exceptions.ManagerLoadException;
import service.exceptions.ManagerSaveException;
import service.interfaces.HistoryManager;
import service.interfaces.TaskManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private static final String CSV_HEADER = "id,type,name,status,description,epic,duration,startTime\n";
    private final File saveFile;

    public FileBackedTaskManager(HistoryManager historyManager, File saveFile) {
        super(historyManager);
        this.saveFile = saveFile;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fbtm = new FileBackedTaskManager(Managers.getDefaultHistory(), file);
        try {
            loadTasksFromString(Files.readString(file.toPath()), fbtm);
        } catch (IOException e) {
            throw new ManagerLoadException();
        }
        return fbtm;
    }

    private static void loadTasksFromString(String data, TaskManager tm) {
        String[] taskLines = data.split("\n");
        for (String taskLine : taskLines) {
            String[] taskData = taskLine.split(",");
            if (taskData[1].equals("type")) continue;
            TaskType taskType = TaskType.valueOf(taskData[1]);
            String name = taskData[2];
            String description = taskData[4];
            int id = Integer.parseInt(taskData[0]);
            TaskStatus status = TaskStatus.valueOf(taskData[3]);
            int epicId = Integer.parseInt(taskData[5]);
            long duration = Long.parseLong(taskData[6]);
            boolean hasStartTime = !taskData[7].equals("null");
            LocalDateTime startTime = hasStartTime ?
                    LocalDateTime.ofInstant(Instant.ofEpochSecond(Long.parseLong(taskData[7])), ZoneId.systemDefault())
                    : null;

            switch (taskType) {
                case TaskType.Task:
                    Task task = new Task(
                            name,
                            description,
                            id,
                            status,
                            duration,
                            startTime
                    );
                    tm.createTask(task);
                    break;
                case TaskType.EpicTask:
                    EpicTask epicTask = new EpicTask(
                            name,
                            description,
                            id
                    );
                    tm.createEpicTask(epicTask);
                    break;
                case TaskType.SubTask:
                    SubTask subTask = new SubTask(
                            name,
                            description,
                            id,
                            status,
                            epicId,
                            duration,
                            startTime
                    );
                    tm.getEpicTaskById(epicId).addSubTask(subTask);
                    tm.createSubTask(subTask);
                    break;
            }
        }
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile))) {
            writer.write(saveTasksToString());
        } catch (IOException e) {
            throw new ManagerSaveException();
        }
    }

    private String saveTasksToString() {
        StringBuilder data = new StringBuilder(CSV_HEADER);
        getAllTasks().forEach(task -> data.append(task.toString()).append("\n"));
        return data.toString();
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void createSubTask(SubTask subtask) {
        super.createSubTask(subtask);
        save();
    }

    @Override
    public void createEpicTask(EpicTask et) {
        super.createEpicTask(et);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubTask(SubTask st) {
        super.updateSubTask(st);
        save();
    }

    @Override
    public void updateEpicTask(EpicTask et) {
        super.updateEpicTask(et);
        save();
    }

    @Override
    public Task removeTaskById(int id) {
        super.removeTaskById(id);
        save();
        return null;
    }

    @Override
    public SubTask removeSubTaskById(int id) {
        super.removeSubTaskById(id);
        save();
        return null;
    }

    @Override
    public EpicTask removeEpicTaskById(int id) {
        super.removeEpicTaskById(id);
        save();
        return null;
    }

    @Override
    public void removeAllTasks() {
        super.removeAllTasks();
        save();
    }

    @Override
    public void removeAllSubTasks() {
        super.removeAllSubTasks();
        save();
    }

    @Override
    public void removeAllEpicTasks() {
        super.removeAllEpicTasks();
        save();
    }
}
