import java.sql.*;
import java.util.Properties;
import java.util.Scanner;

public class _6_DeleteVillain {
    public static void main(String[] args) throws SQLException {
        Properties props = new Properties();
        props.setProperty("user", "root");
        props.setProperty("password", "1234");

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/minions_db", props);

        Scanner scanner = new Scanner(System.in);

        int villainId = Integer.parseInt(scanner.nextLine());

        PreparedStatement selectVillains = connection.prepareStatement("SELECT name FROM villains WHERE id = ?");
        selectVillains.setInt(1, villainId);

        ResultSet villainsSet = selectVillains.executeQuery();

        if(!villainsSet.next()){
            System.out.println("No such villain was found");
            return;
        }

        String villainName = villainsSet.getString("name");

        PreparedStatement selectAllVillainMinions = connection.prepareStatement(
                "SELECT COUNT(DISTINCT minion_id) as m_count" +
                        " FROM minions_villains WHERE villain_id = ?");
        selectAllVillainMinions.setInt(1, villainId);

        ResultSet minionsCountSet = selectAllVillainMinions.executeQuery();
        minionsCountSet.next();

        int countMinions = minionsCountSet.getInt("m_count");

        connection.setAutoCommit(false);

        try {
            PreparedStatement deleteMinionsVillains = connection.prepareStatement("DELETE FROM minions_villains WHERE villain_id = ?;");
            deleteMinionsVillains.setInt(1, villainId);
            deleteMinionsVillains.executeUpdate();

            PreparedStatement deleteVillain = connection.prepareStatement("DELETE FROM villains WHERE id = ?");
            deleteVillain.setInt(1, villainId);
            deleteVillain.executeUpdate();

            connection.commit();

            System.out.println(villainName + " was deleted");
            System.out.println(countMinions + " minions released");

        }catch (SQLException ex){
            ex.printStackTrace();
        }

        connection.close();
    }
}
