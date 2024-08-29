package server.interfaces;

import com.google.gson.Gson;
import config.Managers;
import model.EpicTask;
import model.SubTask;
import model.Task;
import model.enums.TaskStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import server.HttpTaskServer;
import service.interfaces.TaskManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

public class BaseProcessorTest {
    protected TaskManager tm = Managers.getInmemoryTaskManager();
    protected HttpTaskServer taskServer = new HttpTaskServer(tm);
    protected Gson gson = Managers.getGson();

    public BaseProcessorTest() throws IOException {
    }

    public HttpResponse<String> sendRequest(HttpRequest request) throws IOException, InterruptedException {
        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpRequest.Builder getRequestBuilder(String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url));
    }

    public HttpRequest.BodyPublisher getRequestBody(Object src) {
        return HttpRequest.BodyPublishers
                .ofString(gson.toJson(src));
    }

    public HttpResponse<String> getResponse(String url) throws IOException, InterruptedException {
        return sendRequest(getRequestBuilder(url)
                .GET().build());
    }

    public HttpResponse<String> deleteResponse(String url) throws IOException, InterruptedException {
        return sendRequest(getRequestBuilder(url)
                .DELETE().build());
    }

    public HttpResponse<String> postResponse(String url, Object src) throws IOException, InterruptedException {
        return sendRequest(getRequestBuilder(url)
                .POST(getRequestBody(src)).build());
    }

    @BeforeEach
    public void setUp() {
        tm.removeAllTasks();
        tm.removeAllSubTasks();
        tm.removeAllEpicTasks();
        taskServer.start();
        tm.createTask(new Task("t", "dt", 0, TaskStatus.NEW, 20, LocalDateTime.now()));
        tm.createEpicTask(new EpicTask("e", "de", 0));
        tm.createSubTask(new SubTask("s", "ds", 0, TaskStatus.NEW, 0, 30, LocalDateTime.now().plusMinutes(20)));
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }
}
