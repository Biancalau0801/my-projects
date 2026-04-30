import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/RecordAttendanceServlet")
public class RecordAttendanceServlet extends HttpServlet {
    
    // MMU Cyberjaya Coordinates (Target)
    private static final double TARGET_LAT = 2.9231412; 
    private static final double TARGET_LON = 101.6346492;
    private static final double MAX_DISTANCE = 5000.0; // 10 Meters

    // Database Configuration
    private static final String DB_URL = "jdbc:mysql://localhost:3306/edutrackdb";
    private static final String DB_USER = "root"; 
    private static final String DB_PASS = "bianca0801"; // CHANGE THIS to your terminal password

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String latStr = request.getParameter("lat");
        String lonStr = request.getParameter("lon");
        String courseId = request.getParameter("courseId");
        
        // 1. Check Session to ensure student is logged in
        HttpSession session = request.getSession(false);
        String studentID = (session != null) ? (String) session.getAttribute("user") : null;
        
        if (studentID == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        // 2. Process Geolocation Data
        if (latStr != null && !latStr.isEmpty() && lonStr != null && !lonStr.isEmpty()) {
            try {
                double studentLat = Double.parseDouble(latStr);
                double studentLon = Double.parseDouble(lonStr);

                // Calculate distance using Haversine formula
                double distance = calculateHaversine(studentLat, studentLon, TARGET_LAT, TARGET_LON);

                if (distance <= MAX_DISTANCE) {
                    // SUCCESS: Save to MySQL Database
                    saveToDatabase(studentID, courseId, studentLat, studentLon);
                    
                    // REDIRECT back to the SCAN PAGE to show the Success Box
                    response.sendRedirect("scan_attendance.jsp?status=done&courseId=" + courseId);
                    return; 
                } else {
                    // FAILURE: Student is too far away
                    response.sendRedirect("scan_attendance.jsp?error=too_far&dist=" + (int)distance + "&courseId=" + courseId);
                    return;
                }
            } catch (NumberFormatException e) {
                response.sendRedirect("scan_attendance.jsp?error=invalid_coords&courseId=" + courseId);
                return;
            }
        } else {
            // Error: No coordinates received from the form
            response.sendRedirect("scan_attendance.jsp?error=missing_data&courseId=" + courseId);
            return;
        }
    }

    private double calculateHaversine(double lat1, double lon1, double lat2, double lon2) {
        double R = 6371000; // Earth's radius in meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    private void saveToDatabase(String studentID, String courseId, double lat, double lon) {
        String query = "INSERT INTO attendance (student_id, course_id, latitude, longitude, status) VALUES (?, ?, ?, ?, 'Present')";
        
        try {
            // Load MySQL Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                 PreparedStatement pstmt = conn.prepareStatement(query)) {
                
                pstmt.setString(1, studentID);
                pstmt.setString(2, courseId);
                pstmt.setDouble(3, lat);
                pstmt.setDouble(4, lon);
                
                pstmt.executeUpdate();
                System.out.println("DB LOG: Attendance recorded in MySQL for " + studentID + " [" + courseId + "]");
            }
        } catch (Exception e) {
            System.err.println("DB ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 