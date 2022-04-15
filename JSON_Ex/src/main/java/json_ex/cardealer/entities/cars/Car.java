package json_ex.cardealer.entities.cars;

import json_ex.cardealer.entities.Sale;
import json_ex.cardealer.entities.parts.Part;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "cars")
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String make;
    @Column(nullable = false)
    private String model;
    @Column(name = "travelled_distance", nullable = false)
    private float travelledDistance;
    @ManyToMany
    private Set<Part> parts;


    public Car(){}

    public Car(String make, String model, float travelledDistance) {
        this.make = make;
        this.model = model;
        this.travelledDistance = travelledDistance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public float getTravelledDistance() {
        return travelledDistance;
    }

    public void setTravelledDistance(float travelledDistance) {
        this.travelledDistance = travelledDistance;
    }

    public Set<Part> getParts() {
        return parts;
    }

    public void setParts(Set<Part> parts) {
        this.parts = parts;
    }


}
