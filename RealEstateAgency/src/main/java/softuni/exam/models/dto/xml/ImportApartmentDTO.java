package softuni.exam.models.dto.xml;

import softuni.exam.models.entity.ApartmentType;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ImportApartmentDTO {
    @XmlElement
    @NotNull
    private ApartmentType apartmentType;
    @XmlElement
    @Min(value = 40)
    private double area;
    @XmlElement(name = "town")
    @NotNull
    private String town;
    public ImportApartmentDTO(){}

    public ApartmentType getApartmentType() {
        return apartmentType;
    }

    public double getArea() {
        return area;
    }

    public String getTown() {
        return town;
    }
}
