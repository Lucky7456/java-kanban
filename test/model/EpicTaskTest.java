package model;

import model.enums.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EpicTaskTest {

    private final SubTask st1 = new SubTask("first st", "sub 1", TaskStatus.IN_PROGRESS, 1, 30);
    private final SubTask st2 = new SubTask("second st", "sub 2", TaskStatus.DONE, 1, 30);
    private final SubTask st3 = new SubTask("third st", "sub 3", TaskStatus.NEW, 1, 30);

    @Test
    void ShouldCalculateStatusCorrectlyOnlyNEW() {
        EpicTask et = new EpicTask("epic", "epic with 1 sub");
        et.addSubTask(st3);
        Assertions.assertEquals(et.getStatus(), TaskStatus.NEW);
    }

    @Test
    void ShouldCalculateStatusCorrectlyOnlyDONE() {
        EpicTask et = new EpicTask("epic", "epic with 1 sub");
        et.addSubTask(st2);
        Assertions.assertEquals(et.getStatus(), TaskStatus.DONE);
    }

    @Test
    void ShouldCalculateStatusCorrectlyNEWAndDONE() {
        EpicTask et = new EpicTask("epic", "epic with 2 subs");
        et.addSubTask(st2);
        et.addSubTask(st3);
        Assertions.assertEquals(et.getStatus(), TaskStatus.IN_PROGRESS);
    }

    @Test
    void ShouldCalculateStatusCorrectlyInProgress() {
        EpicTask et = new EpicTask("epic", "epic with 3 subs");
        et.addSubTask(st1);
        et.addSubTask(st2);
        et.addSubTask(st3);
        Assertions.assertEquals(et.getStatus(), TaskStatus.IN_PROGRESS);
    }

    @Test
    void TasksWithSameIdShouldBeEqual() {
        EpicTask epicTask1 = new EpicTask("1", "1", 1);
        EpicTask epicTask2 = new EpicTask("2", "2", 1);
        Assertions.assertEquals(epicTask1, epicTask2, "epic tasks with the same id should be equal");
    }
}