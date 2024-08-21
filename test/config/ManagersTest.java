package config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

class ManagersTest {

    @Test
    void getDefaultManagerShouldBeNotNull() {
        try {
            Assertions.assertNotNull(Managers.getDefault(File.createTempFile("data",".txt")), "getting default manager should not return null");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getDefaultHistoryShouldBeNotNull() {
        Assertions.assertNotNull(Managers.getDefaultHistory(), "getting default history should not return null");
    }
}