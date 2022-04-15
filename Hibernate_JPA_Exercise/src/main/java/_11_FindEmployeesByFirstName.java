import entities.Employee;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;
import java.util.Scanner;

public class _11_FindEmployeesByFirstName {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PU_Name");
        EntityManager entityManager = emf.createEntityManager();

        entityManager.getTransaction().begin();
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        Query query = entityManager.createQuery("FROM Employee e WHERE upper(e.firstName) LIKE :value")
                .setParameter("value", input + "%");
        List<Employee> resultList = query.getResultList();
        resultList.forEach(e->{
            System.out.printf("%s %s - %s - ($%.2f)%n", e.getFirstName(), e.getLastName(), e.getJobTitle(), e.getSalary());
        });

        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
