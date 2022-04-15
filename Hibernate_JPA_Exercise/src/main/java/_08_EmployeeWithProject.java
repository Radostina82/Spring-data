import entities.Employee;
import entities.Project;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class _08_EmployeeWithProject {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PU_Name");
        EntityManager entityManager = emf.createEntityManager();

        entityManager.getTransaction().begin();
        Scanner scanner = new Scanner(System.in);
        int searchId = Integer.parseInt(scanner.nextLine());

        Employee employee = entityManager.find(Employee.class, searchId);

        System.out.printf("%s %s - %s%n", employee.getFirstName(), employee.getLastName(), employee.getJobTitle());

        employee.getProjects().stream().sorted(Comparator.comparing(Project::getName)).
        forEach(p-> {
            System.out.println(p.getName());
        });

        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
