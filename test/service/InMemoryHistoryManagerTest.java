package service;

import model.Task;
import model.enums.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.interfaces.HistoryManager;
import config.Managers;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private HistoryManager hm;

    @BeforeEach
    void setUp() {
        hm = Managers.getDefaultHistory();
        Task task = new Task("first_task_name", "first", 0, TaskStatus.NEW, 30, null);
        hm.add(task);
    }

    @Test
    void historyShouldSaveOrderOfSeenTasks() {
        Task task = new Task("second_task_name", "second", 1, TaskStatus.IN_PROGRESS, 45, null);
        hm.add(task);

        Task historyTask1 = hm.getHistory().getFirst();
        assertEquals(historyTask1.getName(), "first_task_name");
        assertEquals(historyTask1.getStatus(), TaskStatus.NEW);
        assertEquals(hm.getHistory().size(), 2, " size should be 2");
    }

    @Test
    void historyShouldBeEmptyAfterRemovingTask() {
        hm.remove(hm.getHistory().getFirst().getId());

        assertNotNull(hm.getHistory(), "history should not be null");
        assertEquals(hm.getHistory().size(), 0, "size should be 0");
        assertTrue(hm.getHistory().isEmpty(), "history should be empty");
    }

    @Test
    void historyShouldNotBeNullOrEmptyAfterPopulating() {
        assertNotNull(hm.getHistory());
        assertFalse(hm.getHistory().isEmpty());
    }
}