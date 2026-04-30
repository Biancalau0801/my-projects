import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // 1. Get parameters from index.html
        String fName = request.getParameter("firstName");
        String lName = request.getParameter("lastName");
        String email = request.getParameter("email");
        String sID = request.getParameter("username"); // Matches name="username"
        String pass = request.getParameter("password");
        String role = request.getParameter("role");     // NEW: Gets 'Student' or 'Lecturer'
        
        String fullName = fName + " " + lName;

        try (Connection conn = DBConnection.getConnection()) {
            String rawEmail = request.getParameter("email").trim().toLowerCase();

        // 2. Clean the email: Take only the text BEFORE the first '@'
        // This prevents "user@gmail.com@edutrack.com" issues
            String cleanHandle = rawEmail.split("@")[0];

        // 3. Append the correct university suffix
            String finalEmail = cleanHandle + "@edutrack.com";

            // 2. Updated SQL query to include the dynamic 'role'
            // Columns: userID, userName, userPassword, role
            String sql = "INSERT INTO user (userID, userName,userEmail,userPassword,role,title) VALUES (?, ?, ?, ?,?,?)";
            
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, sID);      // userID
            pstmt.setString(2, fullName); // userName
            pstmt.setString(3, finalEmail);
            pstmt.setString(4, pass);     // userPassword
            pstmt.setString(5, role);     // role (dynamically from form)

            if ("Lecturer".equalsIgnoreCase(role) || "Programme Coordinator".equalsIgnoreCase(role)) {
                pstmt.setString(6, ""); // Default empty string so it's not NULL if you prefer
            } else {
                pstmt.setNull(6, java.sql.Types.VARCHAR); // Sets actual NULL for Students/Admin
            }
            
            pstmt.executeUpdate();

            // 3. Success Redirect
            response.sendRedirect("index.html?msg=registered");
            
        } catch (Exception e) {
            e.printStackTrace();
            // Redirect with error message if database fails or user already exists
            response.sendRedirect("index.html?error=database");
        }
    }
}