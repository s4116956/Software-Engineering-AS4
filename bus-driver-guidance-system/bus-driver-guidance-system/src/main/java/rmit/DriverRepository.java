package rmit;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class DriverRepository {

    private final String filePath;
    private static final String DELIMITER = "|||";

    public DriverRepository() {
        this("data/drivers.txt");
    }

    public DriverRepository(String filePath) {
        this.filePath = filePath;
        try {
            Path path = Paths.get(filePath);
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot create data directory");
        }
    }

    // Add driver 
    public boolean add(Driver driver) {
        if (driver == null || !isValidDriver(driver)) return false;
        try {
            List<Driver> drivers = loadAll();
            if (findById(drivers, driver.getDriverID()) != null) return false;
            drivers.add(driver);
            saveAll(drivers);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // Retrieve driver by ID
    public Driver retrieve(String driverID) {
        try {
            return findById(loadAll(), driverID);
        } catch (IOException e) {
            return null;
        }
    }

    // Update driver 
    public boolean update(Driver updatedDriver) {
        if (updatedDriver == null) return false;
        try {
            List<Driver> drivers = loadAll();
            Driver existing = findById(drivers, updatedDriver.getDriverID());
            if (existing == null) return false;
            if (!isValidUpdate(existing, updatedDriver)) return false;

            int index = drivers.indexOf(existing);
            drivers.set(index, updatedDriver);
            saveAll(drivers);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // Return number of drivers
    public int count() {
        try {
            return loadAll().size();
        } catch (IOException e) {
            return 0;
        }
    }

    private boolean isValidDriver(Driver d) {
        return isValidDriverID(d.getDriverID()) &&
               d.getName() != null && !d.getName().trim().isEmpty() &&
               d.getExperienceYears() >= 0 &&
               isValidLicense(d.getLicenseType()) &&
               isValidAddress(d.getAddress()) &&
               isValidBirthdate(d.getBirthdate());
    }

    private boolean isValidUpdate(Driver existing, Driver updated) {
        if (!existing.getDriverID().equals(updated.getDriverID())) return false;
        if (!existing.getName().equals(updated.getName())) return false;
        if (existing.getExperienceYears() > 10 && 
            !existing.getLicenseType().equals(updated.getLicenseType())) return false;
        return isValidDriver(updated);
    }

    private boolean isValidDriverID(String id) {
        if (id == null || id.length() != 10) return false;
        if (!Character.isDigit(id.charAt(0)) || id.charAt(0) < '2' || id.charAt(0) > '9') return false;
        if (!Character.isDigit(id.charAt(1)) || id.charAt(1) < '2' || id.charAt(1) > '9') return false;
        if (!Character.isUpperCase(id.charAt(8)) || !Character.isUpperCase(id.charAt(9))) return false;
        int special = 0;
        for (int i = 2; i <= 7; i++) {
            if (!Character.isLetterOrDigit(id.charAt(i))) special++;
        }
        return special >= 2;
    }

    private boolean isValidLicense(String license) {
        return "Light".equals(license) || "Medium".equals(license) ||
               "Heavy".equals(license) || "PublicTransport".equals(license);
    }

    private boolean isValidAddress(String address) {
        if (address == null) return false;
        return address.split("\\|", -1).length == 5;
    }

    private boolean isValidBirthdate(String bd) {
        return bd != null && bd.matches("^(0[1-9]|[12][0-9]|3[01])-(0[1-9]|1[0-2])-\\d{4}$");
    }

    private List<Driver> loadAll() throws IOException {
        List<Driver> list = new ArrayList<>();
        Path path = Paths.get(filePath);
        if (!Files.exists(path)) return list;

        for (String line : Files.readAllLines(path)) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split("\\|\\|\\|", -1);
            if (parts.length == 6) {
                try {
                    Driver d = new Driver(parts[0], parts[1], Integer.parseInt(parts[2]),
                                          parts[3], parts[4], parts[5]);
                    list.add(d);
                } catch (Exception ignored) {}
            }
        }
        return list;
    }

    private void saveAll(List<Driver> drivers) throws IOException {
        Path path = Paths.get(filePath);
        List<String> lines = new ArrayList<>();
        for (Driver d : drivers) {
            lines.add(String.join(DELIMITER, d.getDriverID(), d.getName(),
                    String.valueOf(d.getExperienceYears()), d.getLicenseType(),
                    d.getAddress(), d.getBirthdate()));
        }
        Files.write(path, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private Driver findById(List<Driver> list, String id) {
        for (Driver d : list) {
            if (d.getDriverID().equals(id)) return d;
        }
        return null;
    }
}