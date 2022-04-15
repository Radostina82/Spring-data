import entities.Department;
import entities.Employee;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

public class _10_IncreaseSalaries {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PU_Name");
        EntityManager entityManager = emf.createEntityManager();

        entityManager.getTransaction().begin();
        List<String> departmentsName = Arrays.asList("Engineering", "Tool Design", "Marketing", "Information Services");

        List<Department> departments = entityManager.createQuery("FROM Department d WHERE d.name IN (:departments)", Department.class)
                .setParameter("departments", departmentsName).getResultList();

        entityManager.createQuery("UPDATE Employee e" +
                " SET e.salary = e.salary * 1.12" +
                " WHERE e.department IN (:departments)").setParameter("departments", departments).executeUpdate();

        entityManager.createQuery("FROM Employee e WHERE e.department IN (:departments)", Employee.class)
                .setParameter("departments", departments).getResultStream().
                forEach(e-> {
                    System.out.printf("%s %s ($%.2f)%n", e.getFirstName(), e.getLastName(), e.getSalary());
                });

        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
