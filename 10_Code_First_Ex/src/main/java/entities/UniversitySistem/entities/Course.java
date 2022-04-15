package entities.UniversitySistem.entities;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;


@Entity
@Table(name = "courses")
public class Course {
    //(id, name, description, start date, end date, credits
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description")
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
    @Column(name = "credits")
    private int credits;
    @ManyToMany(mappedBy = "courses", targetEntity = Student.class)
    private Set<Student> students;
    @ManyToOne(optional = false)
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    private Teacher teacher;

    public Course() {
    }

    public Course(String name, String description, LocalDate startDate, LocalDate endDate, int credits) {
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.credits = credits;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }
}
