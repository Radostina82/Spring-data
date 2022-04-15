package gamestore.servises;

public interface ExecutorService {
    String REGISTER_USER_COMMAND = "RegisterUser";
    String LOGIN_USER_COMMAND = "LoginUser";
    String LOGOUT_USER_COMMAND ="Logout";
    String ADD_GAME_COMMAND = "AddGame";
    String EDIT_GAME_COMMAND = "EditGame";
    String DELETE_GAME_COMMAND = "DeleteGame";

    String execute(String command);
}

