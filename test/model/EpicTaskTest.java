package model;

import org.junit.jupiter.api.*;

class EpicTaskTest {

    @Test
    void TasksWithSameIdShouldBeEqual() {
        EpicTask epicTask1 = new EpicTask("1", "1", 1);
        EpicTask epicTask2 = new EpicTask("2", "2", 1);
        Assertions.assertEquals(epicTask1, epicTask2, "epic tasks with the same id should be equal");
    }
}