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

    public HttpTaskServer(TaskManager tm) throws IOException {
        Gson gson = Managers.getGson();
        server = HttpServer.create(new InetSocketAddress(PORT), 0);
        server.createContext("/tasks", new TaskProcessor(tm, gson));
        server.createContext("/subtasks", new SubTaskProcessor(tm, gson));
        server.createContext("/epics", new EpicProcessor(tm, gson));
        server.createContext("/history", new GetHistoryHandler("^/historyGET$", tm, gson));
        server.createContext("/prioritized", new GetPrioritizedHandler("^/prioritizedGET$", tm, gson));
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
        HttpTaskServer taskServer = new HttpTaskServer(Managers.getInmemoryTaskManager());
        taskServer.start();
    }
}
