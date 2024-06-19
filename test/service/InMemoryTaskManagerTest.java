package service;

import model.EpicTask;
import model.SubTask;
import model.Task;
import model.enums.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.interfaces.TaskManager;
import util.Managers;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    private final TaskManager tm = Managers.getDefault();

    @BeforeEach
    void setUp() {
        Task task = new Task("1", "1", 0, TaskStatus.NEW);
        SubTask subTask = new SubTask("2", "2", 1, TaskStatus.NEW);
        EpicTask epicTask = new EpicTask("3", "3", 2);

        tm.createTask(task);
        tm.createSubTask(subTask);
        tm.createEpicTask(epicTask);
    }

    @Test
    void listsShouldNotBeEmptyAfterPopulating() {
        assertFalse(tm.getTasks().isEmpty());
        assertFalse(tm.getSubTasks().isEmpty());
        assertFalse(tm.getEpicTasks().isEmpty());
    }

    @Test
    void shouldReturnTasksById() {
        Task task = tm.getTaskById(0);
        assertNotNull(task);
        assertEquals(task.getName(), "1");

        SubTask subTask = tm.getSubTaskById(1);
        assertNotNull(subTask);
        assertEquals(subTask.getName(), "2");

        EpicTask epicTask = tm.getEpicTaskById(2);
        assertNotNull(epicTask);
        assertEquals(epicTask.getName(), "3");
    }

    @Test
    void taskWithGeneratedIdShouldNotConflictWithTaskWithAssignedId() {
        Task task = new Task("4", "4", TaskStatus.NEW);
        tm.createTask(task);
        assertEquals(tm.getTasks().size(), 1);
        assertNotNull(tm.getTaskById(task.getId()));
        assertEquals(tm.getTaskById(task.getId()).getName(), "4");
    }
}