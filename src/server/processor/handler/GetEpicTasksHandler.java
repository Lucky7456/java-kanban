package server.processor.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import server.processor.handler.interfaces.BaseCommandHandler;
import service.interfaces.TaskManager;

import java.io.IOException;

public class GetEpicTasksHandler extends BaseCommandHandler {
    public GetEpicTasksHandler(String endpoint, TaskManager tm, Gson gson) {
        super(endpoint, tm, gson);
    }

    @Override
    public void processRequest(HttpExchange exchange) throws IOException {
        sendJson(exchange, tm.getEpicTasks());
    }
}
