import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class LAB1 {
    public static void main(String[] args) throws SQLException {

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter username default (root): ");
        String user = scanner.nextLine();
        user = user.equals("") ? "root" : user;
        System.out.println();

        System.out.print("Enter password default (empty):");
        String password = scanner.nextLine().trim();
        System.out.println();

        Properties props = new Properties();
        props.setProperty("user", user);
        props.setProperty("password", password);

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/diablo", props);
        PreparedStatement statement = connection.prepareStatement("SELECT count(ug.id) as count, concat(u.first_name, ' ', u.last_name) as full_name, u.user_name FROM users as u" +
                " JOIN users_games as ug On u.id = ug.user_id" +
                " WHERE u.user_name = ?" +
                " GROUP BY ug.user_id;");
        String userName = scanner.nextLine();
        statement.setString(1, userName);

        ResultSet result = statement.executeQuery();

        if(result.next()){
            System.out.printf("User: %s%n", result.getString("user_name"));
            System.out.printf("%s has played %d games", result.getString("full_name"), result.getInt("count"));

        }else {
            System.out.println("No such user exists");
        }



        connection.close();
    }
}
