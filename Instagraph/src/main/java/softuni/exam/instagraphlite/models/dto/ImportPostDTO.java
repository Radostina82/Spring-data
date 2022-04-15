package softuni.exam.instagraphlite.models.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ImportPostDTO {
    @XmlElement
    @NotNull
    @Size(min = 21)
    private String caption;
    @XmlElement
    @NotNull
    private UserDTO user;
    @XmlElement
    @NotNull
    private PictureDTO picture;

    public String getCaption() {
        return caption;
    }

    public UserDTO getUser() {
        return user;
    }

    public PictureDTO getPicture() {
        return picture;
    }
}
