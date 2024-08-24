package model;

import model.enums.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubTaskTest {
    @Test
    void TasksWithSameIdShouldBeEqual() {
        SubTask subTask1 = new SubTask("1", "1", 1, TaskStatus.NEW,30, null);
        SubTask subTask2 = new SubTask("2", "2", 1, TaskStatus.IN_PROGRESS,30, null);
        Assertions.assertEquals(subTask1, subTask2, "subtasks with the same id should be equal");
    }

    @Test
    void subTaskShouldStoreEpic() {
        SubTask subTask = new SubTask("first_subtask",
                "first subtask", 1, TaskStatus.DONE,30,null);
        EpicTask epicTask = new EpicTask("first_epic",
                "first epic", 2);

        subTask.setEpicTask(epicTask);
        epicTask.addSubTask(subTask);

        assertEquals(subTask.getEpicTask(), epicTask,
                "sub task should store relevant epic");
    }
}