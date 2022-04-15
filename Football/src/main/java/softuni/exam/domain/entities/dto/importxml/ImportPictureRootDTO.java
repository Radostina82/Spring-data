package softuni.exam.domain.entities.dto.importxml;

import softuni.exam.domain.entities.dto.importxml.ImportPictureDTO;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "pictures")
@XmlAccessorType(XmlAccessType.FIELD)
public class ImportPictureRootDTO {
    @XmlElement(name = "picture")
    List<ImportPictureDTO> pictures;

    public List<ImportPictureDTO> getPictures() {
        return pictures;
    }
}
