package json_ex.productshop.entities.users;

import json_ex.productshop.entities.products.Product;

import javax.persistence.*;
import java.util.*;


@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;

    private Integer age;
    @OneToMany(mappedBy = "seller", targetEntity = Product.class)
    private List<Product> sellingItems;
    @OneToMany(mappedBy = "buyer", targetEntity = Product.class)
    private List<Product> itemsBought;
    @ManyToMany
    private Set<User> friends;

    public User(){
        this.sellingItems = new ArrayList<>();
        this.itemsBought = new ArrayList<>();
        this.friends = new HashSet<>();
    }

    public User(String firstName, String lastName, Integer age) {
        this();

        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public List<Product> getSellingItems() {
        return Collections.unmodifiableList(this.sellingItems);
    }

    public void setSellingItems(List<Product> sellingItems) {
        this.sellingItems = sellingItems;
    }

    public List<Product> getItemsBought() {
        return Collections.unmodifiableList(this.itemsBought);
    }

    public void setItemsBought(List<Product> itemsBought) {
        this.itemsBought = itemsBought;
    }

    public Set<User> getFriends() {
        return Collections.unmodifiableSet(this.friends);
    }

    public void setFriends(Set<User> friends) {
        this.friends = friends;
    }
}
