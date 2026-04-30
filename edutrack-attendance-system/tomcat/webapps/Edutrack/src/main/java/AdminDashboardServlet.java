import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/AdminDashboard")
public class AdminDashboardServlet extends HttpServlet {
    
    private static final String DB_URL = "jdbc:mysql://localhost:3306/edutrackdb";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "bianca0801";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        List<Map<String, Object>> logList = new ArrayList<>();
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
                Statement stmt = conn.createStatement();

                // 1. Fetch Statistics for the Dashboard Cards
                // Total Users
                ResultSet rsTotal = stmt.executeQuery("SELECT COUNT(*) FROM user");
                if (rsTotal.next()) request.setAttribute("totalUsers", rsTotal.getInt(1));

                // Total Students
                ResultSet rsStudents = stmt.executeQuery("SELECT COUNT(*) FROM user WHERE role = 'Student'");
                if (rsStudents.next()) request.setAttribute("studentCount", rsStudents.getInt(1));

                // Total Lecturers 
                ResultSet rsLecturers = stmt.executeQuery("SELECT COUNT(*) FROM user WHERE role = 'Lecturer'");
                if (rsLecturers.next()) request.setAttribute("lecturerCount", rsLecturers.getInt(1));

                // Active Classes
                ResultSet rsCourses = stmt.executeQuery("SELECT COUNT(DISTINCT course_code) FROM courses");
                if (rsCourses.next()) request.setAttribute("activeCourses", rsCourses.getInt(1));

                // 2. Fetch the latest Activity Logs using the ActivityLog model
                String sqlLogs = "SELECT description, timestamp FROM activity_logs ORDER BY timestamp DESC LIMIT 10";
                PreparedStatement ps = conn.prepareStatement(sqlLogs);
                ResultSet rsLogs = ps.executeQuery();

                while (rsLogs.next()) {
                    Map<String, Object> log = new HashMap<>();
                    log.put("description", rsLogs.getString("description"));
                    log.put("timestamp", rsLogs.getTimestamp("timestamp"));
                    logList.add(log);
                }
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }

        // Set attributes for the JSP
        request.setAttribute("recentLogs", logList);
        request.getRequestDispatcher("admin_dashboard.jsp").forward(request, response);
    }
}
