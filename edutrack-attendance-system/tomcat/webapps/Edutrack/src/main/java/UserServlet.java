import java.io.IOException;
import java.sql.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/UserServlet")
public class UserServlet extends HttpServlet {
    
    // -------------------------------
    // Database configuration
    // Use same settings as NotificationServlet
    // -------------------------------
    private static final String DB_URL = "jdbc:mysql://localhost:3306/edutrackdb";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "bianca0801";

    // -------------------------------
    // Handle GET requests
    // Retrieve all users from database and forward to JSP
    // -------------------------------
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // List to store user records
        List<Map<String, String>> users = new ArrayList<>();
        
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

            // SQL query: fetch all users
            String sql = "SELECT userID, userName, userEmail, role, status FROM user";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            
            // Loop through result set and store each user in a Map
            while(rs.next()) {
                Map<String, String> user = new HashMap<>();
                user.put("id", rs.getString("userID"));
                user.put("name", rs.getString("userName"));
                user.put("email", rs.getString("userEmail"));
                user.put("role", rs.getString("role"));
                user.put("status", rs.getString("status"));
                users.add(user); // add to list
            }

            // Close database connection
            conn.close();
        } catch (Exception e) {
            e.printStackTrace(); // log any exceptions
        }

        // Pass the user list to JSP for rendering
        request.setAttribute("user", users);
        request.getRequestDispatcher("user_management.jsp").forward(request, response);
    }
}
