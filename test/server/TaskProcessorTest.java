package server;

import com.google.gson.reflect.TypeToken;
import model.Task;
import model.enums.TaskStatus;
import org.junit.jupiter.api.Test;
import server.interfaces.BaseProcessorTest;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TaskProcessorTest extends BaseProcessorTest {

    public TaskProcessorTest() throws IOException {
    }

    @Test
    public void testCreateTask() throws IOException, InterruptedException {
        Task task = new Task("t2", "dt2", 1, TaskStatus.NEW, 5, null);

        HttpResponse<String> response = postResponse("http://localhost:8080/tasks", task);
        assertEquals(201, response.statusCode());

        List<Task> tasksFromManager = tm.getTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(2, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("t2", tasksFromManager.get(1).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testUpdateTask() throws IOException, InterruptedException {
        Task task = new Task("t2", "dt2", 0, TaskStatus.NEW, 5, LocalDateTime.now());

        HttpResponse<String> response = postResponse("http://localhost:8080/tasks/0", task);
        assertEquals(201, response.statusCode());

        List<Task> tasksFromManager = tm.getTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("t2", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetTaskById() throws IOException, InterruptedException {
        HttpResponse<String> response = getResponse("http://localhost:8080/tasks/0");
        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<Task>() {}.getType();
        Task taskFromJson = gson.fromJson(response.body(), taskType);

        assertNotNull(taskFromJson, "Задачи не возвращаются");
        assertEquals("t", taskFromJson.getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetTasks() throws IOException, InterruptedException {
        HttpResponse<String> response = getResponse("http://localhost:8080/tasks");
        assertEquals(200, response.statusCode());

        Type tasksType = new TypeToken<ArrayList<Task>>() {}.getType();
        List<Task> tasksFromJson = gson.fromJson(response.body(), tasksType);

        assertNotNull(tasksFromJson, "Задачи не возвращаются");
        assertEquals(1, tasksFromJson.size(), "Некорректное количество задач");
        assertEquals("t", tasksFromJson.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    public void testRemoveTaskById() throws IOException, InterruptedException {
        HttpResponse<String> response = deleteResponse("http://localhost:8080/tasks/0");
        assertEquals(200, response.statusCode());

        List<Task> tasksFromManager = tm.getTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(0, tasksFromManager.size(), "Некорректное количество задач");
    }
}
