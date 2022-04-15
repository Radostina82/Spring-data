package com.example.football.service.impl;

import com.example.football.models.dto.StatImportDTO;
import com.example.football.models.dto.StatImportRootDTO;
import com.example.football.models.entity.Stat;
import com.example.football.repository.StatRepository;
import com.example.football.service.StatService;
import org.modelmapper.ModelMapper;
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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class StatServiceImpl implements StatService {
    private final Path path =
            Path.of("src", "main", "resources", "files", "xml", "stats.xml");
    private final StatRepository statRepository;
    private final ModelMapper modelMapper;
    private final Validator validator;
    private final Unmarshaller unmarshaller;

    public StatServiceImpl(StatRepository statRepository) throws JAXBException {
        this.statRepository = statRepository;
        this.modelMapper = new ModelMapper();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();

        JAXBContext context = JAXBContext.newInstance(StatImportRootDTO.class);
        this.unmarshaller = context.createUnmarshaller();
    }

    @Override
    public boolean areImported() {
        return this.statRepository.count()>0;
    }

    @Override
    public String readStatsFileContent() throws IOException {

        return Files.readString(path);
    }

    @Override
    public String importStats() throws FileNotFoundException, JAXBException {

        StatImportRootDTO statDTO = (StatImportRootDTO) this.unmarshaller.
                unmarshal(new FileReader(path.toAbsolutePath().toString()));


        return statDTO.getStats().stream().map(stat -> importStat(stat)).collect(Collectors.joining("\n"));
    }

    private String importStat(StatImportDTO stat) {
        Set<ConstraintViolation<StatImportDTO>> validateErrors = this.validator.validate(stat);
        if (!validateErrors.isEmpty()){
            return "Invalid stat";
        }

        Optional<Stat> opStat = this.statRepository.findByShootingAndPassingAndEndurance(stat.getShooting(), stat.getPassing(), stat.getEndurance());
        if (opStat.isPresent()){
            return "Invalid stat";
        }

        Stat stat1 = this.modelMapper.map(stat, Stat.class);

        this.statRepository.save(stat1);

        return "Successfully imported Stat " + stat;
    }
}
