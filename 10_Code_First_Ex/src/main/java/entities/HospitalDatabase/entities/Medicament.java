package entities.HospitalDatabase.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "medicaments")
public class Medicament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String name;
    @ManyToMany(mappedBy = "medicaments", targetEntity = Diagnose.class)
    private Set<Diagnose> diagnoses;

    public Medicament() {
    }

    public Medicament(String name) {
        this.name = name;
        this.diagnoses = new HashSet<>();
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

    public Set<Diagnose> getDiagnoses() {
        return diagnoses;
    }

    public void setDiagnoses(Set<Diagnose> diagnoses) {
        this.diagnoses = diagnoses;
    }
}
