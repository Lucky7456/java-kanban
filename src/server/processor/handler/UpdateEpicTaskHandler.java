package server.processor.handler;

import com.sun.net.httpserver.HttpExchange;
import server.processor.handler.interfaces.BaseCommandHandler;
import service.interfaces.TaskManager;

import java.io.IOException;

public class UpdateEpicTaskHandler extends BaseCommandHandler {
    public UpdateEpicTaskHandler(String endpoint, TaskManager tm) {
        super(endpoint, tm);
    }

    @Override
    public void processRequest(HttpExchange exchange) throws IOException {
        tm.updateEpicTask(epicFromJson(exchange));
        sendSuccess(exchange);
    }
}
