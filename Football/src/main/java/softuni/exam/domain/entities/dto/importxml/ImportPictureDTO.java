package softuni.exam.domain.entities.dto.importxml;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ImportPictureDTO {
    @XmlElement
    @NotNull
    private String url;

    public String getUrl() {
        return url;
    }
}
