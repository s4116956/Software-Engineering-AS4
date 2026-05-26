package rmit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class BusB4Test {

    // B4 - Normal case: experienced driver on an electric bus is allowed
    @Test
    void testExperiencedDriver_ElectricBus_Allowed() {
        Driver driver = new Driver("S129202@L3", "Maele", 8,
            "Heavy", "1|Melbourne|VIC|Australia", "19-04-1984");
        Bus bus = new Bus("12345678", 40, 80.0, "Electricity");

        assertTrue(bus.canBeDrivenBy(driver),
            "A driver with 5+ years experience should be allowed on an electric bus");
    }

    // B4 - Invalid input: inexperienced driver on an electric bus is rejected
    @Test
    void testInexperiencedDriver_ElectricBus_Rejected() {
        Driver driver = new Driver("SHM02&$22", "Jordan", 2,
            "Heavy", "1|Melbourne|VIC|Australia", "31-12-2000");
        Bus bus = new Bus("12345678", 40, 80.0, "Electricity");

        assertFalse(bus.canBeDrivenBy(driver),
            "A driver with fewer than 5 years experience should not be allowed on an electric bus");
    }

    // B4 - Edge case: driver with exactly 5 years experience on an electric bus
    @Test
    void testExactly5YearsExperience_ElectricBus_Allowed() {
        Driver driver = new Driver("LK277%!392", "Felix", 5,
            "Heavy", "1|Melbourne|VIC|Australia", "12-09-1999");
        Bus bus = new Bus("12345678", 40, 80.0, "Electricity");

        assertTrue(bus.canBeDrivenBy(driver),
            "A driver with exactly 5 years experience should be allowed (rule says 'at least 5')");
    }
}

