package server;

import com.google.gson.reflect.TypeToken;
import model.SubTask;
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

public class HttpHandlersTest extends BaseProcessorTest {
    public HttpHandlersTest() throws IOException {
    }

    @Test
    public void testGetHistory() throws IOException, InterruptedException {
        tm.getTaskById(0);
        tm.getEpicTaskById(0);

        HttpResponse<String> response = getResponse("http://localhost:8080/history");
        assertEquals(200, response.statusCode());

        Type historyType = new TypeToken<ArrayList<Task>>() {}.getType();
        List<Task> historyFromJson = gson.fromJson(response.body(), historyType);

        assertNotNull(historyFromJson, "Задачи не возвращаются");
        assertEquals(tm.getHistory().size(), historyFromJson.size(), "Некорректное количество задач");
        assertEquals(tm.getHistory().getFirst().getName(), historyFromJson.getFirst().getName(), "Некорректные задачи");
    }

    @Test
    public void testGetPrioritizedTasks() throws IOException, InterruptedException {
        HttpResponse<String> response = getResponse("http://localhost:8080/prioritized");
        assertEquals(200, response.statusCode());

        Type prioritizedTasksType = new TypeToken<ArrayList<Task>>() {}.getType();
        List<Task> prioritizedTasksFromJson = gson.fromJson(response.body(), prioritizedTasksType);

        assertNotNull(prioritizedTasksFromJson, "Задачи не возвращаются");
        assertEquals(tm.getPrioritizedTasks().size(), prioritizedTasksFromJson.size(), "Некорректное количество задач");
        assertEquals(tm.getPrioritizedTasks().getFirst(), prioritizedTasksFromJson.getFirst(), "Некорректные задачи");
    }

    @Test
    public void testHasIntersection() throws IOException, InterruptedException {
        SubTask subTask = new SubTask("s", "ds", 2,
                TaskStatus.NEW, 1, 10, LocalDateTime.now());

        HttpResponse<String> response = postResponse("http://localhost:8080/subtasks", subTask);
        assertEquals(406, response.statusCode());
    }

    @Test
    public void testNotFoundTask() throws IOException, InterruptedException {
        HttpResponse<String> response = getResponse("http://localhost:8080/tasks/0005");
        assertEquals(404, response.statusCode());
    }

    @Test
    public void testNotFoundEndpoint() throws IOException, InterruptedException {
        HttpResponse<String> response = getResponse("http://localhost:8080/tasks//");
        assertEquals(404, response.statusCode());
    }
}
