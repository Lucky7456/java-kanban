package server.processor;

import com.google.gson.Gson;
import server.processor.handler.*;
import server.processor.interfaces.BaseHttpProcessor;
import service.interfaces.TaskManager;

import java.util.Arrays;

public class TaskProcessor extends BaseHttpProcessor {
    public TaskProcessor(TaskManager tm, Gson gson) {
        super(tm, gson);
    }

    @Override
    public void setCommands(TaskManager tm, Gson gson) {
        handlers = Arrays.asList(
                new GetTasksHandler("^/tasksGET$", tm, gson),
                new GetTaskByIdHandler("^/tasks/\\d+GET$", tm, gson),
                new CreateTaskHandler("^/tasksPOST$", tm, gson),
                new UpdateTaskHandler("^/tasks/\\d+POST$", tm, gson),
                new DeleteTaskHandler("^/tasks/\\d+DELETE$", tm, gson)
        );
    }
}
