package server.processor.handler;

import com.sun.net.httpserver.HttpExchange;
import server.processor.handler.interfaces.BaseCommandHandler;
import service.interfaces.TaskManager;

import java.io.IOException;

public class GetEpicTaskByIdHandler extends BaseCommandHandler {
    public GetEpicTaskByIdHandler(String endpoint, TaskManager tm) {
        super(endpoint, tm);
    }

    @Override
    public void processRequest(HttpExchange exchange) throws IOException {
        sendJson(exchange, tm.getEpicTaskById(getRequestId(exchange)));
    }
}
