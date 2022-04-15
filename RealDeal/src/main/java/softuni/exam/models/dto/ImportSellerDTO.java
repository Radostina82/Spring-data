package softuni.exam.models.dto;

import org.springframework.stereotype.Service;
import softuni.exam.models.RatingSeller;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ImportSellerDTO {
    @XmlElement(name = "first-name")
    @Size(min = 3, max = 19)
    private String firstName;

    @XmlElement(name = "last-name")
    @Size(min = 3, max = 19)
    private String lastName;
    @XmlElement
    @Email
    private String email;
    @XmlElement
    @NotNull
    private RatingSeller rating;
    @XmlElement
    @NotNull
    private String town;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public RatingSeller getRating() {
        return rating;
    }

    public String getTown() {
        return town;
    }
}
