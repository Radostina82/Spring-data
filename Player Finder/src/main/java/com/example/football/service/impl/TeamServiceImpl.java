package com.example.football.service.impl;

import com.example.football.models.dto.TeamImportDTO;
import com.example.football.models.entity.Team;
import com.example.football.models.entity.Town;
import com.example.football.repository.TeamRepository;
import com.example.football.repository.TownRepository;
import com.example.football.service.TeamService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;
    private final TownRepository townRepository;
    private final Validator validator;
    private final ModelMapper modelMapper;
    private final Gson gson;
    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository, TownRepository townRepository) {
        this.teamRepository = teamRepository;
        this.townRepository = townRepository;
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.modelMapper = new ModelMapper();
        this.gson = new GsonBuilder().create();
    }

    @Override
    public boolean areImported() {
        return this.teamRepository.count()>0;
    }

    @Override
    public String readTeamsFileContent() throws IOException {
        Path path  = Path.of("src", "main", "resources", "files", "json", "teams.json");

        return Files.readString(path);
    }

    @Override
    public String importTeams() throws IOException {
        String json = this.readTeamsFileContent();
        TeamImportDTO [] teamImportDTOS = this.gson.fromJson(json, TeamImportDTO[].class);

        List<String> result = new ArrayList<>();

        for (TeamImportDTO teamImportDTO : teamImportDTOS) {
            Set<ConstraintViolation<TeamImportDTO>> validateErrors = this.validator.validate(teamImportDTO);

            if (validateErrors.isEmpty()){
                Optional<Team> teamName = this.teamRepository.findByName(teamImportDTO.getName());

                if (teamName.isEmpty()){
                    Team team = this.modelMapper.map(teamImportDTO, Team.class);
                    Optional<Town> town = this.townRepository.findByName(teamImportDTO.getTownName());
                    team.setTown(town.get());
                    this.teamRepository.save(team);
                    result.add(String.format("Successfully imported Team %s", team.getName()));
                }else {
                    result.add("Invalid team");
                }
            }else {
                result.add("Invalid team");
            }
        }
        return String.join("\n", result);
    }
}
