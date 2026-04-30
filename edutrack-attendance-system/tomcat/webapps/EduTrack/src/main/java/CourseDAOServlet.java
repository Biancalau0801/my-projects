import java.io.IOException;
import java.sql.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 1. Map the Servlet to a URL (e.g., when a user clicks 'Search')
@WebServlet("/SearchCourses")
public class CourseDAOServlet extends HttpServlet {

    // 2. We use doGet because search is typically a data-retrieval operation
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Get the search keyword from the search bar in your JSP
        String keyword = request.getParameter("searchKeyword");
        List<Map<String, String>> courseList = new ArrayList<>();

        // 3. Database Logic inside the Servlet
        // Note: I updated table/column names to match your 'timetable' table
        String sql = "SELECT * FROM timetable WHERE classCode LIKE ? OR instructor LIKE ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            // If keyword is null or empty, show all results using %
            String searchPattern = (keyword != null) ? "%" + keyword + "%" : "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, String> course = new HashMap<>();
                course.put("code", rs.getString("classCode"));
                course.put("lecturer", rs.getString("instructor"));
                // Add more fields if your table has them (e.g., courseName)
                courseList.add(course);
            }

            // 4. Send the results back to the JSP
            request.setAttribute("searchResults", courseList);
            request.getRequestDispatcher("admin_dashboard.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("admin_dashboard.jsp?status=error");
        }
    }
}