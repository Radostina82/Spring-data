package json_ex.cardealer.entities.customers;

import java.time.LocalDate;

public class CustomerImportDTO {
    private String name;
    private String birthDate;
    private boolean isYoungDriver;

    public String getName() {
        return name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public boolean isYoungDriver() {
        return isYoungDriver;
    }
}
