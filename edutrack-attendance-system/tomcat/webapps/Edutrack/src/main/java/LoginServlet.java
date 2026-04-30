import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String sID = request.getParameter("username"); 
        String pass = request.getParameter("password");

        try (Connection conn = DBConnection.getConnection()) {
            
            // 1. Query checks ID and Password
            String sql = "SELECT * FROM user WHERE userID = ? AND userPassword = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, sID);
            ps.setString(2, pass);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // 2. Login successful - Start Session
                HttpSession session = request.getSession();
                
                // 3. Get the role and title stored in the database
                String userRole = rs.getString("role");
                String userTitle = rs.getString("title");

                // 4. Set session attributes
                session.setAttribute("user", rs.getString("userID"));
                session.setAttribute("firstName", rs.getString("userName"));
                session.setAttribute("role", userRole);
                
                // NEW: Save the title to the session
                // If the title is NULL in DB (like for Students), we save an empty string ""
                session.setAttribute("title", (userTitle != null) ? userTitle : "");

                // 5. REDIRECT BASED ON ROLE
                if ("Lecturer".equalsIgnoreCase(userRole)) {
                    response.sendRedirect("lecturer_dashboard.jsp");
                } else if ("Admin".equalsIgnoreCase(userRole)) {
                    response.sendRedirect("AdminDashboard");
                } else if ("Programme Coordinator".equalsIgnoreCase(userRole)){
                    response.sendRedirect("coordinator-dashboard.jsp");
                } 
                else {
                    response.sendRedirect("dashboard.jsp"); 
                }
                
            } else {
                // 6. Login failed
                response.sendRedirect("index.html?error=invalid");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("index.html?error=database");
        }
    }
}