package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.xml.ImportOfferDTO;
import softuni.exam.models.dto.xml.ImportOfferRootDTO;
import softuni.exam.models.entity.Agent;
import softuni.exam.models.entity.Apartment;
import softuni.exam.models.entity.ApartmentType;
import softuni.exam.models.entity.Offer;
import softuni.exam.repository.AgentRepository;
import softuni.exam.repository.ApartmentRepository;
import softuni.exam.repository.OfferRepository;
import softuni.exam.service.OfferService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OfferServiceImpl implements OfferService {
    private final Path path = Path.of("src", "main", "resources", "files", "xml", "offers.xml");
    private final OfferRepository offerRepository;
    private final AgentRepository agentRepository;
    private final ApartmentRepository apartmentRepository;
    private final ModelMapper modelMapper;
    private final Unmarshaller unmarshaller;
    private final Validator validator;

    @Autowired
    public OfferServiceImpl(OfferRepository offerRepository, AgentRepository agentRepository, ApartmentRepository apartmentRepository) throws JAXBException {
        this.offerRepository = offerRepository;
        this.agentRepository = agentRepository;
        this.apartmentRepository = apartmentRepository;
        this.modelMapper = new ModelMapper();
        JAXBContext context = JAXBContext.newInstance(ImportOfferRootDTO.class);
        this.unmarshaller = context.createUnmarshaller();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.modelMapper.addConverter(ctx -> LocalDate.parse(ctx.getSource(), DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                String.class, LocalDate.class);
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
        return importOfferRootDTO.getOffers().stream().map(offerDto -> importOffer(offerDto))
                .collect(Collectors.joining("\n"));
    }

    private String importOffer(ImportOfferDTO offerDto) {
        Set<ConstraintViolation<ImportOfferDTO>> validateErrors = this.validator.validate(offerDto);
        if (!validateErrors.isEmpty()){
            return "Invalid offer";
        }
        Optional<Agent> agent = this.agentRepository.findByFirstName(offerDto.getAgent().getName());
        if (agent.isEmpty()){
            return "Invalid offer";
        }

        Optional<Apartment> apartment = this.apartmentRepository.findById(offerDto.getApartment().getId());

        Offer offer = this.modelMapper.map(offerDto, Offer.class);
        offer.setAgent(agent.get());
        offer.setApartment(apartment.get());


        this.offerRepository.save(offer);
        return String.format("Successfully imported offer %.2f", offer.getPrice());
    }

    @Override
    public String exportOffers() {
        List<Offer> apartmentByType = this.offerRepository.findAllByApartmentTypeOrderByAreaDescPriceAsc(ApartmentType.three_rooms);
        List<String> result = new ArrayList<>();

        for (Offer offer : apartmentByType) {
            result.add(String.format("Agent %s %s with offer №%d:%n" +
                    "   -Apartment area: %.2f%n" +
                    "   --Town: %s%n" +
                    "   ---Price: %.2f$%n", offer.getAgent().getFirstName(), offer.getAgent().getLastName(), offer.getId(),
                    offer.getApartment().getArea(), offer.getApartment().getTown().getTownName(), offer.getPrice()));
        }
        return String.join("", result);
    }
}
