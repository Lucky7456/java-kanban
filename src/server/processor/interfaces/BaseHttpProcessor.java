package server.processor.interfaces;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import server.processor.handler.interfaces.BaseCommandHandler;
import service.exceptions.NotFoundException;
import service.interfaces.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public abstract class BaseHttpProcessor implements HttpHandler {
    protected List<BaseCommandHandler> handlers;

    public BaseHttpProcessor(TaskManager tm, Gson gson) {
        setCommands(tm, gson);
    }

    public abstract void setCommands(TaskManager tm, Gson gson);

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try {
            BaseCommandHandler handler = handlers.stream()
                    .filter(h -> h.canHandle(httpExchange))
                    .findAny().orElse(null);

            if (handler == null) throw new NotFoundException("Endpoint not found");

            handler.handle(httpExchange);
        } catch (NotFoundException e) {
            sendError(httpExchange, e.getMessage(), 404);
        } catch (Exception e) {
            sendError(httpExchange, e.getMessage(), 500);
        }
    }

    private void sendError(HttpExchange h, String text, int code) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(code, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }
}
