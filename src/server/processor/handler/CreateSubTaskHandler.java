package server.processor.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import model.SubTask;
import server.processor.handler.interfaces.BaseCommandHandler;
import service.interfaces.TaskManager;

import java.io.IOException;

public class CreateSubTaskHandler extends BaseCommandHandler {
    public CreateSubTaskHandler(String endpoint, TaskManager tm, Gson gson) {
        super(endpoint, tm, gson);
    }

    @Override
    public void processRequest(HttpExchange exchange) throws IOException {
        tm.createSubTask(gson.fromJson(readText(exchange), SubTask.class));
        sendSuccess(exchange);
    }
}
