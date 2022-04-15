package softuni.exam.models;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    private String make;
    @Column(nullable = false)
    private String model;

    private int kilometers;
    @Column(name = "registered_on", nullable = false)
    private LocalDate registeredOn;
    @OneToMany(mappedBy = "car", targetEntity = Picture.class)
    private List<Picture> pictures;
    public Car(){}

    public Car(String make, String model, int kilometers, LocalDate registeredOn) {
        this.make = make;
        this.model = model;
        this.kilometers = kilometers;
        this.registeredOn = registeredOn;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getKilometers() {
        return kilometers;
    }

    public void setKilometers(int kilometers) {
        this.kilometers = kilometers;
    }

    public LocalDate getRegisteredOn() {
        return registeredOn;
    }

    public void setRegisteredOn(LocalDate registeredOn) {
        this.registeredOn = registeredOn;
    }

    public List<Picture> getPictures() {
        return pictures;
    }

    public void setPictures(List<Picture> pictures) {
        this.pictures = pictures;
    }

    @Override
    public String toString() {
        return String.format("Car make - %s, model - %s%n" +
                "\tKilometers - %d%n" +
                "\tRegistered on - %s%n" +
                "\tNumber of pictures - %d%n", this.getMake(), this.getModel(),
                this.getKilometers(), this.getRegisteredOn(), this.getPictures().size());
    }
}
