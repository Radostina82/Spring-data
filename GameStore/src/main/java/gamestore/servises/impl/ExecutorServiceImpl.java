package gamestore.servises.impl;

import gamestore.entities.game.Game;
import gamestore.entities.game.AddGameDTO;
import gamestore.entities.user.LoginDTO;
import gamestore.entities.user.RegisterDTO;
import gamestore.entities.user.User;
import gamestore.exeptions.UserNotAdminException;
import gamestore.servises.ExecutorService;
import gamestore.servises.GameService;
import gamestore.servises.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class ExecutorServiceImpl implements ExecutorService {

    private final UserService userService;
    private final GameService gameService;
    @Autowired
    public ExecutorServiceImpl(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }


    @Override
    public String execute(String commandLine) {

        String[] commandsPart = commandLine.split("\\|");
        String commandName = commandsPart[0];

        String commandOutput = switch (commandName) {
            case REGISTER_USER_COMMAND -> registerUser(commandsPart);
            case LOGIN_USER_COMMAND -> loginUser(commandsPart);
            case LOGOUT_USER_COMMAND -> logoutUser();
            case ADD_GAME_COMMAND -> addGame(commandsPart);


            default -> "unknown command";
        };

        return commandOutput;
    }

    private String addGame(String[] commandsPart) {
        AddGameDTO addData = new AddGameDTO(commandsPart);

        User loggedUser = this.userService.getLoggedUser();

        if (!loggedUser.isAdmin()){
            throw new UserNotAdminException();
        }
        Game game = gameService.add(addData);

            return "Added " + game.getTitle();
    }

    private String logoutUser() {
        User loggedUser = this.userService.getLoggedUser();

        this.userService.logout();
        return "User " + loggedUser.getFullName()+ " successfully logged out";
    }

    private String loginUser(String[] commandsPart) {
        LoginDTO loginData = new LoginDTO(commandsPart);

        Optional<User> user = userService.login(loginData);
        if (user.isPresent()) {
            return "Successfully logged in " + user.get().getFullName();
        }else {
            return "Wrong credentials.";
        }
    }

    private String registerUser(String[] commandsPart) {
        RegisterDTO registerData = new RegisterDTO(commandsPart);
        User user = userService.register(registerData);
        return user.getFullName() + " was registered";
    }

}
