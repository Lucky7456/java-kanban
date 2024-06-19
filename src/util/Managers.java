package util;

import service.InMemoryHistoryManager;
import service.InMemoryTaskManager;
import service.interfaces.HistoryManager;
import service.interfaces.TaskManager;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
