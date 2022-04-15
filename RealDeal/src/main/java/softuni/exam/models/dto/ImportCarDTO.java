package softuni.exam.models.dto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

public class ImportCarDTO {
    @Size(min = 3, max = 19)
    private String make;
    @Size(min = 3, max = 19)
    private String model;
    @Positive
    private int kilometers;

    private String registeredOn;

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public int getKilometers() {
        return kilometers;
    }

    public String getRegisteredOn() {
        return registeredOn;
    }
}
