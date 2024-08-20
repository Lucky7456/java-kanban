package config;

import service.FileBackedTaskManager;
import service.InMemoryHistoryManager;
import service.InMemoryTaskManager;
import service.interfaces.HistoryManager;
import service.interfaces.TaskManager;

import java.io.File;

public class Managers {
    public static TaskManager getDefault() {
        return new InMemoryTaskManager(getDefaultHistory());
    }

    public static TaskManager getFileBackedTaskManager(File file) {
        return new FileBackedTaskManager(getDefaultHistory(), file);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
