package mapping.ex.demo;

import mapping.ex.demo.DTO.ManagerDTO;
import mapping.ex.demo.entities.Address;
import mapping.ex.demo.entities.Employee;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MapperMain {

    public static void main(String[] args) {
        ModelMapper mapper = new ModelMapper();

        Address address = new Address(
                "boris tri",
                3,
                "Sofia",
                "Bulgaria"
        );

        Employee manager = new Employee(
                "Mr.",
                "Manager",
                BigDecimal.ZERO,
                LocalDate.now(),
                address,
                true);

        Employee first = new Employee(
                "Mr.",
                "Employee First",
                BigDecimal.ONE,
                LocalDate.now(),
                address,
                true);

        Employee second = new Employee(
                "Mr.",
                "Employee Second",
                BigDecimal.TEN,
                LocalDate.now(),
                address,
                true);

        manager.addEmployee(first);
        manager.addEmployee(second);

        ManagerDTO dto = mapper.map(manager, ManagerDTO.class);

        System.out.println(dto);
    }
}