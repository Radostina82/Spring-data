package softuni.exam.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.Car;
import softuni.exam.models.dto.ImportCarDTO;
import softuni.exam.repository.CarRepository;
import softuni.exam.service.CarService;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final ModelMapper modelMapper;
    private  final Gson gson;
    private final Validator validator;

    @Autowired
    public CarServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
        this.modelMapper = new ModelMapper();
        this.gson = new GsonBuilder().create();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        this.modelMapper.addConverter(ctx -> LocalDate.parse(ctx.getSource(), DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                String.class, LocalDate.class);
    }

    @Override
    public boolean areImported() {
        return this.carRepository.count()>0;
    }

    @Override
    public String readCarsFileContent() throws IOException {
        Path path = Path.of("src", "main", "resources", "files", "json", "cars.json");
        return Files.readString(path);
    }

    @Override
    public String importCars() throws IOException {
        String json = this.readCarsFileContent();
        ImportCarDTO[] importCarDTOS = this.gson.fromJson(json, ImportCarDTO[].class);
        return Arrays.stream(importCarDTOS).map(carDto-> importCar(carDto)).collect(Collectors.joining("\n"));
    }

    private String importCar(ImportCarDTO carDto) {
        Set<ConstraintViolation<ImportCarDTO>> validateError = this.validator.validate(carDto);
        if(!validateError.isEmpty()){
            return "Invalid car";
        }

        Optional<Car> carOptional = this.carRepository.findByMakeAndModelAndKilometers(carDto.getMake(), carDto.getModel(), carDto.getKilometers());
        if (carOptional.isPresent()){
            return "Invalid car";
        }

        Car car = this.modelMapper.map(carDto, Car.class);
        this.carRepository.save(car);

        return String.format("Successfully imported car - %s - %s", car.getMake(), car.getModel());
    }

    @Override
    public String getCarsOrderByPicturesCountThenByMake() {
        List<Car> allCarsOrdered = this.carRepository.findAllCarsOrderByPicturesCountThenByMake();
        List<String> result = new ArrayList<>();
        for (Car car : allCarsOrdered) {
            result.add(car.toString());
        }
        return String.join("", result);
    }
}
