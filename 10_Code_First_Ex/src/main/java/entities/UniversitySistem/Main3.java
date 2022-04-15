package entities.UniversitySistem;

import entities.SalesDatabase.entities.Customer;
import entities.SalesDatabase.entities.Product;
import entities.SalesDatabase.entities.Sale;
import entities.SalesDatabase.entities.StoreLocation;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;

public class Main3 {
    public static void main(String[] args) {
        EntityManagerFactory factory =
                Persistence.createEntityManagerFactory("Code");

        EntityManager entityManager = factory.createEntityManager();
        entityManager.getTransaction().begin();



        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
