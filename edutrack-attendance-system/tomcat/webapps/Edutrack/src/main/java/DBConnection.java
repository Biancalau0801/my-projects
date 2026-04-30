import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        
        // 1. Matches your Terminal 'USE EduTrackDB'
        String url = "jdbc:mysql://localhost:3306/edutrackdb";
        String user = "root"; 
        String password = "bianca0801"; 

        return DriverManager.getConnection(url, user, password);
    }
 }
