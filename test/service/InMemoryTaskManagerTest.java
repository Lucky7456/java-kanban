package service;

import model.EpicTask;
import model.SubTask;
import model.Task;
import model.enums.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.interfaces.TaskManager;
import config.Managers;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private final TaskManager tm = new InMemoryTaskManager(Managers.getDefaultHistory());
    private EpicTask epicTask;
    private SubTask subTask;
    private SubTask subTask3;

    @BeforeEach
    void setUp() {
        Task task = new Task("first_task",
                "first task", 0, TaskStatus.NEW, 30, null);
        epicTask = new EpicTask("first_epic",
                "first epic", 1);
        subTask = new SubTask("first_subtask",
                "first subtask", 2, TaskStatus.DONE, epicTask.getId(), 30, null);
        SubTask subTask2 = new SubTask("second_subtask",
                "second subtask", 3, TaskStatus.IN_PROGRESS, epicTask.getId(), 30, null);
        subTask3 = new SubTask("third_subtask",
                "third subtask", 4, TaskStatus.NEW, epicTask.getId(), 30, null);

        epicTask.addSubTask(subTask);
        epicTask.addSubTask(subTask2);
        epicTask.addSubTask(subTask3);

        tm.createTask(task);
        tm.createEpicTask(epicTask);
        tm.createSubTask(subTask);
        tm.createSubTask(subTask2);
        tm.createSubTask(subTask3);
    }

    @Test
    void ShouldCalculateStatusCorrectlyOnlyNEW() {
        epicTask.removeAllSubTasks();
        tm.createSubTask(subTask3);
        epicTask.addSubTask(subTask3);
        Assertions.assertEquals(epicTask.getStatus(), TaskStatus.NEW);
    }

    @Test
    void ShouldCalculateStatusCorrectlyOnlyDONE() {
        epicTask.removeAllSubTasks();
        tm.createSubTask(subTask);
        epicTask.addSubTask(subTask);
        Assertions.assertEquals(epicTask.getStatus(), TaskStatus.DONE);
    }

    @Test
    void ShouldCalculateStatusCorrectlyNEWAndDONE() {
        epicTask.removeAllSubTasks();
        tm.createSubTask(subTask);
        epicTask.addSubTask(subTask);
        tm.createSubTask(subTask3);
        epicTask.addSubTask(subTask3);
        Assertions.assertEquals(epicTask.getStatus(), TaskStatus.IN_PROGRESS);
    }

    @Test
    void ShouldCalculateStatusCorrectlyInProgress() {
        Assertions.assertEquals(epicTask.getStatus(), TaskStatus.IN_PROGRESS);
    }

    @Test
    void epicShouldCalculateStatusCorrectly() {
        assertEquals(epicTask.getStatus(), TaskStatus.IN_PROGRESS,
                "epic task should calculate status correctly");
    }

    @Test
    void epicTaskShouldStoreRelevantSubtaskId() {
        assertEquals(epicTask.getSubTaskIds().getFirst(), subTask.getId(),
                "epic task should store relevant subtask id");
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

        SubTask st = tm.getSubTaskById(subTask.getId()).orElse(null);
        assertNotNull(st);
        assertEquals(st.getName(), "first_subtask");

        EpicTask et = tm.getEpicTaskById(epicTask.getId()).orElse(null);
        assertNotNull(et);
        assertEquals(et.getName(), "first_epic");
    }

    @Test
    void taskWithGeneratedIdShouldNotConflictWithTaskWithAssignedId() {
        Task task = new Task("4", "4", TaskStatus.NEW, 30);
        tm.createTask(task);
        Task optTask = tm.getTaskById(task.getId()).orElse(null);
        assertEquals(tm.getTasks().size(), 1);
        assertNotNull(optTask);
        assertEquals(optTask.getName(), "4");
    }
}