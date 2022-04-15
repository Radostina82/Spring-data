package softuni.exam.domain.entities.dto;

import softuni.exam.domain.entities.dto.importxml.ImportPictureDTO;

import javax.validation.constraints.*;
import java.math.BigDecimal;

public class ImportPlayerDTO {
    @NotNull
    private String firstName;
    @Size(min = 3, max = 15)
    private String lastName;
    @Min(value = 1)
    @Max(value = 99)
    private int number;
    @Positive
    @NotNull
    private BigDecimal salary;
    @Size(min = 2, max = 2)
    private String position;
    @NotNull
    private ImportPictureJsonDTO picture;
    @NotNull
    private ImportTeamJsonDTO team;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getNumber() {
        return number;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public String getPosition() {
        return position;
    }

    public ImportPictureJsonDTO getPicture() {
        return picture;
    }

    public ImportTeamJsonDTO getTeam() {
        return team;
    }
}
