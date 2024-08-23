package model;

import model.enums.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TaskTest {

    @Test
    void TasksWithSameIdShouldBeEqual() {
        Task task1 = new Task("1", "1", 1, TaskStatus.NEW,30, null);
        Task task2 = new Task("2", "2", 1, TaskStatus.IN_PROGRESS, 45, null);
        Assertions.assertEquals(task1, task2, "tasks with the same id should be equal");
    }
}