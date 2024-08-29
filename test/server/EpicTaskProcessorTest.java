package server;

import com.google.gson.reflect.TypeToken;
import model.EpicTask;
import model.SubTask;
import org.junit.jupiter.api.Test;
import server.interfaces.BaseProcessorTest;

import java.io.IOException;
import java.lang.reflect.Type;
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

        HttpResponse<String> response = postResponse("http://localhost:8080/epics", task);
        assertEquals(201, response.statusCode());

        List<EpicTask> epictasksFromManager = tm.getEpicTasks();

        assertNotNull(epictasksFromManager, "Задачи не возвращаются");
        assertEquals(1, epictasksFromManager.size(), "Некорректное количество задач");
        assertEquals(task.getName(), epictasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    public void testUpdateEpicTask() throws IOException, InterruptedException {
        EpicTask task = new EpicTask("e2", "de2", 0);

        HttpResponse<String> response = postResponse("http://localhost:8080/epics/0", task);
        assertEquals(201, response.statusCode());

        List<EpicTask> epictasksFromManager = tm.getEpicTasks();

        assertNotNull(epictasksFromManager, "Задачи не возвращаются");
        assertEquals(1, epictasksFromManager.size(), "Некорректное количество задач");
        assertEquals(task.getName(), epictasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetEpicTaskById() throws IOException, InterruptedException {
        HttpResponse<String> response = getResponse("http://localhost:8080/epics/0");
        assertEquals(200, response.statusCode());

        EpicTask epictaskFromJson = gson.fromJson(response.body(), EpicTask.class);

        assertNotNull(epictaskFromJson, "Задачи не возвращаются");
        assertEquals("e", epictaskFromJson.getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetEpicTasks() throws IOException, InterruptedException {
        HttpResponse<String> response = getResponse("http://localhost:8080/epics");
        assertEquals(200, response.statusCode());

        Type epictasksType = new TypeToken<ArrayList<EpicTask>>() {}.getType();
        List<EpicTask> epictasksFromJson = gson.fromJson(response.body(), epictasksType);

        assertNotNull(epictasksFromJson, "Задачи не возвращаются");
        assertEquals(tm.getEpicTasks().size(), epictasksFromJson.size(), "Некорректное количество задач");
        assertEquals(tm.getEpicTasks().getFirst(), epictasksFromJson.getFirst(), "Некорректная задача");
    }

    @Test
    public void testGetEpicSubTasks() throws IOException, InterruptedException {
        HttpResponse<String> response = getResponse("http://localhost:8080/epics/0/subtasks");
        assertEquals(200, response.statusCode());

        Type epictasksType = new TypeToken<ArrayList<SubTask>>() {}.getType();
        List<SubTask> epicSubtasksFromJson = gson.fromJson(response.body(), epictasksType);

        assertNotNull(epicSubtasksFromJson, "Задачи не возвращаются");
        assertEquals(1, epicSubtasksFromJson.size(), "Некорректное количество задач");
        assertEquals(tm.getSubTasks(tm.getEpicTaskById(0)), epicSubtasksFromJson, "Некорректная задача");
    }

    @Test
    public void testRemoveEpicTaskById() throws IOException, InterruptedException {
        HttpResponse<String> response = deleteResponse("http://localhost:8080/epics/0");
        assertEquals(200, response.statusCode());

        List<EpicTask> epictasksFromManager = tm.getEpicTasks();

        assertNotNull(epictasksFromManager, "Задачи не возвращаются");
        assertEquals(0, epictasksFromManager.size(), "Некорректное количество задач");
    }
}
