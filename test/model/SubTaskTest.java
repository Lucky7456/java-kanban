package model;

import model.enums.TaskStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubTaskTest {
    @Test
    void TasksWithSameIdShouldBeEqual() {
        SubTask subTask1 = new SubTask("1", "1", 1, TaskStatus.NEW, 1, 30, null);
        SubTask subTask2 = new SubTask("2", "2", 1, TaskStatus.IN_PROGRESS, 1, 30, null);
        Assertions.assertEquals(subTask1, subTask2, "subtasks with the same id should be equal");
    }

    @Test
    void subTaskShouldStoreEpic() {
        EpicTask epicTask = new EpicTask("first_epic",
                "first epic", 2);
        SubTask subTask = new SubTask("first_subtask",
                "first subtask", 1, TaskStatus.DONE, epicTask.getId(), 30, null);
        epicTask.addSubTask(subTask);

        assertEquals(subTask.getEpicTaskId(), epicTask.getId(),
                "sub task should store relevant epic");
    }
}