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
    void historyShouldNotHaveDuplicate() {
        Task task = new Task("second_task_name", "second", 1, TaskStatus.IN_PROGRESS, 45, null);
        hm.add(task);
        hm.add(task);
        hm.add(task);
        hm.add(task);

        assertEquals(hm.getHistory().size(), 2, " size should be 2");
    }

    @Test
    void emptyHistoryShouldWork() {
        HistoryManager history = Managers.getDefaultHistory();

        assertEquals(history.getHistory().size(), 0, " size should be 0");
        assertTrue(history.getHistory().isEmpty(), "history should be empty");
    }

    @Test
    void historyShouldWorkAfterRemovingFromMiddle() {
        Task task1 = new Task("name", "description", 1, TaskStatus.NEW, 45, null);
        Task task2 = new Task("name", "description", 2, TaskStatus.NEW, 45, null);
        Task task3 = new Task("name", "description", 3, TaskStatus.NEW, 45, null);
        Task task4 = new Task("name", "description", 4, TaskStatus.NEW, 45, null);
        Task task5 = new Task("name", "description", 5, TaskStatus.NEW, 45, null);
        hm.add(task1);
        hm.add(task2);
        hm.add(task3);
        hm.add(task4);
        hm.add(task5);

        hm.remove(task3.getId());

        assertNotNull(hm.getHistory(), "history should not be null");
        assertEquals(hm.getHistory().size(), 5, "size should be 0");
        assertFalse(hm.getHistory().isEmpty(), "history should not be empty");
    }

    @Test
    void historyShouldWorkAfterRemovingFromStart() {
        Task task1 = new Task("name", "description", 1, TaskStatus.NEW, 45, null);
        Task task2 = new Task("name", "description", 2, TaskStatus.NEW, 45, null);
        Task task3 = new Task("name", "description", 3, TaskStatus.NEW, 45, null);
        Task task4 = new Task("name", "description", 4, TaskStatus.NEW, 45, null);
        Task task5 = new Task("name", "description", 5, TaskStatus.NEW, 45, null);
        hm.add(task1);
        hm.add(task2);
        hm.add(task3);
        hm.add(task4);
        hm.add(task5);

        hm.remove(0);

        assertNotNull(hm.getHistory(), "history should not be null");
        assertEquals(hm.getHistory().size(), 5, "size should be 0");
        assertFalse(hm.getHistory().isEmpty(), "history should not be empty");
    }

    @Test
    void historyShouldWorkAfterRemovingFromEnd() {
        Task task1 = new Task("name", "description", 1, TaskStatus.NEW, 45, null);
        Task task2 = new Task("name", "description", 2, TaskStatus.NEW, 45, null);
        Task task3 = new Task("name", "description", 3, TaskStatus.NEW, 45, null);
        Task task4 = new Task("name", "description", 4, TaskStatus.NEW, 45, null);
        Task task5 = new Task("name", "description", 5, TaskStatus.NEW, 45, null);
        hm.add(task1);
        hm.add(task2);
        hm.add(task3);
        hm.add(task4);
        hm.add(task5);

        hm.remove(task5.getId());

        assertNotNull(hm.getHistory(), "history should not be null");
        assertEquals(hm.getHistory().size(), 5, "size should be 0");
        assertFalse(hm.getHistory().isEmpty(), "history should not be empty");
    }

    @Test
    void historyShouldBeEmptyAfterRemovingTask() {
        hm.remove(hm.getHistory().getFirst().getId());

        assertNotNull(hm.getHistory(), "history should not be null");
        assertEquals(hm.getHistory().size(), 0, "size should be 0");
        assertTrue(hm.getHistory().isEmpty(), "history should be empty");
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
    void historyShouldNotBeNullOrEmptyAfterPopulating() {
        assertNotNull(hm.getHistory());
        assertFalse(hm.getHistory().isEmpty());
    }
}