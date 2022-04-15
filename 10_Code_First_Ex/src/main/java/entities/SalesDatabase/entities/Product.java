package entities.SalesDatabase.entities;

import javax.persistence.*;
import java.math.BigDecimal;

import java.util.Collections;
import java.util.Set;
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "quantity", nullable = false)
    private Double quantity;
    @Column(name = "price", nullable = false)
    private BigDecimal price;
    @OneToMany(mappedBy = "product", targetEntity = Sale.class)
    private Set<Sale> sales;

    public Product(){}

    public Product(String name, Double quantity, BigDecimal price) {
        this.name = name;
        this.quantity = quantity;
        this.price = price;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Set<Sale> getSales() {
        return Collections.unmodifiableSet(this.sales);
    }

    public void setSales(Set<Sale> sales) {
        this.sales = sales;
    }
}
