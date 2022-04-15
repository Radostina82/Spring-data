package hiberspring.service.impl;

import hiberspring.domain.dtos.ImportEmployeeDTO;
import hiberspring.domain.dtos.ImportEmployeeRootDTO;
import hiberspring.domain.entities.Branch;
import hiberspring.domain.entities.Employee;
import hiberspring.domain.entities.EmployeeCard;
import hiberspring.repository.BranchRepository;
import hiberspring.repository.EmployeeCardRepository;
import hiberspring.repository.EmployeeRepository;
import hiberspring.service.EmployeeService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private final Path path = Path.of("src", "main", "resources", "files", "employees.xml");
    private final EmployeeRepository employeeRepository;
    private final BranchRepository branchRepository;
    private final EmployeeCardRepository employeeCardRepository;
    private final ModelMapper modelMapper;
    private final Validator validator;
    private final Unmarshaller unmarshaller;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, BranchRepository branchRepository, EmployeeCardRepository employeeCardRepository) throws JAXBException {
        this.employeeRepository = employeeRepository;
        this.branchRepository = branchRepository;
        this.employeeCardRepository = employeeCardRepository;
        this.modelMapper = new ModelMapper();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
        JAXBContext context = JAXBContext.newInstance(ImportEmployeeRootDTO.class);
        this.unmarshaller= context.createUnmarshaller();
    }


    @Override
    public Boolean employeesAreImported() {
        return this.employeeRepository.count()>0;
    }

    @Override
    public String readEmployeesXmlFile() throws IOException {
        return Files.readString(path);
    }

    @Override
    public String importEmployees() throws JAXBException, FileNotFoundException {
        ImportEmployeeRootDTO importEmployeeRootDTO =(ImportEmployeeRootDTO) this.unmarshaller.unmarshal(new FileReader(path.toAbsolutePath().toString()));

        return importEmployeeRootDTO.getEmployees().stream().map(employeeDto -> importEmployee(employeeDto))
                .collect(Collectors.joining("\n"));
    }

    private String importEmployee(ImportEmployeeDTO employeeDto) {
        Set<ConstraintViolation<ImportEmployeeDTO>> validateErrors = this.validator.validate(employeeDto);
        if (!validateErrors.isEmpty()){
            return "Error: Invalid data.";
        }
        Optional<EmployeeCard> card = this.employeeCardRepository.findByNumber(employeeDto.getCard());
        Optional<Branch> branch = this.branchRepository.findByName(employeeDto.getBranch());

        if (card.isEmpty() || branch.isEmpty()){
            return "Error: Invalid data.";
        }

        Employee employee = this.modelMapper.map(employeeDto, Employee.class);
        employee.setCard(card.get());
        employee.setBranch(branch.get());
        this.employeeRepository.save(employee);

        return String.format("Successfully imported Employee %s %s.", employee.getFirstName(), employee.getLastName());
    }

    @Override
    public String exportProductiveEmployees() {
        List<Employee> employees = this.employeeRepository.findBranchWithProduct();
        List<String> result = new ArrayList<>();
        for (Employee employee : employees) {
            result.add(String.format("Name: %s %s%n" +
                    "Position: %s%n" +
                    "Card Number: %s%n" +
                            "-------------------------%n", employee.getFirstName(), employee.getLastName(),
                    employee.getPosition(), employee.getCard().getNumber()));

        }
        return String.join("",result);
    }
}
