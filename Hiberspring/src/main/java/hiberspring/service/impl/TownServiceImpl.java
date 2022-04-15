package hiberspring.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import hiberspring.domain.dtos.ImportTownDTO;
import hiberspring.domain.entities.Town;
import hiberspring.repository.TownRepository;
import hiberspring.service.TownService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
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
    public Boolean townsAreImported() {
        return this.townRepository.count()>0;
    }

    @Override
    public String readTownsJsonFile() throws IOException {
        Path path = Path.of("src", "main", "resources", "files", "towns.json");
        return Files.readString(path);
    }

    @Override
    public String importTowns(String townsFileContent) throws IOException {
        String json = this.readTownsJsonFile();
        ImportTownDTO[] importTownDTOS = this.gson.fromJson(json, ImportTownDTO[].class);

        return Arrays.stream(importTownDTOS).map(townDto -> importTown(townDto))
                .collect(Collectors.joining("\n"));
    }

    private String importTown(ImportTownDTO townDto) {
        Set<ConstraintViolation<ImportTownDTO>> validateErrors = this.validator.validate(townDto);
        if(!validateErrors.isEmpty()){
            return "Error: Invalid data.";
        }

        int population = townDto.getPopulation();
        if (population <=0){
            return "Error: Invalid data.";
        }
        Town town = this.modelMapper.map(townDto, Town.class);

        this.townRepository.save(town);

        return String.format("Successfully imported Town %s.", town.getName());
    }
}
