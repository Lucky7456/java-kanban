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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EpicTaskProcessorTest extends BaseProcessorTest {
    public EpicTaskProcessorTest() throws IOException {
    }

    @Test
    public void testCreateEpicTask() throws IOException, InterruptedException {
        EpicTask task = new EpicTask("e", "de", 0);
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<EpicTask> epictasksFromManager = tm.getEpicTasks();

        assertNotNull(epictasksFromManager, "Задачи не возвращаются");
        assertEquals(1, epictasksFromManager.size(), "Некорректное количество задач");
        assertEquals(task.getName(), epictasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    public void testUpdateEpicTask() throws IOException, InterruptedException {
        EpicTask epic = new EpicTask("e", "de", 0);
        tm.createEpicTask(epic);

        EpicTask task = new EpicTask("e2", "de2", 0);
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/0");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());

        List<EpicTask> epictasksFromManager = tm.getEpicTasks();

        assertNotNull(epictasksFromManager, "Задачи не возвращаются");
        assertEquals(1, epictasksFromManager.size(), "Некорректное количество задач");
        assertEquals(task.getName(), epictasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetEpicTaskById() throws IOException, InterruptedException {
        EpicTask epic = new EpicTask("e", "de", 0);
        tm.createEpicTask(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/0");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type epictaskType = new TypeToken<SubTask>() {
        }.getType();
        Task epictaskFromJson = gson.fromJson(response.body(), epictaskType);

        assertNotNull(epictaskFromJson, "Задачи не возвращаются");
        assertEquals(epic.getName(), epictaskFromJson.getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetEpicTasks() throws IOException, InterruptedException {
        EpicTask epic = new EpicTask("e", "de", 0);
        tm.createEpicTask(epic);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type epictasksType = new TypeToken<ArrayList<EpicTask>>() {
        }.getType();
        List<EpicTask> epictasksFromJson = gson.fromJson(response.body(), epictasksType);

        assertNotNull(epictasksFromJson, "Задачи не возвращаются");
        assertEquals(tm.getEpicTasks().size(), epictasksFromJson.size(), "Некорректное количество задач");
        assertEquals(tm.getEpicTasks().getFirst(), epictasksFromJson.getFirst(), "Некорректная задача");
    }

    @Test
    public void testGetEpicSubTasks() throws IOException, InterruptedException {
        EpicTask epic = new EpicTask("e", "de", 0);
        tm.createEpicTask(epic);
        tm.createSubTask(new SubTask("t", "t", 1, TaskStatus.NEW, 0, 0, null));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/0/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type epictasksType = new TypeToken<ArrayList<SubTask>>() {
        }.getType();
        List<SubTask> epicSubtasksFromJson = gson.fromJson(response.body(), epictasksType);

        assertNotNull(epicSubtasksFromJson, "Задачи не возвращаются");
        assertEquals(tm.getSubTasks(epic).size(), epicSubtasksFromJson.size(), "Некорректное количество задач");
        assertEquals(tm.getSubTasks(epic), epicSubtasksFromJson, "Некорректная задача");
    }

    @Test
    public void testRemoveEpicTaskById() throws IOException, InterruptedException {
        EpicTask epic = new EpicTask("e", "de", 0);
        tm.createEpicTask(epic);
        tm.createSubTask(new SubTask("t", "t", 1, TaskStatus.NEW, 0, 0, null));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/epics/0");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url).DELETE().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        List<EpicTask> epictasksFromManager = tm.getEpicTasks();

        assertNotNull(epictasksFromManager, "Задачи не возвращаются");
        assertEquals(0, epictasksFromManager.size(), "Некорректное количество задач");
    }
}
