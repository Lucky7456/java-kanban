package server.processor;

import server.processor.handler.*;
import server.processor.interfaces.BaseHttpProcessor;
import service.interfaces.TaskManager;

import java.util.Arrays;

public class EpicProcessor extends BaseHttpProcessor {
    public EpicProcessor(TaskManager tm) {
        super(tm);
    }

    @Override
    public void setCommands(TaskManager tm) {
        handlers = Arrays.asList(
                new GetEpicTasksHandler("^/epicsGET$", tm),
                new GetEpicTaskByIdHandler("^/epics/\\d+GET$", tm),
                new GetEpicSubTasksHandler("^/epics/\\d+/subtasksGET$", tm),
                new CreateEpicTaskHandler("^/epicsPOST$", tm),
                new UpdateEpicTaskHandler("^/epics/\\d+POST$", tm),
                new DeleteEpicTaskHandler("^/epics/\\d+DELETE$", tm)
        );
    }
}
