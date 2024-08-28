package server.processor;

import com.google.gson.Gson;
import server.processor.handler.*;
import server.processor.interfaces.BaseHttpProcessor;
import service.interfaces.TaskManager;

import java.util.Arrays;

public class SubTaskProcessor extends BaseHttpProcessor {
    public SubTaskProcessor(TaskManager tm, Gson gson) {
        super(tm, gson);
    }

    @Override
    public void setCommands(TaskManager tm, Gson gson) {
        handlers = Arrays.asList(
                new GetSubTasksHandler("^/subtasksGET$", tm, gson),
                new GetSubTaskByIdHandler("^/subtasks/\\d+GET$", tm, gson),
                new CreateSubTaskHandler("^/subtasksPOST$", tm, gson),
                new UpdateSubTaskHandler("^/subtasks/\\d+POST$", tm, gson),
                new DeleteSubTaskHandler("^/subtasks/\\d+DELETE$", tm, gson)
        );
    }
}
