import entities.Employee;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class _05_EmployeeFromDepartment {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PU_Name");
        EntityManager entityManager = emf.createEntityManager();

        entityManager.getTransaction().begin();

        List<String> result = entityManager.createQuery("SELECT concat(e.firstName, ' ', e.lastName, ' from ', ' ', " +
                "e.department.name, ' - $', e.salary)  FROM Employee e" +
                " WHERE e.department.name = 'Research and Development'" +
                " ORDER BY e.salary ASC," +
                " e.id ASC", String.class).getResultList();

        System.out.println(String.join("\n", result));
        //Варианта на Ники лектора
        String department = "Research and Development";

        entityManager
                .createQuery("SELECT e FROM Employee e" +
                                " WHERE e.department.name = :departmentName" +
                                " ORDER BY e.salary ASC, e.id ASC",
                        Employee.class)
                .setParameter("departmentName", department)
                .getResultStream()
                .forEach(e -> {
                    String format = String.format("%s %s from %s - $%.2f",
                            e.getFirstName(), e.getLastName(), department, e.getSalary());

                    System.out.println(format);
                });

        entityManager.getTransaction().commit();

        entityManager.close();
    }
}
