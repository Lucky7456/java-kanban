package server.interfaces;

import com.google.gson.Gson;
import config.Managers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import server.HttpTaskServer;
import service.interfaces.TaskManager;

import java.io.IOException;

public class BaseProcessorTest {
    protected TaskManager tm = Managers.getInmemoryTaskManager();
    protected HttpTaskServer taskServer = new HttpTaskServer(tm);
    protected Gson gson = Managers.getGson();

    public BaseProcessorTest() throws IOException {
    }

    @BeforeEach
    public void setUp() {
        tm.removeAllTasks();
        tm.removeAllEpicTasks();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }
}
