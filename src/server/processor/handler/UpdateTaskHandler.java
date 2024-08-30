package server.processor.handler;

import com.sun.net.httpserver.HttpExchange;
import server.processor.handler.interfaces.BaseCommandHandler;
import service.interfaces.TaskManager;

import java.io.IOException;

public class UpdateTaskHandler extends BaseCommandHandler {
    public UpdateTaskHandler(String endpoint, TaskManager tm) {
        super(endpoint, tm);
    }

    @Override
    public void processRequest(HttpExchange exchange) throws IOException {
        tm.updateTask(taskFromJson(exchange));
        sendSuccess(exchange);
    }
}
