package service;

import model.Task;
import model.enums.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.interfaces.HistoryManager;
import config.Managers;

class InMemoryHistoryManagerTest {
    private final HistoryManager hm = Managers.getDefaultHistory();

    @BeforeEach
    void setUp() {
        Task task = new Task("1", "1", 0, TaskStatus.NEW);
        hm.add(task);
    }

    @Test
    void historyShouldSaveTaskState() {
        Task task = new Task("2", "2", 0, TaskStatus.IN_PROGRESS);
        hm.add(task);

        Task historyTask1 = hm.getHistory().getFirst();
        Assertions.assertEquals(historyTask1.getName(), "1");
        Assertions.assertEquals(historyTask1.getStatus(), TaskStatus.NEW);

        Task historyTask2 = hm.getHistory().getLast();
        Assertions.assertEquals(historyTask2.getName(), "2");
        Assertions.assertEquals(historyTask2.getStatus(), TaskStatus.IN_PROGRESS);
    }

    @Test
    void historyShouldNotBeNullOrEmptyAfterPopulating() {
        Assertions.assertNotNull(hm.getHistory());
        Assertions.assertFalse(hm.getHistory().isEmpty());
    }
}