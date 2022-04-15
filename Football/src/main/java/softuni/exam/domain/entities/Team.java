package softuni.exam.domain.entities;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "teams")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private String name;
    @ManyToOne
    private Picture picture;
    @OneToMany(mappedBy = "team", targetEntity = Player.class)
    private List<Player> players;

    public Team(){}

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(this.players);
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
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

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }
}
