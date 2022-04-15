package softuni.exam.domain.entities.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ImportTeamJsonDTO {
    @Size(min = 3, max = 20)
    private String name;
    @NotNull
    private ImportPictureJsonDTO picture;

    public String getName() {
        return name;
    }

    public ImportPictureJsonDTO getPicture() {
        return picture;
    }
}
