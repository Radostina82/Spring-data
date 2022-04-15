import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class _8_IncreaseMinionsAge {
    public static void main(String[] args) throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", "root");
        props.setProperty("password", "1234");

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/minions_db", props);

        Scanner scanner = new Scanner(System.in);
       int [] data = Arrays.stream(scanner.nextLine().split(" ")).mapToInt(Integer::parseInt).toArray();

        PreparedStatement statement = connection.prepareStatement("SELECT id, name, age FROM minions;");

        ResultSet resultSet = statement.executeQuery();
        List<String> result = new ArrayList<>();
        while (resultSet.next()){
            int id = resultSet.getInt("id");
            for (int datum : data) {
                if (id == datum){
                    PreparedStatement increaseAge = connection.prepareStatement("UPDATE minions SET age = age +1, name = lower(name) WHERE id =? ;");
                    increaseAge.setInt(1, id);
                    increaseAge.executeUpdate();

                    PreparedStatement minionToAdd = connection.prepareStatement("SELECT name, age FROM minions WHERE id =? ;");
                    minionToAdd.setInt(1, id);
                    ResultSet resultSet1 = minionToAdd.executeQuery();

                    String minions = "";
                    while (resultSet1.next()){
                        String name = resultSet1.getString("name");
                        String age = resultSet1.getString("age");
                        minions = name + " " + age;

                    }
                    result.add(minions);
                }
            }
        }
        for (String s : result) {
            System.out.println(s);
        }

    }
}
