import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class _7_PrintAllMinions {
    public static void main(String[] args) throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", "root");
        props.setProperty("password", "1234");

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/minions_db", props);

        PreparedStatement statement = connection.prepareStatement("SELECT name FROM minions;");

        ResultSet resultSet = statement.executeQuery();
        List<String> minions = new ArrayList<>();
        while (resultSet.next()){
            String nameMinion = resultSet.getString("name");
            minions.add(nameMinion);
        }
        List<String> resultMinions = new ArrayList<>();
        for (int i = 0; i < minions.size()/2; i++) {
                resultMinions.add(minions.get(i));

            for (int j = minions.size()-1-i; j>= minions.size()-1-i; j--) {
                resultMinions.add(minions.get(j));

            }

        }

        for (String resultMinion : resultMinions) {
            System.out.println(resultMinion);
        }
    }
}
