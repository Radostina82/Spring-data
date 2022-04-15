import entities.Address;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.Scanner;

public class _06_InsertAddress {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PU_Name");
        EntityManager entityManager = emf.createEntityManager();

        entityManager.getTransaction().begin();
        String newAddress = "Vitoshka 15";
        Address address = new Address();
        address.setText(newAddress);
        entityManager.persist(address);

        Scanner scanner = new Scanner(System.in);
        String lastName = scanner.nextLine();

        entityManager.createQuery("UPDATE Employee e" +
                " SET e.address = :address" +
                " WHERE e.lastName = :name").setParameter("address", address)
                        .setParameter("name", lastName).executeUpdate();


        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
