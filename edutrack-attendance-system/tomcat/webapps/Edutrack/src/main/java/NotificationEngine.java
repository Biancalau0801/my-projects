import java.sql.*;

public class NotificationEngine {
    
    // Main method to process all rules
    public static void processAllRules() {
        try {
            Connection conn = DBConnection.getConnection();
            
            // Get all active notification rules
            String sql = "SELECT * FROM NotificationRule";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                String ruleID = rs.getString("ruleID");
                String programmeCode = rs.getString("programmeCode");
                String thresholdType = rs.getString("thresholdType");
                double thresholdValue = rs.getDouble("thresholdValue");
                String notificationMethod = rs.getString("notificationMethod");
                
                // Check condition and trigger notifications
                if ("Attendance".equals(thresholdType)) {
                    checkAttendanceThreshold(conn, ruleID, programmeCode, thresholdValue, notificationMethod);
                } else if ("Lateness".equals(thresholdType)) {
                    checkLatenessThreshold(conn, ruleID, programmeCode, thresholdValue, notificationMethod);
                }
            }
            
            rs.close();
            stmt.close();
            conn.close();
            
            System.out.println("NotificationEngine: All rules processed successfully");
            
        } catch (Exception e) {
            System.err.println("NotificationEngine Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // Check if students are below attendance threshold
    private static void checkAttendanceThreshold(Connection conn, String ruleID, 
                                                 String programmeCode, double threshold, 
                                                 String method) throws SQLException {
        
        // Find students below threshold
        String sql = "SELECT " +
                    "s.studentID, " +
                    "u.name, " +
                    "u.email, " +
                    "(SUM(CASE WHEN ar.status='Present' THEN 1 ELSE 0 END) * 100.0 / COUNT(*)) as attendance_rate, " +
                    "COUNT(*) as total_classes, " +
                    "SUM(CASE WHEN ar.status='Absent' THEN 1 ELSE 0 END) as absent_count " +
                    "FROM Student s " +
                    "JOIN User u ON s.studentID = u.userID " +
                    "JOIN AttendanceRecord ar ON s.studentID = ar.studentID " +
                    "WHERE s.programmeCode = ? " +
                    "GROUP BY s.studentID " +
                    "HAVING attendance_rate < ?";
        
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, programmeCode);
        ps.setDouble(2, threshold);
        ResultSet rs = ps.executeQuery();
        
        int notificationsSent = 0;
        
        while (rs.next()) {
            String studentID = rs.getString("studentID");
            String name = rs.getString("name");
            double attendanceRate = rs.getDouble("attendance_rate");
            int totalClasses = rs.getInt("total_classes");
            int absentCount = rs.getInt("absent_count");
            
            String message = String.format(
                "Low Attendance Alert\n\n" +
                "Dear %s,\n\n" +
                "Your attendance rate is currently %.1f%%, which is below the required threshold of %.1f%%.\n\n" +
                "Summary:\n" +
                "- Total Classes: %d\n" +
                "- Absences: %d\n" +
                "- Current Attendance: %.1f%%\n\n" +
                "Please improve your attendance to avoid academic penalties.\n\n" +
                "Best regards,\nEduTrack System",
                name, attendanceRate, threshold, totalClasses, absentCount, attendanceRate
            );
            
            // Send notification based on method
            if ("System".equals(method)) {
                sendSystemNotification(conn, studentID, message, "Low Attendance Alert");
                notificationsSent++;
            } else if ("Email".equals(method)) {
                String email = rs.getString("email");
                sendEmailNotification(email, "Low Attendance Alert", message);
                notificationsSent++;
            }
            // SMS would go here
        }
        
        // Update lastTriggered for the rule
        if (notificationsSent > 0) {
            String updateSQL = "UPDATE NotificationRule SET lastTriggered = NOW() WHERE ruleID = ?";
            PreparedStatement updatePS = conn.prepareStatement(updateSQL);
            updatePS.setString(1, ruleID);
            updatePS.executeUpdate();
            updatePS.close();
            
            System.out.println("NotificationEngine: Sent " + notificationsSent + " notifications for rule " + ruleID);
        }
        
        rs.close();
        ps.close();
    }
    
    // Check lateness threshold (similar logic)
    private static void checkLatenessThreshold(Connection conn, String ruleID, 
                                               String programmeCode, double threshold, 
                                               String method) throws SQLException {
        
        String sql = "SELECT " +
                    "s.studentID, " +
                    "u.name, " +
                    "u.email, " +
                    "(SUM(CASE WHEN ar.status='Late' THEN 1 ELSE 0 END) * 100.0 / COUNT(*)) as late_rate, " +
                    "COUNT(*) as total_classes, " +
                    "SUM(CASE WHEN ar.status='Late' THEN 1 ELSE 0 END) as late_count " +
                    "FROM Student s " +
                    "JOIN User u ON s.studentID = u.userID " +
                    "JOIN AttendanceRecord ar ON s.studentID = ar.studentID " +
                    "WHERE s.programmeCode = ? " +
                    "GROUP BY s.studentID " +
                    "HAVING late_rate >= ?";
        
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, programmeCode);
        ps.setDouble(2, threshold);
        ResultSet rs = ps.executeQuery();
        
        int notificationsSent = 0;
        
        while (rs.next()) {
            String studentID = rs.getString("studentID");
            String name = rs.getString("name");
            double lateRate = rs.getDouble("late_rate");
            int lateCount = rs.getInt("late_count");
            
            String message = String.format(
                "High Lateness Alert\n\n" +
                "Dear %s,\n\n" +
                "You have been late to %.1f%% of your classes.\n" +
                "Total late arrivals: %d\n\n" +
                "Please ensure you arrive on time for future classes.\n\n" +
                "Best regards,\nEduTrack System",
                name, lateRate, lateCount
            );
            
            if ("System".equals(method)) {
                sendSystemNotification(conn, studentID, message, "High Lateness Alert");
                notificationsSent++;
            }
        }
        
        if (notificationsSent > 0) {
            String updateSQL = "UPDATE NotificationRule SET lastTriggered = NOW() WHERE ruleID = ?";
            PreparedStatement updatePS = conn.prepareStatement(updateSQL);
            updatePS.setString(1, ruleID);
            updatePS.executeUpdate();
            updatePS.close();
        }
        
        rs.close();
        ps.close();
    }
    
    // Send system notification (saves to database)
    private static void sendSystemNotification(Connection conn, String studentID, 
                                               String message, String title) throws SQLException {
        
        // Create notification table if it doesn't exist
        String createTableSQL = "CREATE TABLE IF NOT EXISTS Notification (" +
                               "notificationID VARCHAR(50) PRIMARY KEY, " +
                               "userID VARCHAR(50) NOT NULL, " +
                               "title VARCHAR(200), " +
                               "message TEXT NOT NULL, " +
                               "isRead BOOLEAN DEFAULT FALSE, " +
                               "createdAt DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                               "FOREIGN KEY (userID) REFERENCES User(userID))";
        
        Statement stmt = conn.createStatement();
        stmt.executeUpdate(createTableSQL);
        stmt.close();
        
        // Insert notification
        String notifID = "NOTIF" + System.currentTimeMillis() + (int)(Math.random() * 1000);
        
        String sql = "INSERT INTO Notification (notificationID, userID, title, message, isRead, createdAt) " +
                    "VALUES (?, ?, ?, ?, FALSE, NOW())";
        
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, notifID);
        ps.setString(2, studentID);
        ps.setString(3, title);
        ps.setString(4, message);
        ps.executeUpdate();
        ps.close();
        
        System.out.println("System notification sent to: " + studentID);
    }
    
    // Send email notification (placeholder - implement with JavaMail API)
    private static void sendEmailNotification(String email, String subject, String message) {
        // TODO: Implement email sending with JavaMail API
        System.out.println("Email notification sent to: " + email);
        System.out.println("Subject: " + subject);
        System.out.println("Message: " + message);
        
        // For now, just print to console
        // In production, use JavaMail API:
        /*
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("your-email@gmail.com", "your-password");
            }
        });
        
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("noreply@edutrack.com"));
        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
        msg.setSubject(subject);
        msg.setText(message);
        
        Transport.send(msg);
        */
    }
    
    // Test method - run manually to test
    public static void main(String[] args) {
        System.out.println("Starting Notification Engine Test...");
        processAllRules();
        System.out.println("Test completed!");
    }
}