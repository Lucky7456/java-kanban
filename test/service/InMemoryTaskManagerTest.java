package service;

import model.EpicTask;
import model.SubTask;
import model.Task;
import model.enums.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.interfaces.TaskManager;
import config.Managers;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private final TaskManager tm = new InMemoryTaskManager(Managers.getDefaultHistory());

    @BeforeEach
    void setUp() {
        Task task = new Task("first_task",
                "first task", 0, TaskStatus.NEW,30,null);
        EpicTask epicTask = new EpicTask("first_epic",
                "first epic", 3);
        SubTask subTask = new SubTask("first_subtask",
                "first subtask", 1, TaskStatus.DONE, epicTask.getId(),30,null);
        SubTask subTask2 = new SubTask("second_subtask",
                "second subtask", 2, TaskStatus.IN_PROGRESS, epicTask.getId(),30, null);

        epicTask.addSubTask(subTask);
        epicTask.addSubTask(subTask2);

        tm.createTask(task);
        tm.createSubTask(subTask);
        tm.createSubTask(subTask2);
        tm.createEpicTask(epicTask);
    }

    @Test
    void epicShouldCalculateStatusCorrectly() {
        assertEquals(tm.getEpicTasks().getFirst().getStatus(), TaskStatus.IN_PROGRESS,
                "epic task should calculate status correctly");
    }

    @Test
    void epicTaskShouldStoreRelevantSubtaskId() {
        tm.removeSubTaskById(tm.getSubTasks().getFirst().getId());
        assertEquals(tm.getEpicTasks().getFirst().getSubTasks().getFirst().getId(), 2,
                "epic task should store relevant subtask id");
        assertEquals(tm.getEpicTasks().getFirst().getSubTasks().size(), 1,
                "subtasks size should be 1");
    }

    @Test
    void subTasksShouldBeEmptyAfterRemovingEpicTasks() {
        tm.removeAllEpicTasks();
        assertTrue(tm.getSubTasks().isEmpty(), "subtasks should be empty");
    }

    @Test
    void listsShouldNotBeEmptyAfterPopulating() {
        assertFalse(tm.getTasks().isEmpty());
        assertFalse(tm.getSubTasks().isEmpty());
        assertFalse(tm.getEpicTasks().isEmpty());
    }

    @Test
    void shouldReturnTasksById() {
        Task task = tm.getTaskById(0).orElse(null);
        assertNotNull(task);
        assertEquals(task.getName(), "first_task");

        SubTask subTask = tm.getSubTaskById(1).orElse(null);
        assertNotNull(subTask);
        assertEquals(subTask.getName(), "first_subtask");

        EpicTask epicTask = tm.getEpicTaskById(3).orElse(null);
        assertNotNull(epicTask);
        assertEquals(epicTask.getName(), "first_epic");
    }

    @Test
    void taskWithGeneratedIdShouldNotConflictWithTaskWithAssignedId() {
        Task task = new Task("4", "4", TaskStatus.NEW,30);
        tm.createTask(task);
        Task optTask = tm.getTaskById(task.getId()).orElse(null);
        assertEquals(tm.getTasks().size(), 1);
        assertNotNull(optTask);
        assertEquals(optTask.getName(), "4");
    }
}