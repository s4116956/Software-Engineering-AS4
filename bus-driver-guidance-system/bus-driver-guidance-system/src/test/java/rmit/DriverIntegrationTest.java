package rmit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DriverIntegrationTest {

    private DriverRepository repo;
    private Path driverFilePath;

    @BeforeEach
    void setUp() {
    driverFilePath = Path.of("drivers.txt").toAbsolutePath();
    System.out.println("drivers.txt will be created here: " + driverFilePath);
    repo = new DriverRepository(driverFilePath.toString());
}

    private Driver createValidDriver(String idSuffix, String name, int exp) {
        return new Driver("45AB@#CD" + idSuffix, name, exp,
                "Heavy", "123|Main Street|Melbourne|Victoria|Australia", "15-05-1990");
    }

    // Test case 1: valid drivers are stored correctly
    @Test
    void testValidDriverIsStoredCorrectly() throws IOException {
        Driver d = createValidDriver("EF", "John Smith", 6);

        assertTrue(repo.add(d));
        assertNotNull(repo.retrieve("45AB@#CDEF"));

        String content = Files.readString(driverFilePath);
        assertTrue(content.contains("45AB@#CDEF"));
        assertTrue(content.contains("John Smith"));
    }

    // Test case 2: invalid drivers are rejected
    @Test
    void testInvalidDriverIsRejected() throws IOException {
        Driver invalid = new Driver("15AB@CDEFG", "Bad ID Driver", 3,
                "Light", "100|Fake Road|Sydney|NSW|Australia", "05-07-1998");

        assertFalse(repo.add(invalid));
        assertNull(repo.retrieve("15AB@CDEFG"));
    }

    // Test case 3: updates are persisted correctly
    @Test
    void testUpdateIsPersistedCorrectly() throws IOException {
        Driver original = createValidDriver("GH", "Sarah Lee", 7);
        repo.add(original);

        Driver updated = new Driver("45AB@#CDGH", "Sarah Lee", 10,
                "PublicTransport", "55|New Road|Perth|WA|Australia", "15-09-1985");

        assertTrue(repo.update(updated));

        String content = Files.readString(driverFilePath);
        assertTrue(content.contains("55|New Road|Perth|WA|Australia"));
        assertTrue(content.contains("15-09-1985"));

        Driver retrieved = repo.retrieve("45AB@#CDGH");
        assertEquals(10, retrieved.getExperienceYears());
    }

    // Test case 4: record counts are updated correctly
    @Test
    void testRecordCountIsUpdatedCorrectly() {
        Driver d1 = createValidDriver("MN", "Driver One", 3);
        Driver d2 = createValidDriver("OP", "Driver Two", 4);

        repo.add(d1);
        assertEquals(1, repo.count());

        repo.add(d2);
        assertEquals(2, repo.count());

        Driver update = createValidDriver("MN", "Driver One", 5);
        repo.update(update);
        assertEquals(2, repo.count());
    }
}