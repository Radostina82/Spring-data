package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.Car;
import softuni.exam.models.Offer;
import softuni.exam.models.Seller;
import softuni.exam.models.dto.ImportOfferDTO;
import softuni.exam.models.dto.ImportOfferRootDTO;
import softuni.exam.repository.CarRepository;
import softuni.exam.repository.OfferRepository;
import softuni.exam.repository.SellerRepository;
import softuni.exam.service.OfferService;

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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OfferServiceImpl implements OfferService {
    private final Path path= Path.of("src", "main", "resources", "files", "xml", "offers.xml");
    private final OfferRepository offerRepository;
    private final CarRepository carRepository;
    private final SellerRepository sellerRepository;
    private final ModelMapper modelMapper;
    private final Unmarshaller unmarshaller;
    private final Validator validator;

    @Autowired
    public OfferServiceImpl(OfferRepository offerRepository, CarRepository carRepository, SellerRepository sellerRepository) throws JAXBException {
        this.offerRepository = offerRepository;
        this.carRepository = carRepository;
        this.sellerRepository = sellerRepository;
        this.modelMapper = new ModelMapper();
        JAXBContext context = JAXBContext.newInstance(ImportOfferRootDTO.class);
        this.unmarshaller = context.createUnmarshaller();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.modelMapper.addConverter(ctx-> LocalDateTime.parse(ctx.getSource(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                String.class, LocalDateTime.class);
    }

    @Override
    public boolean areImported() {
        return this.offerRepository.count()>0;
    }

    @Override
    public String readOffersFileContent() throws IOException {
        return Files.readString(path);
    }

    @Override
    public String importOffers() throws IOException, JAXBException {

        ImportOfferRootDTO importOfferRootDTO =(ImportOfferRootDTO) this.unmarshaller.unmarshal(new FileReader(path.toAbsolutePath().toString()));
        return importOfferRootDTO.getOffers().stream().map(offerDTO->importOffer(offerDTO))
                .collect(Collectors.joining("\n"));
    }

    private String importOffer(ImportOfferDTO offerDTO) {
        Set<ConstraintViolation<ImportOfferDTO>> validateErrors = this.validator.validate(offerDTO);
        if (!validateErrors.isEmpty()){
            return "Invalid offer";
        }

       /* Optional<Offer> optionalOffer = this.offerRepository.findByDescriptionAndAddedOn(offerDTO.getDescription(), offerDTO.getAddedOn());
        if (optionalOffer.isPresent()){
            return "Invalid offer";
        }*/
        Optional<Car> carById = this.carRepository.findById(offerDTO.getCar().getId());
        Optional<Seller> sellerById = this.sellerRepository.findById(offerDTO.getSeller().getId());

        if (carById.isEmpty() || sellerById.isEmpty()){
            return "Invalid offer";
        }

        Offer offer = this.modelMapper.map(offerDTO, Offer.class);

        this.offerRepository.save(offer);

        return String.format("Successfully imported %s %s", offer.getAddedOn(), offer.isHasGoldStatus());
    }
}
