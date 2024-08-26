package service.interfaces;

import model.EpicTask;
import model.SubTask;
import model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskManager {
    List<Task> getPrioritizedTasks();

    List<Task> getAllTasks();

    List<Task> getTasks();

    List<SubTask> getSubTasks();

    List<EpicTask> getEpicTasks();

    void removeAllTasks();

    void removeAllSubTasks();

    void removeAllEpicTasks();

    Optional<Task> getTaskById(int id);

    Optional<SubTask> getSubTaskById(int id);

    Optional<EpicTask> getEpicTaskById(int id);

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
