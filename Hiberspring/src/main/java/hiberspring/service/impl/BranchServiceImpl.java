package hiberspring.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import hiberspring.domain.dtos.ImportBranchDTO;
import hiberspring.domain.entities.Branch;
import hiberspring.domain.entities.Town;
import hiberspring.repository.BranchRepository;
import hiberspring.repository.TownRepository;
import hiberspring.service.BranchService;
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
public class BranchServiceImpl implements BranchService {
    private final BranchRepository branchRepository;
    private final TownRepository townRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final Validator validator;

    @Autowired
    public BranchServiceImpl(BranchRepository branchRepository, TownRepository townRepository) {
        this.branchRepository = branchRepository;
        this.townRepository = townRepository;
        this.modelMapper = new ModelMapper();
        this.gson = new GsonBuilder().create();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Override
    public Boolean branchesAreImported() {

        return this.branchRepository.count()>0;
    }

    @Override
    public String readBranchesJsonFile() throws IOException {
        Path path = Path.of("src", "main", "resources", "files", "branches.json");
        return Files.readString(path);
    }

    @Override
    public String importBranches(String branchesFileContent) throws IOException {
        String json = this.readBranchesJsonFile();
        ImportBranchDTO[] importBranchDTOS = this.gson.fromJson(json, ImportBranchDTO[].class);
        return Arrays.stream(importBranchDTOS).map(branchDto -> importBranch(branchDto))
                .collect(Collectors.joining("\n"));
    }

    private String importBranch(ImportBranchDTO branchDto) {
        Set<ConstraintViolation<ImportBranchDTO>> validateErrors = this.validator.validate(branchDto);
        if(!validateErrors.isEmpty()){
            return "Error: Invalid data.";
        }

        Optional<Town> town = this.townRepository.findByName(branchDto.getTown());
        if (town.isEmpty()){
            return "Error: Invalid data.";
        }

        Branch branch = this.modelMapper.map(branchDto, Branch.class);

        branch.setTown(town.get());

        this.branchRepository.save(branch);

        return String.format("Successfully imported Branch %s.", branch.getName());
    }
}
