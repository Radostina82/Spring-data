package softuni.exam.instagraphlite.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.Picture;
import softuni.exam.instagraphlite.models.Post;
import softuni.exam.instagraphlite.models.User;
import softuni.exam.instagraphlite.models.dto.ImportUserDTO;
import softuni.exam.instagraphlite.repository.PictureRepository;
import softuni.exam.instagraphlite.repository.PostRepository;
import softuni.exam.instagraphlite.repository.UserRepository;
import softuni.exam.instagraphlite.service.UserService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PictureRepository pictureRepository;
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final Validator validator;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PictureRepository pictureRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.pictureRepository = pictureRepository;
        this.postRepository = postRepository;
        this.modelMapper = new ModelMapper();
        this.gson = new GsonBuilder().create();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Override
    public boolean areImported() {
        return this.userRepository.count()>0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        Path path = Path.of("src", "main", "resources", "files", "users.json");
        return Files.readString(path);
    }

    @Override
    public String importUsers() throws IOException {
        String json = this.readFromFileContent();
        ImportUserDTO[] importUserDTOS = this.gson.fromJson(json, ImportUserDTO[].class);
        return   Arrays.stream(importUserDTOS).map(userDto -> importUser(userDto)).collect(Collectors.joining("\n"));

    }

    private String importUser(ImportUserDTO userDto) {
        Set<ConstraintViolation<ImportUserDTO>> validateErrors = this.validator.validate(userDto);
        if (!validateErrors.isEmpty()){
            return "Invalid User";
        }

        Optional<Picture> picture = this.pictureRepository.findByPath(userDto.getProfilePicture());

        if (picture.isEmpty()){
            return "Invalid User";
        }

        Optional<User> userName = this.userRepository.findByUserName(userDto.getUsername());
        if (userName.isPresent()){
            return "Invalid User";
        }
        User user = this.modelMapper.map(userDto, User.class);
        user.setProfilePicture(picture.get());
        this.userRepository.save(user);

        return String.format("Successfully imported User: %s", user.getUserName());
    }

    @Override
    public String exportUsersWithTheirPosts() {
        List<User> byUserPostOrderByCountOfPostId = this.userRepository.findByUserPostOrderByCountOfPostId();
        List<String> result = new ArrayList<>();
        for (User user : byUserPostOrderByCountOfPostId) {
            List<Post> posts = this.postRepository.findAllByUserIdOrderByPictureSize(user.getId());
            result.add(String.format("User: %s%nPost count: %d%n", user.getUserName(), posts.size()));
            for (Post post : posts) {
                result.add(post.toString());
            }
        }
        return String.join("", result);
    }
}
