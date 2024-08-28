package server.processor.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import model.Task;
import server.processor.handler.interfaces.BaseCommandHandler;
import service.interfaces.TaskManager;

import java.io.IOException;

public class UpdateTaskHandler extends BaseCommandHandler {
    public UpdateTaskHandler(String endpoint, TaskManager tm, Gson gson) {
        super(endpoint, tm, gson);
    }

    @Override
    public void processRequest(HttpExchange exchange) throws IOException {
        tm.updateTask(gson.fromJson(readText(exchange), Task.class));
        sendSuccess(exchange);
    }
}