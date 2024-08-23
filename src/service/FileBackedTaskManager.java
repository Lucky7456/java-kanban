package service;

import config.Managers;
import model.EpicTask;
import model.SubTask;
import model.Task;
import model.enums.TaskStatus;
import service.exceptions.ManagerLoadException;
import service.exceptions.ManagerSaveException;
import service.interfaces.HistoryManager;
import service.interfaces.TaskManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class FileBackedTaskManager extends InMemoryTaskManager {
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
            switch (taskData[1]) {
                case "TASK":
                    Task task = new Task(
                            taskData[2],
                            taskData[4],
                            Integer.parseInt(taskData[0]),
                            TaskStatus.valueOf(taskData[3])
                    );
                    tm.createTask(task);
                    break;
                case "EPIC":
                    EpicTask epicTask = new EpicTask(
                            taskData[2],
                            taskData[4],
                            Integer.parseInt(taskData[0])
                    );
                    tm.createEpicTask(epicTask);
                    break;
                case "SUBTASK":
                    SubTask subTask = new SubTask(
                            taskData[2],
                            taskData[4],
                            Integer.parseInt(taskData[0]),
                            TaskStatus.valueOf(taskData[3])
                    );
                    EpicTask et = tm.getEpicTaskById(Integer.parseInt(taskData[5]));
                    et.addSubTask(subTask);
                    subTask.setEpicTask(et);
                    tm.createSubTask(subTask);
                    break;
                case "type":
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
        StringBuilder data = new StringBuilder("id,type,name,status,description,epic\n");
        for (Task task : getAllTasks()) {
            data.append(task.toString()).append("\n");
        }
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
    public void removeTaskById(int id) {
        super.removeTaskById(id);
        save();
    }

    @Override
    public void removeSubTaskById(int id) {
        super.removeSubTaskById(id);
        save();
    }

    @Override
    public void removeEpicTaskById(int id) {
        super.removeEpicTaskById(id);
        save();
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
