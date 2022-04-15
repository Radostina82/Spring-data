package entities.BillPaymentSystem;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main5 {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("Code");
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();

        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
