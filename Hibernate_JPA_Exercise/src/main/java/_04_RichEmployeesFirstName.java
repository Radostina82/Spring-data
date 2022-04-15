import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class _04_RichEmployeesFirstName {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PU_Name");
        EntityManager entityManager = emf.createEntityManager();

        entityManager.getTransaction().begin();

        List<String> names = entityManager.createQuery("SELECT e.firstName FROM Employee e" +
                " WHERE e.salary > 50000", String.class).getResultList();

        System.out.println(String.join("\n",names ));
        entityManager.getTransaction().commit();

        entityManager.close();
    }
}
