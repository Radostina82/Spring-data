package softuni.exam.domain.entities.dto.importxml;

import softuni.exam.domain.entities.dto.importxml.ImportPictureDTO;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ImportTeamDTO {
    @XmlElement
    @Size(min = 3, max = 20)
    private String name;
    @XmlElement(name = "picture")
    @NotNull
    private ImportPictureDTO url;

    public String getName() {
        return name;
    }

    public ImportPictureDTO getUrl() {
        return url;
    }
}
