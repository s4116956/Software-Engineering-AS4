package rmit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class BusB3Test {

    // B3 - Normal case: young driver on a large bus is allowed
    @Test
    void testYoungDriver_LargeBus_Allowed() {
        Driver driver = new Driver("13!@45720L", "Johnathan", 11,
            "Heavy", "1|Main St|Melbourne|VIC|Australia", "28-02-1990");
        Bus bus = new Bus("12345678", 60, 80.0, "Diesel");

        assertTrue(bus.canBeDrivenBy(driver),
            "A driver aged 50 or under should be allowed on a large bus");
    }

    // B3 - Invalid input: old driver on a large bus is rejected
    @Test
    void testOldDriver_LargeBus_Rejected() {
        Driver driver = new Driver("S289LS@234", "Andrew", 25,
            "Heavy", "1|Main St|Melbourne|VIC|Australia", "01-01-1970");
        Bus bus = new Bus("12345678", 60, 80.0, "Diesel");

        assertFalse(bus.canBeDrivenBy(driver),
            "A driver older than 50 should not be allowed on a bus with capacity >= 50");
    }

    // B3 - Edge case: driver exactly 50 years old on bus with exactly capacity 50
    @Test
    void testDriverExactly50_BusCapacity50_Allowed() {
        // Calculate birthdate so the driver is exactly 50 today
        int year = java.time.LocalDate.now().getYear() - 50;
        String birthdate = "01-01-" + year;

        Driver driver = new Driver("411@S6956!", "Brandon", 20,
            "Heavy", "1|Main St|Melbourne|VIC|Australia", birthdate);
        Bus bus = new Bus("12345678", 50, 80.0, "Diesel");

        assertTrue(bus.canBeDrivenBy(driver),
            "A driver who is exactly 50 should still be allowed (rule says 'older than 50')");
    }
}