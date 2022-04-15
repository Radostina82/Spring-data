package softuni.exam.instagraphlite.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.Picture;
import softuni.exam.instagraphlite.models.Post;
import softuni.exam.instagraphlite.models.User;
import softuni.exam.instagraphlite.models.dto.ImportPostDTO;
import softuni.exam.instagraphlite.models.dto.ImportPostRootDTO;
import softuni.exam.instagraphlite.repository.PictureRepository;
import softuni.exam.instagraphlite.repository.PostRepository;
import softuni.exam.instagraphlite.repository.UserRepository;
import softuni.exam.instagraphlite.service.PostService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private final Path path = Path.of("src", "main", "resources", "files", "posts.xml");
    private final PostRepository postRepository;
    private final PictureRepository pictureRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final Unmarshaller unmarshaller;
    private final Validator validator;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, PictureRepository pictureRepository, UserRepository userRepository) throws JAXBException {
        this.postRepository = postRepository;
        this.pictureRepository = pictureRepository;
        this.userRepository = userRepository;
        this.modelMapper = new ModelMapper();
        JAXBContext context = JAXBContext.newInstance(ImportPostRootDTO.class);
        this.unmarshaller = context.createUnmarshaller();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Override
    public boolean areImported() {
        return this.postRepository.count()>0;
    }

    @Override
    public String readFromFileContent() throws IOException {

        return Files.readString(path);
    }

    @Override
    public String importPosts() throws IOException, JAXBException {
        ImportPostRootDTO importPostRootDTOs =(ImportPostRootDTO) this.unmarshaller.unmarshal
                (new FileReader(path.toAbsolutePath().toString()));
        return importPostRootDTOs.getPosts().stream().map(postDto -> importPost(postDto)).collect(Collectors.joining("\n"));
    }

    private String importPost(ImportPostDTO postDto) {
        Set<ConstraintViolation<ImportPostDTO>> validateError = this.validator.validate(postDto);
        if (!validateError.isEmpty()){
            return "Invalid Post";
        }
        Optional<Picture> path = this.pictureRepository.findByPath(postDto.getPicture().getPath());
        if (path.isEmpty()){
            return "Invalid Post";
        }

        Optional<User> userName = this.userRepository.findByUserName(postDto.getUser().getUsername());
        if(userName.isEmpty()){
            return "Invalid Post";
        }

        Post post = this.modelMapper.map(postDto, Post.class);
        post.setUser(userName.get());
        post.setPicture(path.get());
        this.postRepository.save(post);
        return String.format("Successfully imported Post, made by %s", postDto.getUser().getUsername());
    }
}
