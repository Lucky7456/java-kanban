package server.processor.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import model.EpicTask;
import server.processor.handler.interfaces.BaseCommandHandler;
import service.interfaces.TaskManager;

import java.io.IOException;

public class UpdateEpicTaskHandler extends BaseCommandHandler {
    public UpdateEpicTaskHandler(String endpoint, TaskManager tm, Gson gson) {
        super(endpoint, tm, gson);
    }

    @Override
    public void processRequest(HttpExchange exchange) throws IOException {
        tm.updateEpicTask(gson.fromJson(readText(exchange), EpicTask.class));
        sendSuccess(exchange);
    }
}
