import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class _5__ChangeTownNames {
    public static void main(String[] args) throws SQLException {
        Properties prop = new Properties();
        prop.setProperty("user", "root");
        prop.setProperty("password", "1234");

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/minions_db", prop);

        Scanner scanner = new Scanner(System.in);

        String countryName = scanner.nextLine();

        PreparedStatement statement = connection.prepareStatement("UPDATE towns SET name = upper(name) WHERE country = ?;");
        statement.setString(1, countryName);
       int updateCount = statement.executeUpdate();

        if (updateCount == 0) {
            System.out.println("No town names were affected.");
            return;
        }

        System.out.println(updateCount + " town names were affected.");
        PreparedStatement selectAllTowns = connection.prepareStatement(
                "SELECT name FROM towns WHERE country = ?"
        );
        selectAllTowns.setString(1, countryName);
        ResultSet townsSet = selectAllTowns.executeQuery();

        List<String> towns = new ArrayList<>();

        while (townsSet.next()){
            String town = townsSet.getString("name");
            towns.add(town);
        }

        System.out.println(towns);
        connection.close();
    }
}
