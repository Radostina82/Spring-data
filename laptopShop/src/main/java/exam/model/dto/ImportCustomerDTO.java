package exam.model.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class ImportCustomerDTO implements Serializable {
    @Size(min = 2)
    private String firstName;
    @Size(min = 2)
    private String lastName;
    @Email
    private String email;
    private String registeredOn;

    private NameDTO town;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getRegisteredOn() {
        return registeredOn;
    }

    public NameDTO getTown() {
        return town;
    }

    public String getEmail() {
        return email;
    }
}
