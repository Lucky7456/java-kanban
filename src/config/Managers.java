package config;

import adapter.DurationAdapter;
import adapter.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import service.FileBackedTaskManager;
import service.InMemoryHistoryManager;
import service.InMemoryTaskManager;
import service.interfaces.HistoryManager;
import service.interfaces.TaskManager;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

public class Managers {
    public static TaskManager getDefault(File file) {
        return new FileBackedTaskManager(getDefaultHistory(), file);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getInmemoryTaskManager() {
        return new InMemoryTaskManager(getDefaultHistory());
    }

    public static Gson getGson() {
        return new GsonBuilder()
                .serializeNulls()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .create();
    }
}
