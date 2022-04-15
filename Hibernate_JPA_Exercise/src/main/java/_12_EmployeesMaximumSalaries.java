import entities.Employee;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

public class _12_EmployeesMaximumSalaries {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PU_Name");
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        BigDecimal min = new BigDecimal("30000");
        BigDecimal max = new BigDecimal("70000");
        List<String> resultList = entityManager.createQuery("SELECT concat(e.department.name,' ', max(e.salary)) FROM Employee e" +
                        " WHERE e.salary < :min OR e.salary > :max" +
                        " GROUP BY e.department.id", String.class).setParameter("min", min)
                .setParameter("max", max).getResultList();

        for (String s : resultList) {
            System.out.println(s);
        }
        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
