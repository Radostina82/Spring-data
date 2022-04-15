package softuni.exam.instagraphlite.models.dto;

import softuni.exam.instagraphlite.models.Picture;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ImportUserDTO {
    @Size(min = 2, max = 18)
    @NotNull
    private String username;
    @Size(min = 4)
    private String password;
    @NotNull
    private String profilePicture;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getProfilePicture() {
        return profilePicture;
    }
}
