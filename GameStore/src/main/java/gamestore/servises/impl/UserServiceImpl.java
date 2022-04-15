package gamestore.servises.impl;

import gamestore.entities.user.LoginDTO;
import gamestore.entities.user.RegisterDTO;
import gamestore.entities.user.User;
import gamestore.exeptions.UserNotLoggedInException;
import gamestore.exeptions.ValidationException;
import gamestore.repositories.UserRepository;
import gamestore.servises.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private User current;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
       this.current = null;
        this.userRepository = userRepository;
    }

    @Override
    public User register(RegisterDTO registerData) {
        if (this.current !=null){
           throw new ValidationException("This user is register");
        }
        ModelMapper mapper = new ModelMapper();
        User toRegister = mapper.map(registerData, User.class);

        if (userRepository.count()==0){
            toRegister.setAdmin(true);
        }

      return   this.userRepository.save(toRegister);
    }

    @Override
    public Optional<User> login(LoginDTO loginData) {
       if (this.current !=null){
            throw  new ValidationException("This user is logged");
        }
       Optional<User> user = this.userRepository.findByEmailAndPassword(loginData.getEmail(), loginData.getPassword());
        if (user.isPresent()){
           this.current = user.get();
       }
        return user;
    }

    @Override
    public void logout() {
        this.current=null;
    }
    @Override
    public User getLoggedUser(){
        User user = this.current;
        if (this.current ==null){
            throw  new UserNotLoggedInException();
        }
        return this.current;
    }


}
