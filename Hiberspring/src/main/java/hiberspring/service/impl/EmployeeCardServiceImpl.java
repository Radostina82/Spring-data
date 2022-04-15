package hiberspring.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import hiberspring.domain.dtos.ImportEmployeeCardDTO;
import hiberspring.domain.entities.EmployeeCard;
import hiberspring.repository.EmployeeCardRepository;
import hiberspring.service.EmployeeCardService;
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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmployeeCardServiceImpl implements EmployeeCardService {
    private final EmployeeCardRepository employeeCardRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final Validator validator;

    @Autowired
    public EmployeeCardServiceImpl(EmployeeCardRepository employeeCardRepository) {
        this.employeeCardRepository = employeeCardRepository;
        this.modelMapper = new ModelMapper();
        this.gson = new GsonBuilder().create();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Override
    public Boolean employeeCardsAreImported() {
        return this.employeeCardRepository.count()>0;
    }

    @Override
    public String readEmployeeCardsJsonFile() throws IOException {
        Path path = Path.of("src", "main", "resources", "files", "employee-cards.json");
        return Files.readString(path);
    }

    @Override
    public String importEmployeeCards(String employeeCardsFileContent) throws IOException {
        String json = this.readEmployeeCardsJsonFile();
        ImportEmployeeCardDTO[] importEmployeeCardDTOS = this.gson.fromJson(json, ImportEmployeeCardDTO[].class);
        return Arrays.stream(importEmployeeCardDTOS).map(cardDto -> importCard(cardDto)).
                collect(Collectors.joining("\n"));
    }

    private String importCard(ImportEmployeeCardDTO cardDto) {
        Set<ConstraintViolation<ImportEmployeeCardDTO>> validateErrors = this.validator.validate(cardDto);
        if(!validateErrors.isEmpty()){
            return  "Error: Invalid data.";
        }

        Optional<EmployeeCard> card = this.employeeCardRepository.findByNumber(cardDto.getNumber());
        if (card.isPresent()){
            return  "Error: Invalid data.";
        }

        EmployeeCard employeeCard = this.modelMapper.map(cardDto, EmployeeCard.class);
        this.employeeCardRepository.save(employeeCard);

        return String.format("Successfully imported Employee Card %s.", employeeCard.getNumber());
    }
}
