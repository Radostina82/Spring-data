package gamestore.entities;

import gamestore.entities.game.Game;
import gamestore.entities.user.User;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private User buyer;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Game> products;

    public Order(){
        this.products = new HashSet<>();
    }

    public Order(User buyer, Set<Game> products) {
        this.buyer = buyer;
        this.products = products;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getBuyer() {
        return buyer;
    }

    public void setBuyer(User buyer) {
        this.buyer = buyer;
    }

    public Set<Game> getProducts() {
        return Collections.unmodifiableSet(this.products);
    }

    public void setProducts(Set<Game> products) {
        this.products = products;
    }
}

