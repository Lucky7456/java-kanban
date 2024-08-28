package server.processor;

import com.google.gson.Gson;
import server.processor.handler.*;
import server.processor.interfaces.BaseHttpProcessor;
import service.interfaces.TaskManager;

import java.util.Arrays;

public class EpicProcessor extends BaseHttpProcessor {
    public EpicProcessor(TaskManager tm, Gson gson) {
        super(tm, gson);
    }

    @Override
    public void setCommands(TaskManager tm, Gson gson) {
        handlers = Arrays.asList(
                new GetEpicTasksHandler("^/epicsGET$", tm, gson),
                new GetEpicTaskByIdHandler("^/epics/\\d+GET$", tm, gson),
                new GetEpicSubTasksHandler("^/epics/\\d+/subtasksGET$", tm, gson),
                new CreateEpicTaskHandler("^/epicsPOST$", tm, gson),
                new UpdateEpicTaskHandler("^/epics/\\d+POST$", tm, gson),
                new DeleteEpicTaskHandler("^/epics/\\d+DELETE$", tm, gson)
        );
    }
}
