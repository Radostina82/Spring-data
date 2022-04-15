import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class _3_GetMinionNames {
    public static void main(String[] args) throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", "root");
        props.setProperty("password", "1234");
        Scanner scanner = new Scanner(System.in);
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/minions_db", props);

        PreparedStatement statement = connection.prepareStatement("SELECT v.name, m.name, m.age FROM villains as v" +
                " JOIN minions_villains as mv ON v.id = mv.villain_id" +
                " JOIN minions as m ON m.id = mv.minion_id" +
                " WHERE v.id = ?;");
        int id = Integer.parseInt(scanner.nextLine());
        statement.setInt(1, id);

        ResultSet resultSet = statement.executeQuery();
        int count = 1;
        if(!resultSet.next()){
            System.out.printf("No villain with ID %d exists in the database.", id);
            return;

        }
        System.out.printf("Villain: %s%n", resultSet.getString("v.name"));
        System.out.printf("%d. %s %d%n", count, resultSet.getString("m.name"), resultSet.getInt("m.age"));
        while (resultSet.next()){
            count++;
            System.out.printf("%d. %s %d%n", count, resultSet.getString("m.name"), resultSet.getInt("m.age"));

        }
    }
}
