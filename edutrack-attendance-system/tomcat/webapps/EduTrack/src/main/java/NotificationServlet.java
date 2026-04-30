import java.io.IOException;
import java.sql.*;
import java.util.*; // Added for Map and HashMap
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/NotificationServlet")
public class NotificationServlet extends HttpServlet {
    
    private static final String DB_URL = "jdbc:mysql://localhost:3306/edutrackdb";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "bianca0801";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        String studentID = (String) session.getAttribute("user");
        
        // Use a List of Maps to store message + time pairs
        List<Map<String, String>> notifications = new ArrayList<>();
        
        if (studentID == null) {
            response.sendRedirect("index.html?error=session_expired");
            return; // Stop execution if no session
        }
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

            // 1. MARK AS READ (Updates the badge tracker timestamp)
            String updateStatusSql = "INSERT INTO notification_read_status (studentID, last_checked) " +
                                     "VALUES (?, NOW()) ON DUPLICATE KEY UPDATE last_checked = NOW()";
            try (PreparedStatement psUpdate = conn.prepareStatement(updateStatusSql)) {
                psUpdate.setString(1, studentID);
                psUpdate.executeUpdate();
            }

            // 2. FETCH LOGGED NOTIFICATIONS (Retrieves history + actual time)
            // This query reads from the notification_logs table you created
            String logQuery = "SELECT message, created_at FROM notification_logs WHERE studentID = ? ORDER BY created_at DESC";
            try (PreparedStatement ps = conn.prepareStatement(logQuery)) {
                ps.setString(1, studentID);
                ResultSet rs = ps.executeQuery();
                
                while(rs.next()) {
                    Map<String, String> notif = new HashMap<>();
                    notif.put("message", rs.getString("message"));
                    // Format the timestamp to a readable string
                    notif.put("time", rs.getTimestamp("created_at").toString()); 
                    notifications.add(notif);
                }
            }

            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            // Optional: Add a map entry to tell the user the DB is down
            Map<String, String> errorNotif = new HashMap<>();
            errorNotif.put("message", "Note: System could not reach database history.");
            errorNotif.put("time", "");
            notifications.add(errorNotif);
        }

        // Send the map list to notification.jsp
        request.setAttribute("notifList", notifications);
        request.getRequestDispatcher("notification.jsp").forward(request, response);
    }
}