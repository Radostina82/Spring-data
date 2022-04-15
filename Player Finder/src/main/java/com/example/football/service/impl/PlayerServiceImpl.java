package com.example.football.service.impl;

import com.example.football.models.dto.PlayerImportDTO;
import com.example.football.models.dto.PlayerImportRootDTO;
import com.example.football.models.entity.Player;
import com.example.football.models.entity.Stat;
import com.example.football.models.entity.Team;
import com.example.football.models.entity.Town;
import com.example.football.repository.PlayerRepository;
import com.example.football.repository.StatRepository;
import com.example.football.repository.TeamRepository;
import com.example.football.repository.TownRepository;
import com.example.football.service.PlayerService;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PlayerServiceImpl implements PlayerService {
    private final java.nio.file.Path path =
            Path.of("src", "main", "resources", "files", "xml", "players.xml");
    private final PlayerRepository playerRepository;
    private final TownRepository townRepository;
    private final TeamRepository teamRepository;
    private final StatRepository statRepository;
    private final ModelMapper modelMapper;
    private final Unmarshaller unmarshaller;
    private final Validator validator;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository, TownRepository townRepository, TeamRepository teamRepository, StatRepository statRepository) throws JAXBException {
        this.playerRepository = playerRepository;
        this.townRepository = townRepository;
        this.teamRepository = teamRepository;
        this.statRepository = statRepository;
        this.modelMapper = new ModelMapper();
        JAXBContext context = JAXBContext.newInstance(PlayerImportRootDTO.class);
        this.unmarshaller = context.createUnmarshaller();

        this.validator = Validation.buildDefaultValidatorFactory().getValidator();

        //      This works
//        this.modelMapper.addConverter(new Converter<String, LocalDate>() {
//            @Override
//            public LocalDate convert(MappingContext<String, LocalDate> context) {
//                return LocalDate.parse(context.getSource(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
//            }
//        });

//      This also works
//        this.modelMapper.addConverter((Converter<String, LocalDate>)
//                context1 -> LocalDate.parse(context1.getSource(), DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        this.modelMapper.addConverter(ctx -> LocalDate.parse(ctx.getSource(), DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                String.class, LocalDate.class);

    }

    @Override
    public boolean areImported() {
        return this.playerRepository.count() > 0;
    }

    @Override
    public String readPlayersFileContent() throws IOException {
        return Files.readString(path);
    }

    @Override
    public String importPlayers() throws FileNotFoundException, JAXBException {
        PlayerImportRootDTO importRootDTO =(PlayerImportRootDTO) unmarshaller.
                unmarshal(new FileReader(path.toAbsolutePath().toString()));

        return importRootDTO.getPlayers().stream()
                .map(p -> importPlayer(p)).collect(Collectors.joining("\n"));
    }

    private String importPlayer(PlayerImportDTO dto) {
        Set<ConstraintViolation<PlayerImportDTO>> validateErrors = this.validator.validate(dto);
        if (!validateErrors.isEmpty()){
            return "Invalid player";
        }

        Optional<Player> playerEmail = this.playerRepository.findByEmail(dto.getEmail());
        if (playerEmail.isPresent()){
            return "Invalid player";
        }

        Player player = this.modelMapper.map(dto, Player.class);
        Optional<Town> town = this.townRepository.findByName(dto.getTown().getName());
        Optional<Team> team = this.teamRepository.findByName(dto.getTeam().getName());
        Optional<Stat> stat = this.statRepository.findById(dto.getStat().getId());

        player.setTown(town.get());
        player.setTeam(team.get());
        player.setStat(stat.get());
        this.playerRepository.save(player);

        return "Successfully imported Player " + player.getFirstName() + " " +
                player.getLastName() + " - " + player.getPosition().toString();
    }

    @Override
    public String exportBestPlayers() {
        LocalDate before = LocalDate.of(2003, 1, 1);
        LocalDate after = LocalDate.of(1995, 1, 1);

        List<Player> bestPlayers = this.playerRepository.findByBirthDateBetweenOrderByStatShootingDescStatPassingDescStatEnduranceDescLastNameAsc(after, before);


        return bestPlayers.stream().map(p-> p.toString()).collect(Collectors.joining("\n"));
    }
}
