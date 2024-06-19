package util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ManagersTest {

    @Test
    void getDefaultManagerShouldBeNotNull() {
        Assertions.assertNotNull(Managers.getDefault(), "getting default manager should not return null");
    }

    @Test
    void getDefaultHistoryShouldBeNotNull() {
        Assertions.assertNotNull(Managers.getDefaultHistory(), "getting default history should not return null");
    }
}