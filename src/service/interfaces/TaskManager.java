package service.interfaces;

import model.EpicTask;
import model.SubTask;
import model.Task;

import java.util.List;

public interface TaskManager {
    List<Task> getPrioritizedTasks();

    List<Task> getAllTasks();

    List<Task> getTasks();

    List<SubTask> getSubTasks();

    List<EpicTask> getEpicTasks();

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

    List<SubTask> getSubTasks(EpicTask epicTask);

    List<Task> getHistory();
}
