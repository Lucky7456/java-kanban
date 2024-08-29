package server.processor.handler;

import com.sun.net.httpserver.HttpExchange;
import server.processor.handler.interfaces.BaseCommandHandler;
import service.interfaces.TaskManager;

import java.io.IOException;

public class CreateTaskHandler extends BaseCommandHandler {
    public CreateTaskHandler(String endpoint, TaskManager tm) {
        super(endpoint, tm);
    }

    @Override
    public void processRequest(HttpExchange exchange) throws IOException {
        tm.createTask(taskFromJson(exchange));
        sendSuccess(exchange);
    }
}
