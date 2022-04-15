import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class _9_IncreaseAge {
    public static void main(String[] args) throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", "root");
        props.setProperty("password", "1234");

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/minions_db", props);
        Scanner scanner = new Scanner(System.in);
        int id = Integer.parseInt(scanner.nextLine());

        PreparedStatement statement = connection.prepareStatement("CALL usp_get_older(?);");
        statement.setInt(1, id);
        statement.execute();

        PreparedStatement statement1 = connection.prepareStatement("SELECT name, age FROM minions WHERE id =?;");
        statement1.setInt(1, id);

        ResultSet resultSet = statement1.executeQuery();

        while (resultSet.next()){
            String name = resultSet.getString("name");
            int age = resultSet.getInt("age");

            System.out.println(name + " " + age);
        }
    }
}
