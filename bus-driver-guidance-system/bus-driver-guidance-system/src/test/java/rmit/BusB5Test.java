package rmit;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

public class BusB5Test {

    // B5 - Normal case: Heavy license driver on a hybrid bus is allowed
    @Test
    void testHeavyLicense_HybridBus_Allowed() {
        Driver driver = new Driver("MS2298&%$", "Lisa", 8,
            "Heavy", "1|Main St|Melbourne|VIC|Australia", "16-07-1991");
        Bus bus = new Bus("12345678", 40, 80.0, "Hybrid");

        assertTrue(bus.canBeDrivenBy(driver),
            "A driver with a Heavy license should be allowed on a hybrid bus");
    }

    // B5 - Invalid input: Light license driver on an electric bus is rejected
    @Test
    void testLightLicense_ElectricBus_Rejected() {
        Driver driver = new Driver("KL227&22!", "Mr Driver", 12,
            "Light", "1|Main St|Melbourne|VIC|Australia", "20-01-1980");
        Bus bus = new Bus("12345678", 40, 80.0, "Electricity");

        assertFalse(bus.canBeDrivenBy(driver),
            "A driver with a Light license should not be allowed on an electric bus");
    }

    // B5 - Edge case: PublicTransport license driver on an electric bus is allowed
    @Test
    void testPublicTransportLicense_ElectricBus_Allowed() {
        Driver driver = new Driver("MKL2987$@3", "Ms Driver", 8,
            "PublicTransport", "1|Main St|Melbourne|VIC|Australia", "16-12-1999");
        Bus bus = new Bus("12345678", 40, 80.0, "Electricity");

        assertTrue(bus.canBeDrivenBy(driver),
            "A driver with a PublicTransport license should be allowed on an electric bus");
    }
}