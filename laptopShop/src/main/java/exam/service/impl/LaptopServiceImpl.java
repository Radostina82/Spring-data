package exam.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exam.model.Laptop;
import exam.model.Shop;
import exam.model.dto.BestLaptopDTO;
import exam.model.dto.ImportLaptopDTO;
import exam.repository.LaptopRepository;
import exam.repository.ShopRepository;
import exam.service.LaptopService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LaptopServiceImpl implements LaptopService {
    private final LaptopRepository laptopRepository;
    private final ShopRepository shopRepository;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final Validator validator;

    @Autowired
    public LaptopServiceImpl(LaptopRepository laptopRepository, ShopRepository shopRepository) {
        this.laptopRepository = laptopRepository;
        this.shopRepository = shopRepository;
        this.modelMapper= new ModelMapper();
        this.gson = new GsonBuilder().create();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Override
    public boolean areImported() {
        return this.laptopRepository.count()>0;
    }

    @Override
    public String readLaptopsFileContent() throws IOException {
        Path path = Path.of("src", "main", "resources", "files", "json", "laptops.json");
        return Files.readString(path);
    }

    @Override
    public String importLaptops() throws IOException {
        String json = this.readLaptopsFileContent();
        ImportLaptopDTO[] importLaptopDTOS = this.gson.fromJson(json, ImportLaptopDTO[].class);

        return Arrays.stream(importLaptopDTOS).map(this::importLaptop).collect(Collectors.joining("\n"));

    }

   private String importLaptop(ImportLaptopDTO importDto) {
        Set<ConstraintViolation<ImportLaptopDTO>> validateError = this.validator.validate(importDto);
        if (!validateError.isEmpty()){
            return "Invalid Laptop";
        }
        Optional<Laptop> optionalLaptop = this.laptopRepository.findByMacAddress(importDto.getMacAddress());
        if (optionalLaptop.isPresent()){
            return "Invalid Laptop";
        }

       Optional<Shop> shop = this.shopRepository.findByName(importDto.getShop().getName());
        Laptop laptop = this.modelMapper.map(importDto, Laptop.class);
        laptop.setShop(shop.get());

        this.laptopRepository.save(laptop);
        return String.format("Successfully imported Laptop %s-%.2f-%d-%d", laptop.getMacAddress(), laptop.getCpuSpeed()
        ,laptop.getRam(), laptop.getStorage());
    }

    @Override
    public String exportBestLaptops() {
        List<BestLaptopDTO> bestLaptops = this.laptopRepository.findBestLaptop();
        List<String> result = new ArrayList<>();
        for (BestLaptopDTO bestLaptop : bestLaptops) {
            result.add(bestLaptop.toString());
        }
        return String.join("\n", result);
    }
}
