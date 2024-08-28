package server;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpServer;
import config.Managers;
import server.processor.EpicProcessor;
import server.processor.SubTaskProcessor;
import server.processor.TaskProcessor;
import server.processor.handler.GetHistoryHandler;
import server.processor.handler.GetPrioritizedHandler;
import service.interfaces.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private static final int PORT = 8080;

    private final HttpServer server;

    public HttpTaskServer() throws IOException {
        TaskManager taskManager = Managers.getInmemoryTaskManager();
        Gson gson = Managers.getGson();
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", new TaskProcessor(taskManager, gson));
        server.createContext("/subtasks", new SubTaskProcessor(taskManager, gson));
        server.createContext("/epics", new EpicProcessor(taskManager, gson));
        server.createContext("/history", new GetHistoryHandler("^/historyGET$", taskManager, gson));
        server.createContext("/prioritized", new GetPrioritizedHandler("^/prioritizedGET$", taskManager, gson));
    }

    public void start() {
        System.out.println("server started:\nhttp://localhost:" + PORT);
        server.start();
    }

    public void stop() {
        server.stop(0);
        System.out.println("server stopped");
    }

    public static void main(String[] args) throws IOException {
        HttpTaskServer taskServer = new HttpTaskServer();
        taskServer.start();
    }
}
