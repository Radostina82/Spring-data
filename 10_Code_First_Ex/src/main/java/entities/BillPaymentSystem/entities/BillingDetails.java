package entities.BillPaymentSystem.entities;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public abstract class BillingDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private int id;
    @Column(nullable = false)
    private int number;
    @Column(nullable = false)
    private String owner;
    @OneToOne(mappedBy = "billingDetails", targetEntity = User.class)
    private User user;

    public BillingDetails(){}

    public BillingDetails(int number, String owner) {
        this.number = number;
        this.owner = owner;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }


}
