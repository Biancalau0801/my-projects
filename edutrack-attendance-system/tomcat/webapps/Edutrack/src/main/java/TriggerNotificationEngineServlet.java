import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TriggerNotificationEngineServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userID") == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }
        
        try {
            // Load notification rules from database
            List<NotificationRule> rules = loadNotificationRules();
            request.setAttribute("rules", rules);
            
            // Forward to JSP
            request.getRequestDispatcher("/Coordinator/notification-rules.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error loading rules: " + e.getMessage());
            request.getRequestDispatcher("/Coordinator/notification-rules.jsp").forward(request, response);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userID") == null) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            return;
        }
        
        String action = request.getParameter("action");
        
        try {
            if ("create".equals(action)) {
                createNotificationRule(request);
                request.setAttribute("message", "Notification rule created successfully!");
            } else if ("delete".equals(action)) {
                deleteNotificationRule(request);
                request.setAttribute("message", "Notification rule deleted successfully!");
            } else if ("toggle".equals(action)) {
                toggleNotificationRule(request);
                request.setAttribute("message", "Notification rule status updated!");
            }
            
            // Reload rules after action
            List<NotificationRule> rules = loadNotificationRules();
            request.setAttribute("rules", rules);
            
            // Forward back to JSP
            request.getRequestDispatcher("/Coordinator/notification-rules.jsp").forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error: " + e.getMessage());
            
            // Still try to load rules even if action failed
            try {
                List<NotificationRule> rules = loadNotificationRules();
                request.setAttribute("rules", rules);
            } catch (Exception ex) {
                // Ignore
            }
            
            request.getRequestDispatcher("/Coordinator/notification-rules.jsp").forward(request, response);
        }
    }
    
    private List<NotificationRule> loadNotificationRules() throws SQLException {
        List<NotificationRule> rules = new ArrayList<>();
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            
            // Check if NotificationRule table exists, if not return empty list
            String sql = "SELECT * FROM NotificationRule ORDER BY createdAt DESC";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                NotificationRule rule = new NotificationRule();
                rule.setRuleID(rs.getString("ruleID"));
                rule.setRuleName(rs.getString("ruleName"));
                rule.setCondition(rs.getString("condition"));
                rule.setMessage(rs.getString("message"));
                rule.setIsActive(rs.getBoolean("isActive"));
                rule.setCreatedAt(rs.getTimestamp("createdAt"));
                rules.add(rule);
            }
            
        } catch (SQLException | ClassNotFoundException e){
            // If table doesn't exist, return empty list
            System.out.println("Warning: NotificationRule table may not exist - " + e.getMessage());
        } finally {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            if (conn != null) conn.close();
        }
        
        return rules;
    }
    
    private void createNotificationRule(HttpServletRequest request) 
        throws SQLException, ClassNotFoundException { // Added ClassNotFoundException
    String ruleName = request.getParameter("ruleName");
    String condition = request.getParameter("condition");
    String message = request.getParameter("message");
    
    Connection conn = null;
    PreparedStatement ps = null;
    
    try {
        conn = DBConnection.getConnection();
        String ruleID = "RULE" + System.currentTimeMillis();
        String sql = "INSERT INTO NotificationRule (ruleID, ruleName, `condition`, message, isActive, createdAt) VALUES (?, ?, ?, ?, ?, NOW())";
        
        ps = conn.prepareStatement(sql);
        ps.setString(1, ruleID);
        ps.setString(2, ruleName);
        ps.setString(3, condition);
        ps.setString(4, message);
        ps.setBoolean(5, true);
        ps.executeUpdate();
    } finally {
        if (ps != null) ps.close();
        if (conn != null) conn.close();
    }
}
    
    private void deleteNotificationRule(HttpServletRequest request) 
        throws SQLException, ClassNotFoundException { // Added ClassNotFoundException
    String ruleID = request.getParameter("ruleID");
    Connection conn = null;
    PreparedStatement ps = null;
    
    try {
        conn = DBConnection.getConnection();
        String sql = "DELETE FROM NotificationRule WHERE ruleID = ?";
        ps = conn.prepareStatement(sql);
        ps.setString(1, ruleID);
        ps.executeUpdate();
    } finally {
        if (ps != null) ps.close();
        if (conn != null) conn.close();
    }
}

// 3. Fixed toggleNotificationRule
private void toggleNotificationRule(HttpServletRequest request) 
        throws SQLException, ClassNotFoundException { // Added ClassNotFoundException
    String ruleID = request.getParameter("ruleID");
    Connection conn = null;
    PreparedStatement ps = null;
    
    try {
        conn = DBConnection.getConnection();
        String sql = "UPDATE NotificationRule SET isActive = NOT isActive WHERE ruleID = ?";
        ps = conn.prepareStatement(sql);
        ps.setString(1, ruleID);
        ps.executeUpdate();
    } finally {
        if (ps != null) ps.close();
        if (conn != null) conn.close();
    }
}
    
    // Inner class for notification rule data
    public static class NotificationRule {
        private String ruleID;
        private String ruleName;
        private String condition;
        private String message;
        private boolean isActive;
        private Timestamp createdAt;
        
        // Getters and setters
        public String getRuleID() { return ruleID; }
        public void setRuleID(String ruleID) { this.ruleID = ruleID; }
        
        public String getRuleName() { return ruleName; }
        public void setRuleName(String ruleName) { this.ruleName = ruleName; }
        
        public String getCondition() { return condition; }
        public void setCondition(String condition) { this.condition = condition; }
        
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        
        public boolean getIsActive() { return isActive; }
        public void setIsActive(boolean isActive) { this.isActive = isActive; }
        
        public Timestamp getCreatedAt() { return createdAt; }
        public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }
    }
}