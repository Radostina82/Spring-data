package gamestore.servises;

import gamestore.entities.user.LoginDTO;
import gamestore.entities.user.RegisterDTO;
import gamestore.entities.user.User;

import java.util.Optional;

public interface UserService {
    User register(RegisterDTO registerData);
    Optional<User> login(LoginDTO loginData);
    void logout();
    User getLoggedUser();
}
