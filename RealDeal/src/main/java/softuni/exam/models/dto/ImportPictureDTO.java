package softuni.exam.models.dto;

import softuni.exam.models.Car;

import javax.validation.constraints.Size;

public class ImportPictureDTO {
@Size(min = 3, max = 19)
    private String name;

    private String dateAndTime;

    private long car;

    public String getName() {
        return name;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public long getCar() {
        return car;
    }
}
