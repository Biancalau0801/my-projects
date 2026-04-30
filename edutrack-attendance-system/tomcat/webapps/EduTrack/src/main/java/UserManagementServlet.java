import java.io.IOException;
import java.sql.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;

public class UserManagementServlet extends HttpServlet {
    
    private static final String DB_URL = "jdbc:mysql://localhost:3306/edutrackdb";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "bianca0801";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        if ("delete".equals(action)) {

            deleteUser(request, response);
            return;
        }

        String searchTerm = request.getParameter("search");
        List<Map<String, String>> users = new ArrayList<>();
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
                String sql;
                PreparedStatement ps;

                if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                    sql = "SELECT * FROM user WHERE userID LIKE ? OR userName LIKE ? ORDER BY userName ASC";
                    ps = conn.prepareStatement(sql);
                    ps.setString(1, "%" + searchTerm + "%");
                    ps.setString(2, "%" + searchTerm + "%");
                } else {
                    sql = "SELECT * FROM user ORDER BY userID ASC";
                    ps = conn.prepareStatement(sql);
                }

                ResultSet rs = ps.executeQuery();
                while(rs.next()){
                    Map<String, String> u = new HashMap<>();
                    u.put("id", rs.getString("userID"));
                    u.put("name", rs.getString("userName"));
                    u.put("email", rs.getString("userEmail"));
                    u.put("role", rs.getString("role"));
                    u.put("status", rs.getString("status")); 
                    u.put("title", rs.getString("title")); 
                    users.add(u);
                }
            }
            request.setAttribute("userList", users);
            request.getRequestDispatcher("user_management.jsp").forward(request, response);
        } catch (Exception e) { 
            e.printStackTrace();
            throw new ServletException(e); 
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String oldId = request.getParameter("id");  // hidden field, original ID
        String newId = request.getParameter("displayId"); // input field, new ID


        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String role = request.getParameter("role");
        String status = request.getParameter("status"); 
        String title = request.getParameter("title");

        // Email validation
        if (email == null || !email.toLowerCase().endsWith("@edutrack.com")) {
            response.sendRedirect("UserManagement?message=Error: Email must end with @edutrack.com");
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
                

                if (oldId != null && !oldId.isEmpty() && !oldId.equals(newId)) {
                    PreparedStatement psDel = conn.prepareStatement("DELETE FROM user WHERE userID = ?");
                    psDel.setString(1, oldId);
                    psDel.executeUpdate();
               }

                // 1. SAVE/UPDATE USER
                String sqlUser = "INSERT INTO user (userID, userName, userEmail, userPassword, role, title, status) " +
                             "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                             "ON DUPLICATE KEY UPDATE userName=?, userEmail=?, role=?, title=?, status=?";
                
                PreparedStatement psUser = conn.prepareStatement(sqlUser);
                psUser.setString(1, newId);
                psUser.setString(2, name);
                psUser.setString(3, email);
                psUser.setString(4, "123456"); 
                psUser.setString(5, role);
                psUser.setString(6, title); 
                psUser.setString(7, status);
                
                psUser.setString(8, name);
                psUser.setString(9, email);
                psUser.setString(10, role);
                psUser.setString(11, title);
                psUser.setString(12, status);
                
                psUser.executeUpdate();

                // 2. INSERT INTO ACTIVITY LOG
                String logDescription = "Admin saved/updated user: " + name + " (ID: " + newId + ")";
                String sqlLog = "INSERT INTO activity_logs (description) VALUES (?)";
                
                try (PreparedStatement psLog = conn.prepareStatement(sqlLog)) {
                    psLog.setString(1, logDescription);
                    psLog.executeUpdate();
                }
            }
            response.sendRedirect("UserManagement?message=User " + newId + " Saved Successfully");
        } catch (Exception e) { 
            e.printStackTrace();
            response.sendRedirect("UserManagement?message=Error: " + e.getMessage());
        }
    }

    private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String id = request.getParameter("id");
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
                
                // 1. Delete the user
                PreparedStatement ps = conn.prepareStatement("DELETE FROM user WHERE userID = ?");
                ps.setString(1, id);
                ps.executeUpdate();
                
                // 2. Log the deletion activity
                String logDescription = "Admin deleted user ID: " + id;
                String sqlLog = "INSERT INTO activity_logs (description) VALUES (?)";
                
                try (PreparedStatement psLog = conn.prepareStatement(sqlLog)) {
                    psLog.setString(1, logDescription);
                    psLog.executeUpdate();
                }

                response.sendRedirect("UserManagement?message=User " + id + " Deleted");
            }
        } catch (Exception e) { 
            e.printStackTrace();
            response.sendRedirect("UserManagement?message=Delete Failed: Row is linked to other data.");
        }
    }
}