import entities.Project;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import java.util.List;

public class _09_FindLatest10Projects {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PU_Name");
        EntityManager entityManager = emf.createEntityManager();

        entityManager.getTransaction().begin();

        List<Project> resultList = entityManager.createQuery("SELECT p FROM Project p" +
                " ORDER BY p.startDate DESC", Project.class).setMaxResults(10).getResultList();

        for (Project project : resultList) {
            System.out.println(project.getName() + " " + project.getDescription() + " "
            + project.getStartDate() + " " + project.getEndDate());
        }


        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
