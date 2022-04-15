package com.example.football.models.dto;

import com.example.football.models.entity.Player;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "players")
@XmlAccessorType(XmlAccessType.FIELD)
public class PlayerImportRootDTO {
    @XmlElement(name = "player")
    List<PlayerImportDTO> players;

    public PlayerImportRootDTO(){}

    public List<PlayerImportDTO> getPlayers() {
        return players;
    }
}
