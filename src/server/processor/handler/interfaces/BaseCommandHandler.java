package server.processor.handler.interfaces;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import service.exceptions.IntersectionException;
import service.exceptions.NotFoundException;
import service.interfaces.TaskManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public abstract class BaseCommandHandler implements HttpHandler {
    private final String endpoint;
    protected final TaskManager tm;
    protected final Gson gson;

    public BaseCommandHandler(String endpoint, TaskManager tm, Gson gson) {
        this.endpoint = endpoint;
        this.tm = tm;
        this.gson = gson;
    }

    public boolean canHandle(HttpExchange h) {
        String path = h.getRequestURI().getPath();
        String requestMethod = h.getRequestMethod();
        return Pattern.matches(endpoint, path + requestMethod);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            processRequest(exchange);
        } catch (IntersectionException e) {
            sendHasIntersections(exchange);
        } catch (NotFoundException e) {
            sendNotFound(exchange, e.getMessage());
        } catch (Exception e) {
            sendResponse(exchange, e.getMessage(), 500);
        }
    }

    protected abstract void processRequest(HttpExchange exchange) throws IOException;

    protected int getRequestId(HttpExchange exchange) {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        try {
            return Integer.parseInt(pathParts[2]);
        } catch (NumberFormatException exception) {
            throw new NotFoundException("id not found");
        }
    }

    protected String readText(HttpExchange h) throws IOException {
        return new String(h.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
    }

    protected void sendJson(HttpExchange h, Object src) throws IOException {
        String response = gson.toJson(src);
        sendText(h, response);
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        sendResponse(h, text, 200);
    }

    protected void sendNotFound(HttpExchange h, String text) throws IOException {
        sendResponse(h, text, 404);
    }

    protected void sendHasIntersections(HttpExchange h) throws IOException {
        sendResponse(h, "task has intersection", 406);
    }

    protected void sendSuccess(HttpExchange h) throws IOException {
        h.sendResponseHeaders(201, -1);
        h.close();
    }

    protected void sendResponse(HttpExchange h, String text, int code) throws IOException {
        byte[] resp = text.getBytes(StandardCharsets.UTF_8);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(code, resp.length);
        h.getResponseBody().write(resp);
        h.close();
    }
}
