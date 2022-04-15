package json_ex.cardealer.entities;

import json_ex.cardealer.entities.cars.Car;
import json_ex.cardealer.entities.customers.Customer;

import javax.persistence.*;

@Entity
@Table(name = "sales")
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @OneToOne
    private Car car;
    @OneToOne
    private Customer customer;
    @Column(name = "discount_percentage")
    private float discountPercentage;

    public Sale(){}

    public Sale(Car car, Customer customer, float discountPercentage) {
        this.car = car;
        this.customer = customer;
        this.discountPercentage = discountPercentage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public float getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(float discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
}
