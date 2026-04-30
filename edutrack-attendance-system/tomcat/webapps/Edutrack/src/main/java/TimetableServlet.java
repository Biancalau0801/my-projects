import java.io.IOException;
import java.sql.*;
import java.util.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/TimetableServlet")
public class TimetableServlet extends HttpServlet {
    
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false); 
        
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("index.html?error=session_expired");
            return;
        }

        String studentID = (String) session.getAttribute("user");
        String op = request.getParameter("operation");

        try (Connection conn = DBConnection.getConnection()) {
            
            if ("viewByDay".equals(op)) {
                Map<String, List<Map<String, String>>> weeklyMap = new LinkedHashMap<>();
                String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
                for (String day : days) { weeklyMap.put(day, new ArrayList<>()); }

                String sql = "SELECT * FROM timetable WHERE studentID = ? ORDER BY timeSlot ASC";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, studentID);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            Map<String, String> row = new HashMap<>();
                            String slot = rs.getString("timeSlot"); 
                            row.put("code", rs.getString("classCode"));
                            row.put("time", slot);
                            row.put("instructor", rs.getString("instructor"));

                            if (slot.startsWith("Mon")) weeklyMap.get("Monday").add(row);
                            else if (slot.startsWith("Tue")) weeklyMap.get("Tuesday").add(row);
                            else if (slot.startsWith("Wed")) weeklyMap.get("Wednesday").add(row);
                            else if (slot.startsWith("Thu")) weeklyMap.get("Thursday").add(row);
                            else if (slot.startsWith("Fri")) weeklyMap.get("Friday").add(row);
                        }
                    }
                }
                request.setAttribute("weeklyData", weeklyMap);
                request.getRequestDispatcher("weeklyView.jsp").forward(request, response);

            } else if ("view".equals(op) || op == null) {
                List<Map<String, String>> timetableList = new ArrayList<>();
                String sql = "SELECT t.*, u.userName as studentName FROM timetable t " +
             "JOIN user u ON t.studentID = u.userID " +
             "WHERE t.studentID = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, studentID);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            Map<String, String> row = new HashMap<>();
                            row.put("code", rs.getString("classCode"));
                            row.put("slot", rs.getString("timeSlot"));
                            String desc = rs.getString("description");
                            row.put("description", (desc != null) ? desc : "No Description");
                            row.put("duration", rs.getString("duration"));
                            row.put("instructor", rs.getString("instructor"));
                            timetableList.add(row);
                        }
                    }
                }
                request.setAttribute("timetableData", timetableList);
                request.getRequestDispatcher("timetable.jsp").forward(request, response);

            } else if ("add".equals(op)) {
                String fullCode = request.getParameter("classCode");
                String slot = request.getParameter("timeSlot");
                
                // Safety check for empty parameters (prevents the null/ABC123 issues)
                if (fullCode == null || slot == null) {
                    response.sendRedirect("TimetableServlet?operation=view&error=Invalid Course Selection");
                    return;
                }

                String baseCode = fullCode.split(" ")[0];

                // 1. Check for Duplicate Base Course (e.g., already has CS204 S1, can't add CS204 S2)
                if (hasBaseCourse(conn, studentID, baseCode)) {
                    response.sendRedirect("TimetableServlet?operation=view&error=Duplicate Enrollment: You are already enrolled in " + baseCode);
                } 
                // 2. Check for Time Conflict
                else if (hasConflict(conn, studentID, slot)) {
                    response.sendRedirect("TimetableServlet?operation=view&error=Schedule Clash: You already have a class at " + slot);
                } 
                else {
                    String sql = "INSERT INTO timetable (studentID, classCode, timeSlot, description, duration, instructor) VALUES (?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement ps = conn.prepareStatement(sql)) {
                        ps.setString(1, studentID);
                        ps.setString(2, fullCode);
                        ps.setString(3, slot);
                        ps.setString(4, request.getParameter("description"));
                        ps.setInt(5, Integer.parseInt(request.getParameter("duration")));
                        ps.setString(6, request.getParameter("instructor"));
                        ps.executeUpdate();
                    }
                    
                    logAction(conn, studentID, "🎓 Enrollment Confirmed: " + fullCode);
                    response.sendRedirect("TimetableServlet?operation=view&msg=Success! You have enrolled in " + fullCode);
                }

            } else if ("drop".equals(op)) {
                String classCode = request.getParameter("classCode");
                String sql = "DELETE FROM timetable WHERE studentID = ? AND classCode = ?";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, studentID);
                    ps.setString(2, classCode);
                    ps.executeUpdate();
                }
                
                logAction(conn, studentID, "❌ Class Dropped: " + classCode);
                response.sendRedirect("TimetableServlet?operation=view&msg=Course " + classCode + " has been removed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("TimetableServlet?operation=view&error=System Error: " + e.getMessage());
        }
    }

    private void logAction(Connection conn, String studentID, String message) throws SQLException {
        String sql = "INSERT INTO notification_logs (studentID, message) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, studentID);
            ps.setString(2, message);
            ps.executeUpdate();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    private boolean hasBaseCourse(Connection conn, String id, String baseCode) throws SQLException {
        String sql = "SELECT 1 FROM timetable WHERE studentID=? AND classCode LIKE ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, baseCode + "%");
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }

    private boolean hasConflict(Connection conn, String id, String slot) throws SQLException {
        String sql = "SELECT 1 FROM timetable WHERE studentID=? AND timeSlot=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.setString(2, slot);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }
}