package softuni.exam.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.domain.entities.Picture;
import softuni.exam.domain.entities.Player;
import softuni.exam.domain.entities.Team;
import softuni.exam.domain.entities.dto.ImportPlayerDTO;
import softuni.exam.repository.PictureRepository;
import softuni.exam.repository.PlayerRepository;
import softuni.exam.repository.TeamRepository;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService {
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;
    private final PictureRepository pictureRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final Validator validator;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository, TeamRepository teamRepository, PictureRepository pictureRepository) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
        this.pictureRepository = pictureRepository;
        this.modelMapper = new ModelMapper();
        this.gson = new GsonBuilder().create();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Override
    public String importPlayers() throws IOException {
        String json = this.readPlayersJsonFile();
        ImportPlayerDTO[] importPlayerDTOS = this.gson.fromJson(json, ImportPlayerDTO[].class);

        return Arrays.stream(importPlayerDTOS).map(playerDto -> importPlayer(playerDto)).
                collect(Collectors.joining("\n"));
    }

    private String importPlayer(ImportPlayerDTO playerDto) {
        Set<ConstraintViolation<ImportPlayerDTO>> validateErrors = this.validator.validate(playerDto);
        if(!validateErrors.isEmpty()){
            return "Invalid player";
        }

        Optional<Team> team = this.teamRepository.findByName(playerDto.getTeam().getName());
        if (team.isEmpty()) {
            return "Invalid player";
        }
        Optional<Picture> pictureTeam = this.pictureRepository.findByUrl(playerDto.getTeam().getPicture().getUrl());
        if (pictureTeam.isEmpty()){
            return "Invalid player";
        }
        Optional<Picture> picture = this.pictureRepository.findByUrl(playerDto.getPicture().getUrl());
        if (picture.isEmpty()){
            return "Invalid player";
        }

        Player player = this.modelMapper.map(playerDto, Player.class);
        team.get().setPicture(pictureTeam.get());
        player.setTeam(team.get());
        player.setPicture(picture.get());
        this.playerRepository.save(player);

        return String.format("Successfully imported player: %s %s", player.getFirstName(), player.getLastName());
    }

    @Override
    public boolean areImported() {
        return this.playerRepository.count()>0;
    }

    @Override
    public String readPlayersJsonFile() throws IOException {
        Path path = Path.of("src", "main", "resources", "files", "json", "players.json");

        return Files.readString(path);
    }

    @Override
    public String exportPlayersWhereSalaryBiggerThan() {
        BigDecimal salary = new BigDecimal("100000");
        List<Player> players = this.playerRepository.findBySalaryGreaterThanOrderBySalaryDesc(salary);
        List<String> result = new ArrayList<>();

        for (Player player : players) {
            result.add(String.format("Player name: %s %s %n" +
                    "\tNumber: %d%n" +
                    "\tSalary: %.2f%n" +
                    "\tTeam: %s%n", player.getFirstName(), player.getLastName(), player.getNumber(),
                    player.getSalary(), player.getTeam().getName()));
        }
        return String.join("", result);
    }

    @Override
    public String exportPlayersInATeam() {
        String name = "North Hub";
        List<Player> players = this.playerRepository.findByTeamNameLikeOrderById(name);
        List<String> result = new ArrayList<>();
        result.add(name + "\n");
        for (Player player : players) {
                result.add(String.format("\tPlayer name: %s %s - %s%n" +
                        "\tNumber: %d%n", player.getFirstName(), player.getLastName(), player.getPosition(),
                        player.getNumber()));
        }

        return String.join("", result);
    }
}
