package hiberspring.domain.dtos;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.xml.bind.annotation.*;

@XmlRootElement(name = "product")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportProductDTO {
    @XmlAttribute
    @NotNull
    private String name;
    @XmlAttribute
    @NotNull
    @Positive
    private int clients;
    @XmlElement
    @NotNull
    private String branch;

    public String getName() {
        return name;
    }

    public int getClients() {
        return clients;
    }

    public String getBranch() {
        return branch;
    }
}
