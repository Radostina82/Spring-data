package hiberspring.domain.dtos;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;

@XmlRootElement(name = "employee")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportEmployeeDTO {
    @XmlAttribute(name = "first-name")
    @NotNull
    private String firstName;
    @XmlAttribute(name = "last-name")
    @NotNull
    private String lastName;
    @XmlAttribute
    @NotNull
    private String position;
    @XmlElement
    @NotNull
    private String card;
    @XmlElement
    @NotNull
    private String branch;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPosition() {
        return position;
    }

    public String getCard() {
        return card;
    }

    public String getBranch() {
        return branch;
    }
}
