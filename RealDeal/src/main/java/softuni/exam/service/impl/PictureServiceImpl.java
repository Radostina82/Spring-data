package softuni.exam.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.Car;
import softuni.exam.models.Picture;
import softuni.exam.models.dto.ImportPictureDTO;
import softuni.exam.repository.CarRepository;
import softuni.exam.repository.PictureRepository;
import softuni.exam.service.PictureService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PictureServiceImpl implements PictureService {
    private final PictureRepository pictureRepository;
    private final CarRepository carRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final Validator validator;

    @Autowired
    public PictureServiceImpl(PictureRepository pictureRepository, CarRepository carRepository) {
        this.pictureRepository = pictureRepository;
        this.carRepository = carRepository;
        this.modelMapper = new ModelMapper();
        this.gson = new GsonBuilder().create();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.modelMapper.addConverter(ctx-> LocalDateTime.parse(ctx.getSource(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                String.class, LocalDateTime.class);
    }

    @Override
    public boolean areImported() {
        return this.pictureRepository.count()>0;
    }

    @Override
    public String readPicturesFromFile() throws IOException {
        Path path = Path.of("src", "main", "resources", "files", "json", "pictures.json");
        return Files.readString(path);
    }

    @Override
    public String importPictures() throws IOException {
        String json = this.readPicturesFromFile();
        ImportPictureDTO[] importPictureDTOS = this.gson.fromJson(json, ImportPictureDTO[].class);
        return Arrays.stream(importPictureDTOS).map(pictureDto->importPicture(pictureDto)).collect(Collectors.joining("\n"));
    }

    private String importPicture(ImportPictureDTO pictureDto) {
        Set<ConstraintViolation<ImportPictureDTO>> validateErrors = this.validator.validate(pictureDto);
        if (!validateErrors.isEmpty()){
            return "Invalid picture";
        }

        Optional<Picture> optPicture = this.pictureRepository.findByName(pictureDto.getName());
        if (optPicture.isPresent()){
            return "Invalid picture";
        }
        Optional<Car> car = this.carRepository.findById(pictureDto.getCar());
        if (car.isEmpty()) {
            return "Invalid picture";
        }
        Picture picture = this.modelMapper.map(pictureDto, Picture.class);
        picture.setCar(car.get());
        this.pictureRepository.save(picture);
        return String.format("Successfully imported picture %s", picture.getName());
    }
}
