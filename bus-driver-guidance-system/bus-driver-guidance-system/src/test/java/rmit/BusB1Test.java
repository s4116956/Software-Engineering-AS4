package rmit;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class BusB1Test {

    // B1 - Normal case: a valid 8-digit ID is accepted
    @Test
    void testValidBusID_AllDigits() {
        Bus bus = new Bus("12345678", 40, 80.0, "Diesel");
        assertEquals("12345678", bus.getBusID(),
            "A bus ID with exactly 8 digits should be accepted");
    }

    // B1 - Invalid input: ID containing a letter is rejected
    @Test
    void testInvalidBusID_ContainsLetter() {
        assertThrows(IllegalArgumentException.class,
            () -> new Bus("12345SHD", 40, 80.0, "Diesel"),
            "A bus ID containing a non-digit character should be rejected");
    }

    // B1 - Edge case: duplicate IDs are rejected by the repository
    @Test
    void testDuplicateBusID_Rejected() {
        BusRepository repo = new BusRepository();
        Bus first = new Bus("12345678", 40, 80.0, "Diesel");
        Bus duplicate = new Bus("12345678", 30, 50.0, "Hybrid");

        assertTrue(repo.add(first), "First bus should be added");
        assertFalse(repo.add(duplicate), "Duplicate bus ID should be rejected");
        assertEquals(1, repo.count(), "Repository should contain only one bus");
    }
}

