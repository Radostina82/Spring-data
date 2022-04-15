package json_ex.cardealer.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import json_ex.cardealer.entities.cars.Car;
import json_ex.cardealer.entities.cars.CarImportDTO;
import json_ex.cardealer.entities.customers.Customer;
import json_ex.cardealer.entities.customers.CustomerImportDTO;
import json_ex.cardealer.entities.parts.Part;
import json_ex.cardealer.entities.parts.PartImportDTO;
import json_ex.cardealer.entities.suppliers.Supplier;
import json_ex.cardealer.entities.suppliers.SupplierImportDTO;
import json_ex.cardealer.repositories.CarRepository;
import json_ex.cardealer.repositories.CustomerRepository;
import json_ex.cardealer.repositories.PartRepository;
import json_ex.cardealer.repositories.SupplierRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SeedServiceImpl implements SeedService {
    private static final String SUPPLIER_JSON_PATH = "src\\main\\resources\\suppliers.json";
    private static final String PART_JSON_PATH = "src\\main\\resources\\parts.json";
    private static final String CARS_JSON_PATH = "src\\main\\resources\\cars.json";
    private static final String CUSTOMER_JSON_PATH = "src\\main\\resources\\customers.json";

    private final SupplierRepository supplierRepository;
    private final PartRepository partRepository;
    private final CarRepository carRepository;
    private final CustomerRepository customerRepository;
    private final ModelMapper mapper;
    private final Gson gson;

    @Autowired
    public SeedServiceImpl(SupplierRepository supplierRepository, PartRepository partRepository, CarRepository carRepository, CustomerRepository customerRepository) {
        this.supplierRepository = supplierRepository;
        this.partRepository = partRepository;
        this.carRepository = carRepository;
        this.customerRepository = customerRepository;
        this.mapper = new ModelMapper();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.mapper.addConverter(ctx->LocalDateTime.parse(ctx.getSource(),  DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                String.class, LocalDateTime.class);
    }

    @Override
    public void seedSuppliers() throws FileNotFoundException {
        FileReader fileReader = new FileReader(SUPPLIER_JSON_PATH);
        SupplierImportDTO[] supplierImportDTO = this.gson.fromJson(fileReader, SupplierImportDTO[].class);

        List<Supplier> suppliers = Arrays.stream(supplierImportDTO).map(supplierDTO -> this.mapper.map(
                supplierDTO, Supplier.class)).collect(Collectors.toList());

        this.supplierRepository.saveAll(suppliers);
    }

    @Override
    public void seedParts() throws FileNotFoundException {
        FileReader fileReader = new FileReader(PART_JSON_PATH);
        PartImportDTO[] partImportDTOS = this.gson.fromJson(fileReader, PartImportDTO[].class);

        List<Part> parts = Arrays.stream(partImportDTOS).map(partDTO -> this.mapper.map(partDTO, Part.class))
                .map(this::setRandomSupplier).collect(Collectors.toList());

        this.partRepository.saveAll(parts);
    }

    @Override
    public void seedCars() throws FileNotFoundException {
    FileReader fileReader = new FileReader(CARS_JSON_PATH);
    CarImportDTO[] carImportDTOS = this.gson.fromJson(fileReader, CarImportDTO[].class);
        List<Car> cars = Arrays.stream(carImportDTOS).map(carDTO -> this.mapper.map(carDTO, Car.class))
                .map(this::getRandomParts).collect(Collectors.toList());

    this.carRepository.saveAll(cars);
    }



    @Override
    public void seedCustomers() throws FileNotFoundException {
        FileReader fileReader = new FileReader(CUSTOMER_JSON_PATH);
        CustomerImportDTO[] customerImportDTOS = this.gson.fromJson(fileReader, CustomerImportDTO[].class);

        List<Customer> customers = Arrays.stream(customerImportDTOS).map(customerDTO -> this.mapper.map(customerDTO, Customer.class))
                .collect(Collectors.toList());

        this.customerRepository.saveAll(customers);
    }
    private Part setRandomSupplier(Part part) {
        long supplierCount = this.supplierRepository.count();
        int randomSupplierId = new Random().nextInt((int) supplierCount) + 1;

        Optional<Supplier> supplier = this.supplierRepository.findById(randomSupplierId);

        part.setSupplier(supplier.get());

        return part;

    }
    private Car getRandomParts(Car car) {
        Random random = new Random();
        long partsCount = this.partRepository.count();
        int randomPartCount = random.nextInt((int) partsCount);
        if (randomPartCount < 3){
            randomPartCount = 3;
        }
        Set<Part> parts = new HashSet<>();
        for (int i = 0; i < randomPartCount; i++) {
            if (i==5){
                break;
            }
            int randomPartId = random.nextInt((int) partsCount) +1;
            Optional<Part> part = this.partRepository.findById(randomPartId);
            if (part.isPresent()){

                parts.add(part.get());
            }

        }
        car.setParts(parts);

        return car;

    }

}
