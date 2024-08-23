package util;

import model.SubTask;
import model.Task;
import model.enums.TaskStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TimeMapperTest {

    @Test
    void mapperShouldCalculateCollisionCorrectly() {
        LocalDateTime start = LocalDateTime.now();
        TimeMapper timeMapper = new TimeMapper(start);

        Task task = new Task("first_task",
                "first task", 0, TaskStatus.NEW,35,start.plusMinutes(30));
        assertFalse(timeMapper.hasCollision(task),"task time should have no collision");
        timeMapper.add(task);
        SubTask task2 = new SubTask("second_task",
                "second task", 1, TaskStatus.NEW,30,start.plusMinutes(65));
        assertFalse(timeMapper.hasCollision(task2),"sub task time should have no collision");
        timeMapper.add(task2);
        Task task3 = new Task("third_task",
                "third task", 2, TaskStatus.NEW,15,start.plusMinutes(90));
        assertTrue(timeMapper.hasCollision(task3),"task time should have collision");
    }
}
