import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import javax.servlet.ServletContext;

@WebServlet("/SystemSettingsServlet")
public class SystemSettingsServlet extends HttpServlet {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/edutrackdb";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "bianca0801";

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String lateThreshold = request.getParameter("lateThreshold");
        String attendanceAlert = request.getParameter("attendanceAlert");
        String sessionTimeout = request.getParameter("sessionTimeout");

        // Checkbox handling
        String enableActivityLog = request.getParameter("enableActivityLog");
        enableActivityLog = (enableActivityLog != null) ? "true" : "false";

        ServletContext context = getServletContext();
        context.setAttribute("lateThreshold", lateThreshold);
        context.setAttribute("attendanceAlert", attendanceAlert);
        context.setAttribute("sessionTimeout", sessionTimeout);
        context.setAttribute("enableActivityLog", enableActivityLog);

        request.setAttribute("message", "Settings updated successfully!");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {

                String description =
                        "Admin updated system settings: "
                      + "LateThreshold=" + lateThreshold
                      + ", AttendanceAlert=" + attendanceAlert
                      + ", SessionTimeout=" + sessionTimeout;
                      

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
