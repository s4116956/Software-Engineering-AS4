package rmit;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BusB2Test {

    // B2 - Normal case: decreasing capacity is allowed
    @Test
    void testDecreaseCapacity_Allowed() {
        Bus bus = new Bus("12345678", 50, 80.0, "Diesel");
        boolean result = bus.updateCapacity(30);

        assertTrue(result, "Decreasing capacity should be allowed");
        assertEquals(30, bus.getCapacity(),
            "Bus capacity should be updated to the new lower value");
    }

    // B2 - Invalid input: increasing capacity is rejected
    @Test
    void testIncreaseCapacity_Rejected() {
        Bus bus = new Bus("12345678", 30, 80.0, "Diesel");
        boolean result = bus.updateCapacity(50);

        assertFalse(result, "Increasing capacity should be rejected");
        assertEquals(30, bus.getCapacity(),
            "Bus capacity should remain unchanged when increase is attempted");
    }

    // B2 - Edge case: setting capacity to the same value
    @Test
    void testSameCapacity_Allowed() {
        Bus bus = new Bus("12345678", 40, 80.0, "Diesel");
        boolean result = bus.updateCapacity(40);

        assertTrue(result, "Setting capacity to the same value should be allowed (no increase)");
        assertEquals(40, bus.getCapacity(),
            "Bus capacity should remain at the same value");
    }
}