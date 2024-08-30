package server.processor;

import server.processor.handler.*;
import server.processor.interfaces.BaseHttpProcessor;
import service.interfaces.TaskManager;

import java.util.Arrays;

public class SubTaskProcessor extends BaseHttpProcessor {
    public SubTaskProcessor(TaskManager tm) {
        super(tm);
    }

    @Override
    public void setCommands(TaskManager tm) {
        handlers = Arrays.asList(
                new GetSubTasksHandler("^/subtasksGET$", tm),
                new GetSubTaskByIdHandler("^/subtasks/\\d+GET$", tm),
                new CreateSubTaskHandler("^/subtasksPOST$", tm),
                new UpdateSubTaskHandler("^/subtasks/\\d+POST$", tm),
                new DeleteSubTaskHandler("^/subtasks/\\d+DELETE$", tm)
        );
    }
}
