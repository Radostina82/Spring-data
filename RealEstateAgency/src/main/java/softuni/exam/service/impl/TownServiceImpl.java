package softuni.exam.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ImportTownDTO;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.TownService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TownServiceImpl implements TownService {
    private final TownRepository townRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final Validator validator;

    @Autowired
    public TownServiceImpl(TownRepository townRepository) {
        this.townRepository = townRepository;
        this.modelMapper = new ModelMapper();
        this.gson = new GsonBuilder().create();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Override
    public boolean areImported() {
        return this.townRepository.count()>0;
    }

    @Override
    public String readTownsFileContent() throws IOException {
        Path path = Path.of("src", "main", "resources", "files", "json", "towns.json");
        return Files.readString(path);
    }

    @Override
    public String importTowns() throws IOException {
        String json = this.readTownsFileContent();
        ImportTownDTO[] importTownDTOS = this.gson.fromJson(json, ImportTownDTO[].class);

        return Arrays.stream(importTownDTOS).map(this::importTown)
                .collect(Collectors.joining("\n"));
    }

    private String importTown(ImportTownDTO townDto) {
        Set<ConstraintViolation<ImportTownDTO>> validateErrors = this.validator.validate(townDto);
        if (!validateErrors.isEmpty()){
            return "Invalid town";
        }

        Optional<Town> town = this.townRepository.findByTownName(townDto.getTownName());
        if (town.isPresent()){
            return "Invalid town";
        }

        Town townToAdd = this.modelMapper.map(townDto, Town.class);
        this.townRepository.save(townToAdd);

        return String.format("Successfully imported town %s %d", townToAdd.getTownName(), townToAdd.getPopulation());
    }
}
