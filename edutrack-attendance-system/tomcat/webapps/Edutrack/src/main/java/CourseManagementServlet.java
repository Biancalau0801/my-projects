import java.io.IOException;
import java.sql.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/CourseManagement")
public class CourseManagementServlet extends HttpServlet {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/edutrackdb";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "bianca0801";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Map<String, String>> courses = new ArrayList<>();
        List<String> lecturers = new ArrayList<>();

        // 🔧 FIX 1: parameter name must match JSP input name
        String search = request.getParameter("search");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {

                /* =========================
                   DELETE COURSE LOGIC
                   ========================= */
                String action = request.getParameter("action");
                if ("delete".equals(action)) {
                    String deleteId = request.getParameter("id");
                    try (PreparedStatement psDel =
                            conn.prepareStatement("DELETE FROM courses WHERE id = ?")) {

                        psDel.setString(1, deleteId);
                        if (psDel.executeUpdate() > 0) {
                            logActivity(conn, "Admin deleted course with ID: " + deleteId);
                        }
                        response.sendRedirect("CourseManagement?message=Deleted");
                        return;
                    }
                }

                /* =========================
                   FETCH LECTURERS (Dropdown)
                   ========================= */
                String lecSql =
                        "SELECT userName FROM user WHERE role = 'Lecturer' AND status = 'Active'";
                try (Statement stmt = conn.createStatement();
                     ResultSet rsLec = stmt.executeQuery(lecSql)) {

                    while (rsLec.next()) {
                        lecturers.add(rsLec.getString("userName"));
                    }
                }

                /* =========================
                   FETCH COURSES + SEARCH + SORT
                   ========================= */
                String sql;
                PreparedStatement ps;

                if (search != null && !search.trim().isEmpty()) {
                    // Search by course name or course code
                    // ORDER BY course_code ensures alphanumeric sorting
                    sql = "SELECT * FROM courses " +
                          "WHERE course_name LIKE ? OR course_code LIKE ? " +
                          "ORDER BY course_code ASC";

                    ps = conn.prepareStatement(sql);
                    String keyword = "%" + search + "%";
                    ps.setString(1, keyword);
                    ps.setString(2, keyword);
                } else {
                    // Default: fetch all courses sorted by course code
                    sql = "SELECT * FROM courses ORDER BY course_code ASC";
                    ps = conn.prepareStatement(sql);
                }

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Map<String, String> c = new HashMap<>();
                        c.put("id", rs.getString("id"));
                        c.put("courseCode", rs.getString("course_code"));
                        c.put("courseName", rs.getString("course_name"));
                        c.put("credits", rs.getString("credits"));
                        c.put("lecturer", rs.getString("lecturer"));
                        courses.add(c);
                    }
                }
            }

            // Pass data to JSP
            request.setAttribute("courses", courses);
            request.setAttribute("lecturerList", lecturers);
            request.getRequestDispatcher("course_management.jsp")
                   .forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("CourseManagement?message=Error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id");
        String code = request.getParameter("courseCode");
        String name = request.getParameter("courseName");
        String credits = request.getParameter("credits");
        String lecturer = request.getParameter("lecturer");

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {

            boolean isNew = (id == null || id.isEmpty());

            String sql = isNew
                    ? "INSERT INTO courses (course_code, course_name, credits, lecturer) VALUES (?, ?, ?, ?)"
                    : "UPDATE courses SET course_code=?, course_name=?, credits=?, lecturer=? WHERE id=?";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, code);
                ps.setString(2, name);
                ps.setString(3, credits);
                ps.setString(4, lecturer);
                if (!isNew) ps.setString(5, id);
                ps.executeUpdate();

                logActivity(conn,
                        isNew ? "Admin added course: " + code
                              : "Admin updated course: " + code);
            }

            response.sendRedirect("CourseManagement?message=Success");

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("CourseManagement?message=Error");
        }
    }

    /* =========================
       ACTIVITY LOGGING
       ========================= */
    private void logActivity(Connection conn, String desc) throws SQLException {
        String sql = "INSERT INTO activity_logs (description) VALUES (?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, desc);
            ps.executeUpdate();
        }
    }
}
