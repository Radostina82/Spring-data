package entities.WizardDeposit;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main1 {

        public static void main(String[] args) {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("Code");
            EntityManager entityManager = emf.createEntityManager();
            entityManager.getTransaction().begin();



            entityManager.getTransaction().commit();
            entityManager.close();
        }
}
