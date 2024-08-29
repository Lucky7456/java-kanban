package server;

import com.google.gson.reflect.TypeToken;
import model.SubTask;
import model.enums.TaskStatus;
import org.junit.jupiter.api.Test;
import server.interfaces.BaseProcessorTest;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SubTaskProcessorTest extends BaseProcessorTest {
    public SubTaskProcessorTest() throws IOException {
    }

    @Test
    public void testCreateSubTask() throws IOException, InterruptedException {
        SubTask task = new SubTask("s2", "ds2", 1,
                TaskStatus.NEW, 0, 5, null);

        HttpResponse<String> response = postResponse("http://localhost:8080/subtasks", task);
        assertEquals(201, response.statusCode());

        List<SubTask> subtasksFromManager = tm.getSubTasks();

        assertNotNull(subtasksFromManager, "Задачи не возвращаются");
        assertEquals(2, subtasksFromManager.size(), "Некорректное количество задач");
        assertEquals("s2", subtasksFromManager.get(1).getName(), "Некорректное имя задачи");
    }

    @Test
    public void testUpdateSubTask() throws IOException, InterruptedException {
        SubTask task = new SubTask("s2", "s2", 0,
                TaskStatus.NEW, 0, 5, null);

        HttpResponse<String> response = postResponse("http://localhost:8080/subtasks/0", task);
        assertEquals(201, response.statusCode());

        List<SubTask> subtasksFromManager = tm.getSubTasks();

        assertNotNull(subtasksFromManager, "Задачи не возвращаются");
        assertEquals(1, subtasksFromManager.size(), "Некорректное количество задач");
        assertEquals("s2", subtasksFromManager.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetSubTaskById() throws IOException, InterruptedException {
        HttpResponse<String> response = getResponse("http://localhost:8080/subtasks/0");
        assertEquals(200, response.statusCode());

        Type subtaskType = new TypeToken<SubTask>() {}.getType();
        SubTask subtaskFromJson = gson.fromJson(response.body(), subtaskType);

        assertNotNull(subtaskFromJson, "Задачи не возвращаются");
        assertEquals("s", subtaskFromJson.getName(), "Некорректное имя задачи");
    }

    @Test
    public void testGetSubTasks() throws IOException, InterruptedException {
        HttpResponse<String> response = getResponse("http://localhost:8080/subtasks");
        assertEquals(200, response.statusCode());

        Type subtasksType = new TypeToken<ArrayList<SubTask>>() {}.getType();
        List<SubTask> subtasksFromJson = gson.fromJson(response.body(), subtasksType);

        assertNotNull(subtasksFromJson, "Задачи не возвращаются");
        assertEquals(1, subtasksFromJson.size(), "Некорректное количество задач");
        assertEquals("s", subtasksFromJson.getFirst().getName(), "Некорректное имя задачи");
    }

    @Test
    public void testRemoveSubTaskById() throws IOException, InterruptedException {
        HttpResponse<String> response = deleteResponse("http://localhost:8080/subtasks/0");
        assertEquals(200, response.statusCode());

        List<SubTask> subtasksFromManager = tm.getSubTasks();

        assertNotNull(subtasksFromManager, "Задачи не возвращаются");
        assertEquals(0, subtasksFromManager.size(), "Некорректное количество задач");
    }
}
