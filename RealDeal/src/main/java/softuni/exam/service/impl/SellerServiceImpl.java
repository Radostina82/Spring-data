package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.Seller;
import softuni.exam.models.dto.ImportSellerDTO;
import softuni.exam.models.dto.ImportSellerRootDTO;
import softuni.exam.repository.SellerRepository;
import softuni.exam.service.SellerService;

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
public class SellerServiceImpl implements SellerService {
    private final Path path= Path.of("src", "main", "resources", "files", "xml", "sellers.xml");
    private final SellerRepository sellerRepository;
    private final ModelMapper modelMapper;
    private final Unmarshaller unmarshaller;
    private final Validator validator;

    @Autowired
    public SellerServiceImpl(SellerRepository sellerRepository) throws JAXBException {
        this.sellerRepository = sellerRepository;
        this.modelMapper = new ModelMapper();
        JAXBContext context = JAXBContext.newInstance(ImportSellerRootDTO.class);
        this.unmarshaller = context.createUnmarshaller();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Override
    public boolean areImported() {
        return this.sellerRepository.count()>0;
    }

    @Override
    public String readSellersFromFile() throws IOException {
        return Files.readString(path);
    }

    @Override
    public String importSellers() throws IOException, JAXBException {
        ImportSellerRootDTO importSellerRootDTO =(ImportSellerRootDTO) this.unmarshaller.unmarshal(new FileReader(path.toAbsolutePath().toString()));
        return importSellerRootDTO.getSellers().stream().map(sellerDto-> importSeller(sellerDto))
                .collect(Collectors.joining("\n"));
    }

    private String importSeller(ImportSellerDTO sellerDto) {
        Set<ConstraintViolation<ImportSellerDTO>> validateErrors = this.validator.validate(sellerDto);
        if (!validateErrors.isEmpty()){
            return "Invalid seller";
        }

        Optional<Seller> sellerOpt = this.sellerRepository.findByEmail(sellerDto.getEmail());
        if (sellerOpt.isPresent()){
            return "Invalid seller";
        }

        Seller seller = this.modelMapper.map(sellerDto, Seller.class);
        this.sellerRepository.save(seller);
        return String.format("Successfully import seller %s - %s", seller.getLastName(), seller.getEmail());
    }
}
