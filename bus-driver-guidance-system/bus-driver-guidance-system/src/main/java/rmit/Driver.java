package rmit;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class Driver {
    private String driverID;
    private String name;
    private int experienceYears;
    private String licenseType;
    private String address;
    private String birthdate; // format: DD-MM-YYYY

    public Driver(String driverID, String name, int experienceYears,
                  String licenseType, String address, String birthdate) {
        this.driverID = driverID;
        this.name = name;
        this.experienceYears = experienceYears;
        this.licenseType = licenseType;
        this.address = address;
        this.birthdate = birthdate;
    }

    // Calculate age from birthdate string (DD-MM-YYYY)
    public int getAge() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate birth = LocalDate.parse(birthdate, formatter);
        return Period.between(birth, LocalDate.now()).getYears();
    }

    // Getters
    public String getDriverID() { return driverID; }
    public String getName() { return name; }
    public int getExperienceYears() { return experienceYears; }
    public String getLicenseType() { return licenseType; }
    public String getAddress() { return address; }
    public String getBirthdate() { return birthdate; }
}
