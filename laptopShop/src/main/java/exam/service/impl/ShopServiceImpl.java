package exam.service.impl;

import exam.model.Shop;
import exam.model.Town;
import exam.model.dto.ImportShopDTO;
import exam.model.dto.ImportShopRootDTO;
import exam.repository.ShopRepository;
import exam.repository.TownRepository;
import exam.service.ShopService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ShopServiceImpl implements ShopService {
    private final Path path = Path.of("src", "main", "resources", "files", "xml", "shops.xml");
    private final ShopRepository shopRepository;
    private final TownRepository townRepository;
    private final ModelMapper modelMapper;
    private final Unmarshaller unmarshaller;
    private final Validator validator;

    @Autowired
    public ShopServiceImpl(ShopRepository shopRepository, TownRepository townRepository) throws JAXBException {
        this.shopRepository = shopRepository;
        this.townRepository = townRepository;
        this.modelMapper = new ModelMapper();
        JAXBContext context = JAXBContext.newInstance(ImportShopRootDTO.class);
        this.unmarshaller = context.createUnmarshaller();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Override
    public boolean areImported() {
        return this.shopRepository.count() > 0;
    }

    @Override
    public String readShopsFileContent() throws IOException {
        return Files.readString(path);
    }

    @Override
    public String importShops() throws JAXBException, FileNotFoundException {
        ImportShopRootDTO shopRootDTOs = (ImportShopRootDTO) unmarshaller.
                unmarshal(new FileReader(path.toAbsolutePath().toString()));
        return shopRootDTOs.getShops().stream().map(shopDTO -> importShop(shopDTO)).collect(Collectors.joining("\n"));
    }

    private String importShop(ImportShopDTO shopDTO) {
        Set<ConstraintViolation<ImportShopDTO>> validateErrors = this.validator.validate(shopDTO);
        if (!validateErrors.isEmpty()) {
            return "Invalid shop";
        }

        Optional<Shop> shopOptional = this.shopRepository.findByName(shopDTO.getName());
        if (shopOptional.isPresent()) {
            return "Invalid shop";
        }

        Shop shop = this.modelMapper.map(shopDTO, Shop.class);
        Optional<Town> town = this.townRepository.findByName(shopDTO.getTown().getName());
        shop.setTown(town.get());
        this.shopRepository.save(shop);

        return String.format("Successfully imported Shop %s %.0f", shop.getName(), shop.getIncome());
    }
}