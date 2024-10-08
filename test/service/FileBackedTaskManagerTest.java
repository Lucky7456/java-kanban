package service;

import model.EpicTask;
import model.SubTask;
import model.Task;
import model.enums.TaskStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.interfaces.TaskManager;
import config.Managers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTest {
    private TaskManager tm;
    private File file;

    @BeforeEach
    void setUp() {
        try {
            file = File.createTempFile("test", null);
            tm = Managers.getDefault(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Task t1 = new Task("first", "to do", TaskStatus.NEW, 30);
        Task t2 = new Task("second", "to do", TaskStatus.NEW, 30);

        tm.createTask(t1);
        tm.createTask(t2);

        EpicTask et1 = new EpicTask("first epic", "epic with 1 sub");
        SubTask st1 = new SubTask("first st", "sub 1", TaskStatus.NEW, et1.getId(), 30);
        et1.addSubTask(st1);

        tm.createEpicTask(et1);
        tm.createSubTask(st1);
    }

    @Test
    void shouldSaveAndLoadEmptyFile() {
        tm.removeAllTasks();
        tm.removeAllEpicTasks();

        tm = FileBackedTaskManager.loadFromFile(file);

        assertTrue(tm.getTasks().isEmpty(), "tasks should be empty");
        assertTrue(tm.getSubTasks().isEmpty(), "sub tasks should be empty");
        assertTrue(tm.getEpicTasks().isEmpty(), "epic tasks should be empty");
    }

    @Test
    void testSaveFileException() {
        assertDoesNotThrow(() -> tm.removeAllTasks(), "saving file should not throw exception");
    }

    @Test
    void testLoadFileException() {
        assertDoesNotThrow(() -> {
            FileBackedTaskManager.loadFromFile(file);
        }, "loading file should not throw exception");
    }

    @Test
    void shouldSaveTasksToFile() {
        String load;
        try {
            load = Files.readString(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        StringBuilder save = new StringBuilder("id,type,name,status,description,epic,duration,startTime\n");
        tm.getAllTasks().forEach(task -> save.append(task.toString()).append("\n"));

        assertEquals(save.toString(), load, "should save tasks to file");
    }

    @Test
    void shouldLoadTasksFromFile() {
        SubTask subTask = tm.getSubTasks().getFirst();

        TaskManager taskManager = FileBackedTaskManager.loadFromFile(file);
        SubTask optSt = taskManager.getSubTaskById(subTask.getId());
        assertNotNull(optSt);
        assertEquals(optSt.getEpicTaskId(), subTask.getEpicTaskId(), "sub tasks should have equal epic tasks");
        assertEquals(taskManager.getTasks().size(), tm.getTasks().size(), "task list sizes should be equal");
        assertEquals(taskManager.getSubTasks().size(), tm.getSubTasks().size(), "sub task list sizes should be equal");
        assertEquals(taskManager.getEpicTasks().size(), tm.getEpicTasks().size(), "task list sizes should be equal");
    }
}
