package softuni.exam.service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.domain.entities.Picture;
import softuni.exam.domain.entities.dto.importxml.ImportPictureDTO;
import softuni.exam.domain.entities.dto.importxml.ImportPictureRootDTO;
import softuni.exam.repository.PictureRepository;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PictureServiceImpl implements PictureService {
    private final Path path = Path.of("src", "main", "resources", "files", "xml", "pictures.xml");
    private final PictureRepository pictureRepository;
    private final ModelMapper modelMapper;
    private final Unmarshaller unmarshaller;
    private final Validator validator;

    @Autowired
    public PictureServiceImpl(PictureRepository pictureRepository) throws JAXBException {
        this.pictureRepository = pictureRepository;
        this.modelMapper = new ModelMapper();
        JAXBContext context = JAXBContext.newInstance(ImportPictureRootDTO.class);
        this.unmarshaller= context.createUnmarshaller();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Override
    public String importPictures() throws FileNotFoundException, JAXBException {
       ImportPictureRootDTO importPictureRootDTO =(ImportPictureRootDTO) this.unmarshaller.unmarshal(new FileReader(path.toAbsolutePath().toString()));

        return importPictureRootDTO.getPictures().stream().map(pictureDto-> importPicture(pictureDto))
                .collect(Collectors.joining("\n"));
    }

    private String importPicture(ImportPictureDTO pictureDto) {
        Set<ConstraintViolation<ImportPictureDTO>> validateErrors = this.validator.validate(pictureDto);
        if(!validateErrors.isEmpty()){
            return "Invalid picture";
        }

        Picture picture = this.modelMapper.map(pictureDto, Picture.class);
        this.pictureRepository.save(picture);

        return String.format("Successfully imported picture %s", picture.getUrl());
    }

    @Override
    public boolean areImported() {

        return this.pictureRepository.count()>0;
    }

    @Override
    public String readPicturesXmlFile() throws IOException {

        return Files.readString(path);
    }

}
