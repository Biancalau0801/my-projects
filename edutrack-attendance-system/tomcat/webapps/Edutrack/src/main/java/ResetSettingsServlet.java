import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.ServletContext;

@WebServlet("/ResetSettingsServlet")
public class ResetSettingsServlet extends HttpServlet {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/edutrackdb";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "bianca0801";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ServletContext context = getServletContext();
        context.setAttribute("lateThreshold", "15");
        context.setAttribute("attendanceAlert", "80");
        context.setAttribute("sessionTimeout", "30");
        context.setAttribute("enableActivityLog", "false"); // reset

        request.setAttribute("message", "System settings reset to default!");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {

                String description =
                        "Admin reset system settings to default values";

                String sql = "INSERT INTO activity_logs (description) VALUES (?)";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, description);
                    ps.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace(); 
        }

        request.getRequestDispatcher("/system_settings.jsp")
               .forward(request, response);
    }
}
