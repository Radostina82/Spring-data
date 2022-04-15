package gamestore.servises.impl;

import gamestore.entities.game.AddGameDTO;
import gamestore.entities.game.Game;
import gamestore.repositories.GameRepository;
import gamestore.servises.GameService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;

    @Autowired
    public GameServiceImpl(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    @Override
    public Game add(AddGameDTO gameData) {
        ModelMapper mapper = new ModelMapper();
        mapper.addConverter(ctx -> LocalDate.parse(ctx.getSource(), DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                String.class, LocalDate.class);
        Game gameToAdd = mapper.map(gameData, Game.class);
        return this.gameRepository.save(gameToAdd);
    }
}
