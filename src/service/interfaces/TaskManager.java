package service.interfaces;

import model.EpicTask;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    ArrayList<Task> getTasks();

    ArrayList<SubTask> getSubTasks();

    ArrayList<EpicTask> getEpicTasks();

    void removeAllTasks();

    void removeAllSubTasks();

    void removeAllEpicTasks();

    Task getTaskById(int id);

    SubTask getSubTaskById(int id);

    EpicTask getEpicTaskById(int id);

    void createTask(Task task);

    void createSubTask(SubTask st);

    void createEpicTask(EpicTask et);

    void updateTask(Task task);

    void updateSubTask(SubTask st);

    void updateEpicTask(EpicTask et);

    void removeTaskById(int id);

    void removeSubTaskById(int id);

    void removeEpicTaskById(int id);

    ArrayList<SubTask> getSubTasks(EpicTask epicTask);

    List<Task> getHistory();
}
