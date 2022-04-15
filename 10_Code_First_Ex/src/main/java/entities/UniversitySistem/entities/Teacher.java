package entities.UniversitySistem.entities;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "teachers")
public class Teacher extends Person{
    @Column(name = "email", nullable = false, updatable = true)
    private String email;
    @Column(name = "salary_per_hour", nullable = false)
    private float salaryPerHour;
    @OneToMany(mappedBy = "teacher", targetEntity = Course.class)
    private Set<Course> courses;

    public Teacher(){
        super();
    }
    public Teacher(String firstName, String lastName, String phoneNumber, String email, float salaryPerHour) {
        super(firstName, lastName, phoneNumber);

        this.email = email;
        this.salaryPerHour = salaryPerHour;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public float getSalaryPerHour() {
        return salaryPerHour;
    }

    public void setSalaryPerHour(float salaryPerHour) {
        this.salaryPerHour = salaryPerHour;
    }
}
