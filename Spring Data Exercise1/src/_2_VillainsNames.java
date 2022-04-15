import java.sql.*;
import java.util.Properties;

public class _2_VillainsNames {
    public static void main(String[] args) throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", "root");
        props.setProperty("password", "1234");

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/minions_db", props);

        PreparedStatement statement = connection.prepareStatement("SELECT v.name, count(mv.minion_id) as count FROM villains as v" +
                " JOIN minions_villains as mv ON v.id = mv.villain_id" +
                " GROUP BY mv.villain_id" +
                " HAVING count >15" +
                " ORDER BY count DESC;");

        ResultSet resultSet = statement.executeQuery();

        while (resultSet.next()){
            System.out.printf("%s %d%n", resultSet.getString("name"), resultSet.getInt("count"));
        }

        connection.close();
    }
}
