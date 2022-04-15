package com.example.football.service.impl;

import com.example.football.models.dto.TownImportDTO;
import com.example.football.models.entity.Town;
import com.example.football.repository.TownRepository;
import com.example.football.service.TownService;
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
public class TownServiceImpl implements TownService {

    private final TownRepository townRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final Validator validator;


    @Autowired
    public TownServiceImpl(TownRepository townRepository) {
        this.townRepository = townRepository;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.modelMapper = new ModelMapper();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Override
    public boolean areImported() {
        return this.townRepository.count() > 0;
    }

    @Override
    public String readTownsFileContent() throws IOException {
        Path path = Path.of("src", "main", "resources", "files", "json", "towns.json");


        return Files.readString(path);
    }

    @Override
    public String importTowns() throws IOException {
        String json = this.readTownsFileContent();

        TownImportDTO[] townImportDTO = this.gson.fromJson(json, TownImportDTO[].class);

        List<String> result = new ArrayList<>();

        for (TownImportDTO importDTO : townImportDTO) {
            Set<ConstraintViolation<TownImportDTO>> validateErrors = this.validator.validate(importDTO);

            if (validateErrors.isEmpty()) {
                Optional<Town> townName = this.townRepository.findByName(importDTO.getName());
                if (townName.isEmpty()) {
                    Town town = this.modelMapper.map(importDTO, Town.class);
                    this.townRepository.save(town);
                    result.add(String.format("Successfully imported Town %s - %d", town.getName(), town.getPopulation()));
                } else {
                    result.add("Invalid Town");
                }
            }else {
                result.add("Invalid Town");
            }
        }

        return String.join("\n", result);
    }
}
