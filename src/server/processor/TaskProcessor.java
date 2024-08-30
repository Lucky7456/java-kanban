package server.processor;

import server.processor.handler.*;
import server.processor.interfaces.BaseHttpProcessor;
import service.interfaces.TaskManager;

import java.util.Arrays;

public class TaskProcessor extends BaseHttpProcessor {
    public TaskProcessor(TaskManager tm) {
        super(tm);
    }

    @Override
    public void setCommands(TaskManager tm) {
        handlers = Arrays.asList(
                new GetTasksHandler("^/tasksGET$", tm),
                new GetTaskByIdHandler("^/tasks/\\d+GET$", tm),
                new CreateTaskHandler("^/tasksPOST$", tm),
                new UpdateTaskHandler("^/tasks/\\d+POST$", tm),
                new DeleteTaskHandler("^/tasks/\\d+DELETE$", tm)
        );
    }
}
