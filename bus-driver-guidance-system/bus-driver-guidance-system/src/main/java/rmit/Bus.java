package rmit;

public class Bus {
    private String busID;
    private int capacity;
    private double fuelLevel;
    private String fuelType;

    public Bus(String busID, int capacity, double fuelLevel, String fuelType) {
        if (!isValidBusID(busID)) {
            throw new IllegalArgumentException("Invalid bus ID: " + busID);
        }
        this.busID = busID;
        this.capacity = capacity;
        this.fuelLevel = fuelLevel;
        this.fuelType = fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public void setFuelLevel(double fuelLevel) {
        this.fuelLevel = fuelLevel;
    }

    // B1: must be exactly 8 characters, all digits
    public static boolean isValidBusID(String id) {
        return id != null && id.matches("\\d{8}");
    }

    // B2: capacity can only decrease, not increase
    public boolean updateCapacity(int newCapacity) {
        if (newCapacity > this.capacity) {
            return false;
        }
        this.capacity = newCapacity;
        return true;
    }

    public boolean canBeDrivenBy(Driver driver) {
        // B3: drivers older than 50 cannot drive buses with capacity >= 50
        if (driver.getAge() > 50 && this.capacity >= 50) {
            return false;
        }
        // B4: electric buses require at least 5 years of experience
        if (this.fuelType.equals("Electricity") && driver.getExperienceYears() < 5) {
            return false;
        }
        // B5: only Heavy or PublicTransport licences can drive electric or hybrid buses
        if (this.fuelType.equals("Electricity") || this.fuelType.equals("Hybrid")) {
            if (!driver.getLicenseType().equals("Heavy")
                && !driver.getLicenseType().equals("PublicTransport")) {
                return false;
            }
        }
        return true;
    }

    public String getBusID() { return busID; }
    public int getCapacity() { return capacity; }
    public double getFuelLevel() { return fuelLevel; }
    public String getFuelType() { return fuelType; }
}
