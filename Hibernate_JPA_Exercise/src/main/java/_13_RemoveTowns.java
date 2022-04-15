import entities.Address;
import entities.Town;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class _13_RemoveTowns {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("PU_Name");
        EntityManager entityManager = emf.createEntityManager();
        entityManager.getTransaction().begin();
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();

        Optional<Town> town1 = entityManager.createQuery("SELECT t FROM Town t WHERE t.name = :town", Town.class)
                .setParameter("town", input).getResultStream().findFirst();
        if (town1.isEmpty()){
            System.out.printf("Town %s doesn't exist in the database", input);
            return;
        }

        Integer townId = town1.get().getId();

        Stream<Integer> idsStream = entityManager.createQuery(" SELECT e.id FROM Employee AS e WHERE e.address.town.id = :town_id", Integer.class)
                .setParameter("town_id", townId)
                .getResultStream();

        String employeeIds = idsStream.map(String::valueOf).collect(Collectors.joining(", "));

        String sql = String.format("Update Employee AS e SET e.address.id = null WHERE e.id IN (%s)", employeeIds);

        entityManager.createQuery(sql)
                .executeUpdate();

        int affectedAddresses = entityManager.createQuery("DELETE FROM Address AS a WHERE a.town.id = :town_id")
                .setParameter("town_id", townId)
                .executeUpdate();

        entityManager.createQuery("DELETE FROM Town AS t WHERE t.name = :town_name")
                .setParameter("town_name", input)
                .executeUpdate();

        System.out.printf("%d address in %s deleted", affectedAddresses, input);


        entityManager.getTransaction().commit();
        entityManager.close();
    }
}
