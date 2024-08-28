package server;

import com.google.gson.reflect.TypeToken;
import model.EpicTask;
import model.SubTask;
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

public class SubTaskProcessorTest extends BaseProcessorTest {
    public SubTaskProcessorTest() throws IOException {
    }

    @Test
    public void testCreateSubTask() throws IOException, InterruptedException {
        EpicTask epic = new EpicTask("e","de", 1);
        tm.createEpicTask(epic);

        SubTask task = new SubTask("s", "ds",
                TaskStatus.NEW, 1,5, LocalDateTime.now());
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<SubTask> subtasksFromManager = tm.getSubTasks();

        assertNotNull(subtasksFromManager, "Задачи не возвращаются");
        assertEquals(1, subtasksFromManager.size(), "Некорректное количество задач");
        assertEquals("s", subtasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    public void testUpdateSubTask() throws IOException, InterruptedException {
        EpicTask epic = new EpicTask("e","de", 1);
        tm.createEpicTask(epic);
        tm.createSubTask(new SubTask("s","ds",0,TaskStatus.NEW,1,0,null));

        SubTask task = new SubTask("Test 2", "Testing task 2",0,
                TaskStatus.NEW, 1,5, LocalDateTime.now());
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/0");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<SubTask> subtasksFromManager = tm.getSubTasks();

        assertNotNull(subtasksFromManager, "Задачи не возвращаются");
        assertEquals(1, subtasksFromManager.size(), "Некорректное количество задач");
        assertEquals("Test 2", subtasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetSubTaskById() throws IOException, InterruptedException {
        EpicTask epic = new EpicTask("e","de", 1);
        tm.createEpicTask(epic);
        tm.createSubTask(new SubTask("s","ds",0,TaskStatus.NEW,1,0,null));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/0");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type subtaskType = new TypeToken<SubTask>(){}.getType();
        Task subtaskFromJson = gson.fromJson(response.body(),subtaskType);

        assertNotNull(subtaskFromJson, "Задачи не возвращаются");
        assertEquals("s", subtaskFromJson.getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetSubTasks() throws IOException, InterruptedException {
        EpicTask epic = new EpicTask("e","de", 1);
        tm.createEpicTask(epic);
        tm.createSubTask(new SubTask("s","ds",0,TaskStatus.NEW,1,0,null));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type subtasksType = new TypeToken<ArrayList<SubTask>>(){}.getType();
        List<SubTask> subtasksFromJson = gson.fromJson(response.body(),subtasksType);

        assertNotNull(subtasksFromJson, "Задачи не возвращаются");
        assertEquals(1, subtasksFromJson.size(), "Некорректное количество задач");
        assertEquals("s", subtasksFromJson.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    public void testRemoveSubTaskById() throws IOException, InterruptedException {
        EpicTask epic = new EpicTask("e","de", 1);
        tm.createEpicTask(epic);
        tm.createSubTask(new SubTask("s","ds",0,TaskStatus.NEW,1,0,null));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks/0");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<SubTask> subtasksFromManager = tm.getSubTasks();

        assertNotNull(subtasksFromManager, "Задачи не возвращаются");
        assertEquals(0, subtasksFromManager.size(), "Некорректное количество задач");
    }
}
