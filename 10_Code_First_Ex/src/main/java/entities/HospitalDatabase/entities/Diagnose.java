package entities.HospitalDatabase.entities;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "diagnoses")
public class Diagnose {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String comment;
    @OneToOne(mappedBy = "diagnose", targetEntity = Patients.class)
    private Patients patient;
    @ManyToMany
    @JoinTable(name = "diagnoses_medicaments", joinColumns = @JoinColumn(name = "diagnoses_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "mediacments_id", referencedColumnName = "id"))
    private Set<Medicament> medicaments;

    public Diagnose() {
    }

    public Diagnose(String name, String comment) {
        this.name = name;
        this.comment = comment;
        this.medicaments = new HashSet<>();
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Set<Medicament> getMedicaments() {
        return medicaments;
    }

    public void setMedicaments(Set<Medicament> medicaments) {
        this.medicaments = medicaments;
    }
}
