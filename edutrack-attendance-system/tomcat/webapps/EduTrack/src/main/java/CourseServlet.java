import java.io.IOException;
import java.sql.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/CourseServlet")
public class CourseServlet extends HttpServlet {

    // --- Data Fields ---
    private String courseCode;
    private String lecturer;

    // --- The "Waiter" (Handles form submission) ---
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Get data from the form
        String code = request.getParameter("courseCode");
        String prof = request.getParameter("lecturer"); // Captured from dropdown

        // 2. Use Setters to update local variables
        this.setCourseCode(code);
        this.setLecturer(prof);

        // 3. Save the data to MySQL (timetable table)
        // Note: Using a standard connection utility if you have one, or direct JDBC
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/edutrackdb", "root", "bianca0801")) {
                
                String sql = "INSERT INTO timetable (classCode, instructor) VALUES (?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                
                // Using Getters here to retrieve data
                pstmt.setString(1, this.getCourseCode()); 
                pstmt.setString(2, this.getLecturer());

                pstmt.executeUpdate();
                
                // Log the assignment activity
                String logSql = "INSERT INTO activity_logs (description) VALUES (?)";
                try (PreparedStatement psLog = conn.prepareStatement(logSql)) {
                    psLog.setString(1, "Assigned " + this.getLecturer() + " to class " + this.getCourseCode());
                    psLog.executeUpdate();
                }

                response.sendRedirect("admin_dashboard.jsp?status=success");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("admin_dashboard.jsp?status=error");
        }
    }

    // --- Standard Getters and Setters ---
    public String getCourseCode() { return courseCode; }
    public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

    public String getLecturer() { return lecturer; }
    public void setLecturer(String lecturer) { this.lecturer = lecturer; }
}