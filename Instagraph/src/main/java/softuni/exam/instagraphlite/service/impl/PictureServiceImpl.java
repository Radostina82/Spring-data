package softuni.exam.instagraphlite.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.instagraphlite.models.Picture;
import softuni.exam.instagraphlite.models.dto.ImportPictureDTO;
import softuni.exam.instagraphlite.repository.PictureRepository;
import softuni.exam.instagraphlite.service.PictureService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PictureServiceImpl implements PictureService {
    private final PictureRepository pictureRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final Validator validator   ;

    @Autowired
    public PictureServiceImpl(PictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;
        this.modelMapper = new ModelMapper();
        this.gson = new GsonBuilder().create();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Override
    public boolean areImported() {
        return this.pictureRepository.count()>0;
    }

    @Override
    public String readFromFileContent() throws IOException {
        Path path = Path.of("src", "main", "resources", "files", "pictures.json");
        return Files.readString(path);
    }

    @Override
    public String importPictures() throws IOException {
        String json = this.readFromFileContent();
        ImportPictureDTO[] importPictureDTOS = this.gson.fromJson(json, ImportPictureDTO[].class);
        List<String> result = new ArrayList<>();
        for (ImportPictureDTO importPictureDTO : importPictureDTOS) {
            Set<ConstraintViolation<ImportPictureDTO>> validateErrors = this.validator.validate(importPictureDTO);
            if (!validateErrors.isEmpty()){
                result.add("Invalid Picture") ;
            }else {
                Optional<Picture> path = this.pictureRepository.findByPath(importPictureDTO.getPath());
                if (path.isPresent()){
                    result.add("Invalid Picture");
                }else{
                Picture picture = this.modelMapper.map(importPictureDTO, Picture.class);

            this.pictureRepository.save(picture);
            result.add(String.format("Successfully imported Picture, with size %.2f", picture.getSize()));
        }}}
       // return  Arrays.stream(importPictureDTOS).map(pictureDto -> importPicture(pictureDto)).collect(Collectors.joining("\n"));
return String.join("\n", result);
    }

    private String importPicture(ImportPictureDTO pictureDto) {
        Set<ConstraintViolation<ImportPictureDTO>> validateErrors = this.validator.validate(pictureDto);
        if (!validateErrors.isEmpty()){
            return "Invalid Picture";
        }

        Picture picture = this.modelMapper.map(pictureDto, Picture.class);

        this.pictureRepository.save(picture);

        return String.format("Successfully imported Picture, with size %.2f", picture.getSize());
    }

    @Override
    public String exportPictures() {
        List<Picture> allPicturesBySize = this.pictureRepository.findAllBySizeGreaterThan(3000);
        List<String> result = new ArrayList<>();
        for (Picture picture : allPicturesBySize) {
            result.add(picture.toString());
        }
        return String.join("\n", result);
    }
}
