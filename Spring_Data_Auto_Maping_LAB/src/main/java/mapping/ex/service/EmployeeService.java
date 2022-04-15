package mapping.ex.service;

import mapping.ex.entities.Employee;
import mapping.ex.entities.dto.EmployeeSpringDTO;

import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    Optional<Employee> findOneById(int id);

    void save(Employee employee);

    List<EmployeeSpringDTO> findEmployeesBornBefore(int year);

    List<Employee> findAll();
}