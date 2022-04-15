package json_ex.cardealer.entities.suppliers;

import javax.persistence.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "suppliers")
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    @Column(name = "is_importer")
    private boolean isImporter;

   // private Set<Part> parts;

    public Supplier(){
        //this.parts = new HashSet<>();
    }

    public Supplier(String name) {

        this.name = name;

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

    public boolean isImporter() {
        return isImporter;
    }

    public void setImporter(boolean importer) {
        isImporter = importer;
    }

   /*  public Set<Part> getParts() {
        return Collections.unmodifiableSet(this.parts);
    }

    public void setParts(Set<Part> parts) {
        this.parts = parts;
    }*/
}
