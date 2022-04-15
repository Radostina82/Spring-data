package exam.service.impl;

import exam.model.Town;
import exam.model.dto.ImportTownDTO;
import exam.model.dto.ImportTownRootDTO;
import exam.repository.TownRepository;
import exam.service.TownService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TownServiceImpl implements TownService {
    private final Path path = Path.of("src", "main", "resources", "files", "xml", "towns.xml");
    private final TownRepository townRepository;
    private final ModelMapper modelMapper;
    private final Unmarshaller unmarshaller;
    private final Validator validator;

    @Autowired
    public TownServiceImpl(TownRepository townRepository) throws JAXBException {
        this.townRepository = townRepository;
        this.modelMapper = new ModelMapper();
        JAXBContext context = JAXBContext.newInstance(ImportTownRootDTO.class);
        this.unmarshaller = context.createUnmarshaller();

        this.validator = Validation.buildDefaultValidatorFactory().getValidator();

    }

    @Override
    public boolean areImported() {
        return this.townRepository.count()>0;
    }

    @Override
    public String readTownsFileContent() throws IOException {

        return Files.readString(path);
    }

    @Override
    public String importTowns() throws JAXBException, FileNotFoundException {
        ImportTownRootDTO importTownRootDTOs =(ImportTownRootDTO) unmarshaller.
                unmarshal(new FileReader(path.toAbsolutePath().toString()));
        return importTownRootDTOs.getTowns().stream().map(townDTO -> importTown(townDTO))
                .collect(Collectors.joining("\n"));
    }

    private String importTown(ImportTownDTO townDTO) {
        Set<ConstraintViolation<ImportTownDTO>> validateErrors = this.validator.validate(townDTO);
        if (!validateErrors.isEmpty()){
            return "Invalid town";
        }

        Town town = this.modelMapper.map(townDTO, Town.class);

        this.townRepository.save(town);

        return "Successfully imported Town"+ " " + town.getName();
    }
}
