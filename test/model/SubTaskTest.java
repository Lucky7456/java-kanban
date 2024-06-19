package model;

import model.enums.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SubTaskTest {
    @Test
    void TasksWithSameIdShouldBeEqual() {
        SubTask subTask1 = new SubTask("1", "1", 1, TaskStatus.NEW);
        SubTask subTask2 = new SubTask("2", "2", 1, TaskStatus.IN_PROGRESS);
        Assertions.assertEquals(subTask1, subTask2, "subtasks with the same id should be equal");
    }
}