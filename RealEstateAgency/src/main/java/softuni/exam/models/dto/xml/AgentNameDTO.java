package softuni.exam.models.dto.xml;

import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class AgentNameDTO {
    @XmlElement
    @Size(min = 2)
    private String name;

    public String getName() {
        return name;
    }
}
