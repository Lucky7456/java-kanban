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

public class HttpHandlersTest extends BaseProcessorTest {
    public HttpHandlersTest() throws IOException {
    }

    @Test
    public void testGetHistory() throws IOException, InterruptedException {
        tm.createTask(new Task("t", "dt", 0, TaskStatus.NEW, 0, null));
        tm.createEpicTask(new EpicTask("e", "de", 1));
        tm.getTaskById(0);
        tm.getEpicTaskById(1);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/history");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type historyType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> historyFromJson = gson.fromJson(response.body(), historyType);

        assertNotNull(historyFromJson, "Задачи не возвращаются");
        assertEquals(tm.getHistory().size(), historyFromJson.size(), "Некорректное количество задач");
        assertEquals(tm.getHistory().getFirst(), historyFromJson.getFirst(), "Некорректные задачи");
    }

    @Test
    public void testGetPrioritizedTasks() throws IOException, InterruptedException {
        tm.createTask(new Task("t", "dt", 0, TaskStatus.NEW, 30, LocalDateTime.now()));
        tm.createEpicTask(new EpicTask("e", "de", 1));
        tm.createSubTask(new SubTask("s", "ds", 2, TaskStatus.NEW, 1, 10, LocalDateTime.now().plusMinutes(30)));

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/prioritized");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());

        Type prioritizedTasksType = new TypeToken<ArrayList<Task>>() {
        }.getType();
        List<Task> prioritizedTasksFromJson = gson.fromJson(response.body(), prioritizedTasksType);

        assertNotNull(prioritizedTasksFromJson, "Задачи не возвращаются");
        assertEquals(tm.getPrioritizedTasks().size(), prioritizedTasksFromJson.size(), "Некорректное количество задач");
        assertEquals(tm.getPrioritizedTasks().getFirst(), prioritizedTasksFromJson.getFirst(), "Некорректные задачи");
    }

    @Test
    public void testHasIntersection() throws IOException, InterruptedException {
        tm.createTask(new Task("t", "dt", 0, TaskStatus.NEW, 30, LocalDateTime.now()));
        tm.createEpicTask(new EpicTask("e", "de", 1));

        SubTask subTask = new SubTask("s", "ds", 2,
                TaskStatus.NEW, 1, 10, LocalDateTime.now());
        String taskJson = gson.toJson(subTask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url).POST(HttpRequest.BodyPublishers.ofString(taskJson)).build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(406, response.statusCode());
    }

    @Test
    public void testNotFoundTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/0005");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }

    @Test
    public void testNotFoundEndpoint() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks//");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url).GET().build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(404, response.statusCode());
    }
}
