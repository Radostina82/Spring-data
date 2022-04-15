package hiberspring.domain.entities;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "branches")
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String name;
    @ManyToOne
    private Town town;
    @OneToMany(mappedBy = "branch", targetEntity = Product.class)
    private List<Product> products;

    public Branch(){}

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

    public Town getTown() {
        return town;
    }

    public void setTown(Town town) {
        this.town = town;
    }
}
