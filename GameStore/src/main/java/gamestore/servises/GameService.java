package gamestore.servises;

import gamestore.entities.game.AddGameDTO;
import gamestore.entities.game.Game;

public interface GameService {
    Game add(AddGameDTO commandParts);
}
