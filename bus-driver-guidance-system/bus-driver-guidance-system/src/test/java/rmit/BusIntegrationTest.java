package rmit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Task 4 - Bus Integration Testing.
 *
 * These tests exercise the real {@link BusRepository} and {@link Bus}
 * implementations against a real TXT file on disk. Each test starts from a
 * clean file and persistence is proven both by reading the file back and by
 * re-opening a fresh repository on the same file.
 */
public class BusIntegrationTest {

    private BusRepository repo;
    private Path busFilePath;

    @BeforeEach
    void setUp() throws IOException {
        busFilePath = Path.of("buses.txt").toAbsolutePath();
        Files.deleteIfExists(busFilePath); // start each test from a clean file
        System.out.println("buses.txt will be created here: " + busFilePath);
        repo = new BusRepository(busFilePath.toString());
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(busFilePath); // leave no residue between tests/runs
    }

    // Test case 1: valid buses are stored correctly
    @Test
    void testValidBusIsStoredCorrectly() throws IOException {
        Bus bus = new Bus("12345678", 40, 80.0, "Diesel");

        assertTrue(repo.add(bus), "A valid bus should be added");
        assertNotNull(repo.retrieve("12345678"), "The stored bus should be retrievable");

        // Proven on disk
        String content = Files.readString(busFilePath);
        assertTrue(content.contains("12345678"), "File should contain the bus ID");
        assertTrue(content.contains("Diesel"), "File should contain the fuel type");

        // Proven across a fresh repository reading the same real file
        BusRepository reopened = new BusRepository(busFilePath.toString());
        Bus loaded = reopened.retrieve("12345678");
        assertNotNull(loaded, "Bus should still be present after reloading from file");
        assertEquals(40, loaded.getCapacity(), "Capacity should be persisted");
        assertEquals("Diesel", loaded.getFuelType(), "Fuel type should be persisted");
    }

    // Test case 2: invalid buses are rejected
    @Test
    void testInvalidBusIsRejected() throws IOException {
        assertTrue(repo.add(new Bus("12345678", 40, 80.0, "Diesel")),
                "The first valid bus should be added");

        // Duplicate bus ID is rejected by the repository (B1 uniqueness)
        assertFalse(repo.add(new Bus("12345678", 30, 50.0, "Hybrid")),
                "A duplicate bus ID should be rejected");

        // A malformed bus ID is rejected at construction (B1 format) and so can
        // never reach the repository
        assertThrows(IllegalArgumentException.class,
                () -> new Bus("12345SHD", 40, 80.0, "Diesel"),
                "A bus ID containing a non-digit character should be rejected");

        // Only the first valid, unique bus is persisted
        assertEquals(1, repo.count(), "Only the valid, unique bus should be stored");
        assertEquals("Diesel", repo.retrieve("12345678").getFuelType(),
                "The originally stored bus should be unchanged");
    }

    // Test case 3: updates are persisted correctly
    @Test
    void testUpdateIsPersistedCorrectly() throws IOException {
        repo.add(new Bus("87654321", 50, 90.0, "Diesel"));

        // Legal update: decrease capacity 50 -> 30 (allowed by B2) and change fuel type
        assertTrue(repo.update(new Bus("87654321", 30, 90.0, "Hybrid")),
                "A legal update should succeed");

        String content = Files.readString(busFilePath);
        assertTrue(content.contains("Hybrid"), "The updated fuel type should be persisted");

        Bus retrieved = repo.retrieve("87654321");
        assertEquals(30, retrieved.getCapacity(), "The decreased capacity should be persisted");
        assertEquals("Hybrid", retrieved.getFuelType(), "The new fuel type should be persisted");

        // Illegal update: increasing capacity 30 -> 60 must be rejected (B2) and not persisted
        assertFalse(repo.update(new Bus("87654321", 60, 90.0, "Hybrid")),
                "Increasing capacity should be rejected (B2)");
        assertEquals(30, repo.retrieve("87654321").getCapacity(),
                "Capacity should remain 30 after a rejected update");
    }

    // Test case 4: record counts are updated correctly
    @Test
    void testRecordCountIsUpdatedCorrectly() {
        assertEquals(0, repo.count(), "A new repository should be empty");

        repo.add(new Bus("11111111", 40, 70.0, "Diesel"));
        assertEquals(1, repo.count(), "Count should be 1 after one add");

        repo.add(new Bus("22222222", 45, 60.0, "Hybrid"));
        assertEquals(2, repo.count(), "Count should be 2 after two adds");

        // A rejected duplicate must not change the count
        repo.add(new Bus("11111111", 20, 50.0, "Diesel"));
        assertEquals(2, repo.count(), "A rejected duplicate must not change the count");

        // Count reflects the persisted state via a fresh repository on the same file
        BusRepository reopened = new BusRepository(busFilePath.toString());
        assertEquals(2, reopened.count(), "Count should survive a reload from file");
    }
}
