package exam.service.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import exam.model.Customer;
import exam.model.Town;
import exam.model.dto.ImportCustomerDTO;
import exam.repository.CustomerRepository;
import exam.repository.TownRepository;
import exam.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final Path path = Path.of("src", "main", "resources", "files", "json", "customers.json");
    private final CustomerRepository customerRepository;
    private final TownRepository townRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final Validator validator;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, TownRepository townRepository) {
        this.customerRepository = customerRepository;
        this.townRepository = townRepository;
        this.gson = new GsonBuilder().create();
        this.modelMapper = new ModelMapper();

        this.modelMapper.addConverter(ctx-> LocalDate.parse(ctx.getSource(), DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                String.class, LocalDate.class);
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Override
    public boolean areImported() {
        return this.customerRepository.count()>0;
    }

    @Override
    public String readCustomersFileContent() throws IOException {
        return Files.readString(path);
    }

    @Override
    public String importCustomers() throws IOException {
        String json = this.readCustomersFileContent();
        ImportCustomerDTO [] importCustomerDTOS = this.gson.fromJson(json, ImportCustomerDTO[].class);
        return Arrays.stream(importCustomerDTOS).map(importDTO -> importCustomer(importDTO)).collect(Collectors.joining("\n"));
    }

    private String importCustomer(ImportCustomerDTO importDTO) {
        Set<ConstraintViolation<ImportCustomerDTO>> validateError = this.validator.validate(importDTO);

        if (!validateError.isEmpty()){
            return "Invalid Customer";
        }

        Optional<Customer> optCustomer = this.customerRepository.findByEmail(importDTO.getEmail());

        if (optCustomer.isPresent()){
            return "Invalid Customer";
        }

        Customer customer = this.modelMapper.map(importDTO, Customer.class);
        Optional<Town> town = this.townRepository.findByName(importDTO.getTown().getName());
        customer.setTown(town.get());
        this.customerRepository.save(customer);

        return String.format("Successfully imported Customer %s %s %s", customer.getFirstName(), customer.getLastName(), customer.getEmail());
    }
}
