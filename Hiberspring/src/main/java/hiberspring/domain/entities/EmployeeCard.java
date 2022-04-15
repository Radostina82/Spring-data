package hiberspring.domain.entities;

import javax.persistence.*;

@Entity
@Table(name = "employee_cards")
public class EmployeeCard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, unique = true)
    private String number;

    public EmployeeCard(){}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
