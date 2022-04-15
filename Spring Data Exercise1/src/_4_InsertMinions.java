import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class _4_InsertMinions {
    public static void main(String[] args) throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", "root");
        props.setProperty("password", "1234");

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/minions_db", props);

        Scanner scanner = new Scanner(System.in);
        String [] minions = scanner.nextLine().split(" ");
        String nameMinion = minions[1];
        String townMinion = minions[3];
        int ageMinion = Integer.parseInt(minions[2]);
        String nameVillain = scanner.nextLine().split(" ")[1];

        int townId = getOrInsertTown(connection, townMinion);
        int villainId = getOrInsertVillain(connection, nameVillain);

        PreparedStatement statement = connection.prepareStatement("INSERT INTO minions(name, age, town_id) VALUES(?, ?, ?)");
        statement.setString(1, nameMinion);
        statement.setInt(2, ageMinion);
        statement.setInt(3, townId);
        statement.executeUpdate();

        PreparedStatement getLastMinions = connection.prepareStatement("SELECT id FROM minions ORDER BY id DESC LIMIT 1;");
        ResultSet lastMinionSet = getLastMinions.executeQuery();
        lastMinionSet.next();
        int lastMinionId = lastMinionSet.getInt("id");

        PreparedStatement insertMinionsVillains = connection.prepareStatement("INSERT INTO minions_villains VALUES (?, ?)");
        insertMinionsVillains.setInt(1, lastMinionId);
        insertMinionsVillains.setInt(2, villainId);
        insertMinionsVillains.executeUpdate();

        System.out.printf("Successfully added %s to be minion of %s.%n",
                nameMinion, nameVillain);

        connection.close();
    }

    private static int getOrInsertVillain(Connection connection, String nameVillain) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT id FROM villains" +
                " WHERE name = ?; ");
        statement.setString(1, nameVillain);
        ResultSet resultSet = statement.executeQuery();

        int villainId = 0;

        if(!resultSet.next()){
              PreparedStatement insertVillain = connection.prepareStatement("INSERT INTO villains(name, evilness_factor)" +
                      " VALUES(?, ?);");
              insertVillain.setString(1,nameVillain);
              insertVillain.setString(2, "evil");
              insertVillain.executeUpdate();

              ResultSet villainIdSet = statement.executeQuery();
              villainIdSet.next();
              villainId = villainIdSet.getInt("id");

            System.out.printf("Villain %s was added to the database.%n", nameVillain);
        }else {
            villainId = resultSet.getInt("id");
        }
        return villainId;
    }

    private static int getOrInsertTown(Connection connection, String townMinion) throws SQLException {
        PreparedStatement statement = connection.prepareStatement("SELECT id FROM towns" +
                " WHERE name = ?; ");
        statement.setString(1, townMinion);

        ResultSet resultSet = statement.executeQuery();

        int townId = 0;
        if(!resultSet.next()){
            PreparedStatement insertTown = connection.prepareStatement("INSERT INTO towns(name) VALUES(?);");
            insertTown.setString(1,townMinion);
            insertTown.executeUpdate();

            ResultSet townIdSet = statement.executeQuery();
            townIdSet.next();
            townId = townIdSet.getInt("id");
            System.out.printf("Town %s was added to the database.%n", townMinion);
        }else {
            townId = resultSet.getInt("id");
        }

        return townId;
    }
}
