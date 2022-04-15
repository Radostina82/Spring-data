import entities.Employee;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Scanner;

public class _03_ContainsEmployee {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PU_Name");
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        Scanner scanner = new Scanner(System.in);

        String[] input = scanner.nextLine().split(" ");

        Long employeeCount = entityManager.createQuery("SELECT COUNT(e) FROM Employee e" +
                        " WHERE e.firstName = :first_name" +
                        " AND e.lastName = :last_name", Long.class).setParameter("first_name", input[0]).
                setParameter("last_name", input[1])
                .getSingleResult();

        if (employeeCount > 0) {
            System.out.println("Yes");
        }else {
            System.out.println("No");
        }

        entityManager.getTransaction().commit();

        entityManager.close();
    }
}
