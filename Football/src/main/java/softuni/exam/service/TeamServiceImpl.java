package softuni.exam.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.domain.entities.Picture;
import softuni.exam.domain.entities.Team;
import softuni.exam.domain.entities.dto.importxml.ImportTeamDTO;
import softuni.exam.domain.entities.dto.importxml.ImportTeamRootDTO;
import softuni.exam.repository.PictureRepository;
import softuni.exam.repository.TeamRepository;

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
public class TeamServiceImpl implements TeamService {
    private final Path path = Path.of("src", "main", "resources", "files", "xml", "teams.xml");
    private final TeamRepository teamRepository;
    private final PictureRepository pictureRepository;
    private final ModelMapper modelMapper;
    private final Unmarshaller unmarshaller;
    private final Validator validator;

    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository, PictureRepository pictureRepository) throws JAXBException {
        this.teamRepository = teamRepository;
        this.pictureRepository = pictureRepository;
        this.modelMapper = new ModelMapper();
        JAXBContext context = JAXBContext.newInstance(ImportTeamRootDTO.class);
        this.unmarshaller= context.createUnmarshaller();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }


    @Override
    public String importTeams() throws FileNotFoundException, JAXBException {
       ImportTeamRootDTO importTeamRootDTO =(ImportTeamRootDTO) this.unmarshaller.unmarshal(new FileReader(path.toAbsolutePath().toString()));
        return importTeamRootDTO.getTeams().stream().map(teamDto -> importTeam(teamDto))
                .collect(Collectors.joining("\n"));
    }

    private String importTeam(ImportTeamDTO teamDto) {
        Set<ConstraintViolation<ImportTeamDTO>> validateErrors = this.validator.validate(teamDto);
        if(!validateErrors.isEmpty()){
            return "Invalid team";
        }
        Optional<Picture> picture = this.pictureRepository.findByUrl(teamDto.getUrl().getUrl());
        if (picture.isEmpty()){
            return "Invalid team";
        }

        Team team = this.modelMapper.map(teamDto, Team.class);
        team.setPicture(picture.get());

        this.teamRepository.save(team);
        return String.format("Successfully imported - %s", team.getName());
    }

    @Override
    public boolean areImported() {

        return this.teamRepository.count()>0;
    }

    @Override
    public String readTeamsXmlFile() throws IOException {

        return Files.readString(path);
    }
}
