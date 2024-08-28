package server;

import com.google.gson.reflect.TypeToken;
import model.Task;
import model.enums.TaskStatus;
import org.junit.jupiter.api.Test;
import server.interfaces.BaseProcessorTest;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
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
        Task task = new Task("t", "dt",
                TaskStatus.NEW, 5, LocalDateTime.now());
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Task> tasksFromManager = tm.getTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("t", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    public void testUpdateTask() throws IOException, InterruptedException {
        tm.createTask(new Task("t","dt",0,TaskStatus.NEW,0,null));

        Task task = new Task("t2", "dt2",0,
                TaskStatus.NEW, 5, LocalDateTime.now());
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/0");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<Task> tasksFromManager = tm.getTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач");
        assertEquals("t2", tasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetTaskById() throws IOException, InterruptedException {
        tm.createTask(new Task("t","dt",0,TaskStatus.NEW,0,null));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/0");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type taskType = new TypeToken<Task>(){}.getType();
        Task taskFromJson = gson.fromJson(response.body(),taskType);

        assertNotNull(taskFromJson, "Задачи не возвращаются");
        assertEquals("t", taskFromJson.getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetTasks() throws IOException, InterruptedException {
        tm.createTask(new Task("t","dt",0,TaskStatus.NEW,0,null));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type tasksType = new TypeToken<ArrayList<Task>>(){}.getType();
        List<Task> tasksFromJson = gson.fromJson(response.body(),tasksType);

        assertNotNull(tasksFromJson, "Задачи не возвращаются");
        assertEquals(1, tasksFromJson.size(), "Некорректное количество задач");
        assertEquals("t", tasksFromJson.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    public void testRemoveTaskById() throws IOException, InterruptedException {
        tm.createTask(new Task("t","dt",0,TaskStatus.NEW,0,null));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/0");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<Task> tasksFromManager = tm.getTasks();

        assertNotNull(tasksFromManager, "Задачи не возвращаются");
        assertEquals(0, tasksFromManager.size(), "Некорректное количество задач");
    }
}
