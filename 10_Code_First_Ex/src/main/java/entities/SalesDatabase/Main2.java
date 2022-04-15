package entities.SalesDatabase;

import entities.SalesDatabase.entities.Customer;
import entities.SalesDatabase.entities.Product;
import entities.SalesDatabase.entities.Sale;
import entities.SalesDatabase.entities.StoreLocation;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.math.BigDecimal;

public class Main2 {
    public static void main(String[] args) {
        EntityManagerFactory factory =
                Persistence.createEntityManagerFactory("Code");

        EntityManager entityManager = factory.createEntityManager();
        entityManager.getTransaction().begin();

        Product product = new Product("product", 123.00, BigDecimal.TEN);

        Customer customer =
                new Customer("customer", "customer", "asd");
        StoreLocation location = new StoreLocation("location");
        Sale sale = new Sale(product, customer, location);

        entityManager.persist(product);
        entityManager.persist(customer);
        entityManager.persist(location);
        entityManager.persist(sale);

        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
