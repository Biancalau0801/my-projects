import java.io.IOException;
import java.sql.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/AttendanceServlet")
public class AttendanceServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        String studentID = (session != null) ? (String) session.getAttribute("user") : null;

        if (studentID == null) {
            response.sendRedirect("index.html");
            return;
        }

        List<Map<String, Object>> courseReports = new ArrayList<>();
        
        // Variables for Overall Stats
        int totalClassesAcrossAll = 0;
        int totalAttendedAcrossAll = 0;

        try (Connection conn = DBConnection.getConnection()) {
            // Main query to get individual course data
            String sql = "SELECT a.classCode, c.course_name, " +
             "SUM(CASE WHEN a.status = 'Present' THEN 1 ELSE 0 END) as attended, " +
             "(SELECT COUNT(DISTINCT attendance_date) FROM attendance WHERE classCode = a.classCode) as total_held " +
             "FROM attendance a " +
             "JOIN courses c ON a.classCode = c.course_code " + 
             "WHERE a.studentID = ? GROUP BY a.classCode, c.course_name";
             
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, studentID);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, Object> map = new HashMap<>();
                int total = rs.getInt("total_held");
                int attended = rs.getInt("attended");
                
                // Accumulate totals for the summary stats
                totalClassesAcrossAll += total;
                totalAttendedAcrossAll += attended;

                double pct = (total > 0) ? ((double) attended / total) * 100 : 0;

                map.put("className", rs.getString("classCode"));
                map.put("total", total);
                map.put("present", attended);
                map.put("pct", String.format("%.1f", pct));
                
                // Logic for "Trend" (Simulated since DB is simple, but useful for UI)
                map.put("trend", pct >= 80 ? "up" : "down"); 
                
                courseReports.add(map);
            }

            // Calculate Overall Percentage
            double overallPct = (totalClassesAcrossAll > 0) 
                ? ((double) totalAttendedAcrossAll / totalClassesAcrossAll) * 100 : 0;

            // Send EVERYTHING to the JSP
            request.setAttribute("courseReports", courseReports);
            request.setAttribute("totalAttended", totalAttendedAcrossAll);
            request.setAttribute("totalMissed", totalClassesAcrossAll - totalAttendedAcrossAll);
            request.setAttribute("overallPct", String.format("%.1f", overallPct));
            request.setAttribute("totalCourses", courseReports.size());

            request.getRequestDispatcher("AttendanceReport.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500);
        }
    }
}