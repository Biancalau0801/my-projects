import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import javax.servlet.annotation.WebServlet; 
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("NotificationRulesServlet")
public class NotificationRulesServlet extends HttpServlet {
    
    // Get all rules (GET request)
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
        throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            Connection conn = DBConnection.getConnection();
            
            String sql = "SELECT * FROM NotificationRule ORDER BY ruleID DESC";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            
            StringBuilder json = new StringBuilder("[");
            boolean first = true;
            
            while (rs.next()) {
                if (!first) json.append(",");
                
                json.append("{")
                    .append("\"ruleID\":\"").append(rs.getString("ruleID")).append("\",")
                    .append("\"programmeCode\":\"").append(rs.getString("programmeCode")).append("\",")
                    .append("\"thresholdType\":\"").append(rs.getString("thresholdType")).append("\",")
                    .append("\"thresholdValue\":").append(rs.getDouble("thresholdValue")).append(",")
                    .append("\"notificationMethod\":\"").append(rs.getString("notificationMethod")).append("\",")
                    .append("\"frequency\":\"").append(rs.getString("frequency")).append("\",")
                    .append("\"lastTriggered\":\"").append(rs.getString("lastTriggered") != null ? rs.getString("lastTriggered") : "Never").append("\"")
                    .append("}");
                
                first = false;
            }
            json.append("]");
            
            out.print(json.toString());
            
            rs.close();
            stmt.close();
            conn.close();
            
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
    
    // Create new rule (POST request)
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String action = request.getParameter("action");
        
        if ("create".equals(action)) {
            createRule(request, response);
        } else if ("delete".equals(action)) {
            deleteRule(request, response);
        } else if ("update".equals(action)) {
            updateRule(request, response);
        }
    }
    
    private void createRule(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        String programmeCode = request.getParameter("programmeCode");
        String thresholdType = request.getParameter("thresholdType");
        String thresholdValue = request.getParameter("thresholdValue");
        String notificationMethod = request.getParameter("notificationMethod");
        String frequency = request.getParameter("frequency");
        String recipientList = request.getParameter("recipientList");
        
        try {
            Connection conn = DBConnection.getConnection();
            
            String ruleID = "RULE" + System.currentTimeMillis();
            
            String sql = "INSERT INTO NotificationRule " +
                        "(ruleID, programmeCode, thresholdType, thresholdValue, recipientList, " +
                        "notificationMethod, frequency) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)";
            
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ruleID);
            ps.setString(2, programmeCode);
            ps.setString(3, thresholdType);
            ps.setDouble(4, Double.parseDouble(thresholdValue));
            ps.setString(5, recipientList != null ? recipientList : "[]");
            ps.setString(6, notificationMethod);
            ps.setString(7, frequency);
            
            int result = ps.executeUpdate();
            
            ps.close();
            conn.close();
            
            if (result > 0) {
                response.sendRedirect("notification-rules.jsp?success=created");
            } else {
                response.sendRedirect("notification-rules.jsp?error=failed");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("notification-rules.jsp?error=" + e.getMessage());
        }
    }
    
    private void deleteRule(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        String ruleID = request.getParameter("ruleID");
        
        try {
            Connection conn = DBConnection.getConnection();
            
            String sql = "DELETE FROM NotificationRule WHERE ruleID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ruleID);
            
            int result = ps.executeUpdate();
            
            ps.close();
            conn.close();
            
            if (result > 0) {
                response.sendRedirect("notification-rules.jsp?success=deleted");
            } else {
                response.sendRedirect("notification-rules.jsp?error=notfound");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("notification-rules.jsp?error=" + e.getMessage());
        }
    }
    
    private void updateRule(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        String ruleID = request.getParameter("ruleID");
        String thresholdValue = request.getParameter("thresholdValue");
        String frequency = request.getParameter("frequency");
        
        try {
            Connection conn = DBConnection.getConnection();
            
            String sql = "UPDATE NotificationRule SET thresholdValue = ?, frequency = ? WHERE ruleID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setDouble(1, Double.parseDouble(thresholdValue));
            ps.setString(2, frequency);
            ps.setString(3, ruleID);
            
            int result = ps.executeUpdate();
            
            ps.close();
            conn.close();
            
            if (result > 0) {
                response.sendRedirect("notification-rules.jsp?success=updated");
            } else {
                response.sendRedirect("notification-rules.jsp?error=notfound");
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("notification-rules.jsp?error=" + e.getMessage());
        }
    }
}