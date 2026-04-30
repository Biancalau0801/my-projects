import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.util.*;

@WebServlet("/AttendanceServlet_lecture")
public class AttendanceServlet_lecture extends HttpServlet {
    
    private Connection getDirectConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        // Ensure your DB name and credentials match your local setup
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/edutrackdb", "root", "bianca0801");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        HttpSession session = request.getSession(false);
        
        // --- Standardized to 'firstName' as per your LoginServlet ---
        String lecturerID = (session != null) ? (String) session.getAttribute("user") : null;
        String lecturerName = (session != null) ? (String) session.getAttribute("firstName") : null; 
        
        String action = request.getParameter("action");
        String selectedClass = request.getParameter("classCode");

        // Default action if none is provided
        if (action == null) action = "selectClass";

        out.println("<html><head><title>EduTrack Attendance</title><style>");
        out.println("body{font-family:'Segoe UI',sans-serif; background:#f0f4f8; padding:40px; color:#2d3748;}");
        out.println(".card{background:white; padding:30px; border-radius:15px; box-shadow:0 10px 25px rgba(0,0,0,0.05); max-width:850px; margin:auto;}");
        out.println(".class-link{display:block; padding:18px; background:#f1f5f9; color:#1e293b; text-decoration:none; margin-bottom:12px; border-radius:10px; font-weight:bold; border-left:5px solid #3b82f6; transition: 0.2s;}");
        out.println(".class-link:hover{background:#e2e8f0; transform: translateX(5px);}");
        out.println("table{width:100%; border-collapse:collapse; margin:20px 0;} th,td{padding:14px; border-bottom:1px solid #edf2f7; text-align:left;}");
        out.println(".btn-save{background:#10b981; color:white; border:none; padding:12px 25px; border-radius:8px; cursor:pointer; font-weight:bold;}");
        out.println(".back-nav{display:inline-block; margin-bottom:20px; text-decoration:none; color:#64748b; font-weight:bold;}");
        out.println(".badge-risk{background:#fee2e2; color:#ef4444; padding:4px 8px; border-radius:5px; font-size:12px;}");
        out.println(".badge-good{background:#dcfce7; color:#16a34a; padding:4px 8px; border-radius:5px; font-size:12px;}");
        out.println("</style>");
        out.println("<script>function toggleAll(source){checkboxes=document.getElementsByName('studentIDs');for(var i=0;i<checkboxes.length;i++)checkboxes[i].checked=source.checked;}</script>");
        out.println("</head><body><div class='card'>");

        try (Connection conn = getDirectConnection()) { 
            
            // --- 1. CLASS SELECTION UI ---
            // Handles both 'selectClass' and 'vieselectClassAnalytics' from your dashboard
            if ("selectClass".equals(action) || "vieselectClassAnalytics".equals(action)) {
                out.println("<a href='lecturer_dashboard.jsp' class='back-nav'>&larr; Back to Dashboard</a>");
                
                String title = "vieselectClassAnalytics".equals(action) ? "Attendance Analysis" : "Manual Attendance";
                out.println("<h2>" + title + "</h2><p>Logged in as: <b>" + lecturerName + "</b></p>");

                // --- SMART SQL: Matches name from User table using the Staff ID ABC123 ---
                // This SUBSTRING_INDEX trick grabs the last 2 parts of the name (e.g., 'Zi Kin') to find in timetable
                String sql = "SELECT DISTINCT classCode FROM timetable WHERE instructor LIKE " +
                             "(SELECT CONCAT('%', SUBSTRING_INDEX(userName, ' ', -2), '%') FROM user WHERE userID = ?)";

                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, lecturerID); 
                    ResultSet rs = ps.executeQuery();
                    
                    boolean found = false;
                    while(rs.next()) {
                        found = true;
                        String code = rs.getString("classCode");
                        // Decide where the link goes based on the current action
                        String nextAct = "vieselectClassAnalytics".equals(action) ? "view" : "manual";
                        out.println("<a href='AttendanceServlet_lecture?action=" + nextAct + "&classCode=" + 
                                     java.net.URLEncoder.encode(code, "UTF-8") + "' class='class-link'>📘 " + code + "</a>");
                    }
                    if (!found) {
                        out.println("<p style='color:#ef4444;'>No classes assigned to ID: " + lecturerID + " in the timetable.</p>");
                    }
                }
            } 
            
            // --- 2. MANUAL MARKING UI ---
            else if ("manual".equals(action)) {
                showManualMarking(out, conn, selectedClass, request);
            }

            // --- 3. ANALYTICS VIEW ---
            else if ("view".equals(action)) {
                showAnalytics(out, conn, selectedClass);
            }

        } catch (Exception e) { 
            out.println("<div style='color:red;'><b>System Error:</b> " + e.getMessage() + "</div>");
            e.printStackTrace();
        }
        out.println("</div></body></html>");
    }

    private void showManualMarking(PrintWriter out, Connection conn, String selectedClass, HttpServletRequest request) throws SQLException {
        String attDate = request.getParameter("attDate");
        if (attDate == null) attDate = new java.sql.Date(System.currentTimeMillis()).toString();

        out.println("<a href='AttendanceServlet_lecture?action=selectClass' class='back-nav'>&larr; Change Class</a>");
        out.println("<h2>Marking Attendance: " + selectedClass + "</h2>");
        out.println("<form method='GET' style='margin-bottom:20px;'><input type='hidden' name='action' value='manual'><input type='hidden' name='classCode' value='"+selectedClass+"'>");
        out.println("Select Date: <input type='date' name='attDate' value='"+attDate+"' onchange='this.form.submit()' style='padding:8px; border-radius:5px; border:1px solid #cbd5e1;'></form>");

        out.println("<form action='AttendanceServlet_lecture' method='POST'>");
        out.println("<input type='hidden' name='classCode' value='"+selectedClass+"'><input type='hidden' name='attDate' value='"+attDate+"'>");
        
        String sql = "SELECT t.studentID, u.userName, (SELECT status FROM attendance WHERE studentID = t.studentID AND classCode = t.classCode AND attendance_date = ?) as status " +
                     "FROM timetable t JOIN user u ON t.studentID = u.userID WHERE t.classCode = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, attDate);
            ps.setString(2, selectedClass);
            ResultSet rs = ps.executeQuery();
            out.println("<table><tr><th><input type='checkbox' onclick='toggleAll(this)'></th><th>Student ID</th><th>Student Name</th></tr>");
            while(rs.next()) {
                String sid = rs.getString("studentID");
                String checked = "Present".equals(rs.getString("status")) ? "checked" : "";
                out.println("<tr><td><input type='checkbox' name='studentIDs' value='"+sid+"' "+checked+"></td><td>"+sid+"</td><td>"+rs.getString("userName")+"</td></tr>");
            }
            out.println("</table><button type='submit' class='btn-save'>Update Attendance Records</button></form>");
        }
    }

    private void showAnalytics(PrintWriter out, Connection conn, String selectedClass) throws SQLException {
        out.println("<a href='AttendanceServlet_lecture?action=vieselectClassAnalytics' class='back-nav'>&larr; Change Class</a>");
        out.println("<h2>Attendance Summary: " + selectedClass + "</h2>");
        
        String sql = "SELECT u.userID, u.userName, COUNT(CASE WHEN a.status = 'Present' THEN 1 END) as present_days, " +
                     "(SELECT COUNT(DISTINCT attendance_date) FROM attendance WHERE classCode = ?) as total_days " +
                     "FROM user u JOIN timetable t ON u.userID = t.studentID LEFT JOIN attendance a ON t.studentID = a.studentID AND a.classCode = t.classCode " +
                     "WHERE t.classCode = ? GROUP BY u.userID, u.userName";
                     
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, selectedClass);
            ps.setString(2, selectedClass);
            ResultSet rs = ps.executeQuery();
            out.println("<table><tr><th>ID</th><th>Student Name</th><th>Percentage</th><th>Status</th></tr>");
            while(rs.next()) {
                int total = rs.getInt("total_days");
                double pct = total > 0 ? (rs.getDouble("present_days") / total) * 100 : 0;
                String statusBadge = (pct >= 80) ? "<span class='badge-good'>Good</span>" : "<span class='badge-risk'>At Risk</span>";
                out.println("<tr><td>"+rs.getString("userID")+"</td><td>"+rs.getString("userName")+"</td><td>"+String.format("%.1f%%", pct)+"</td><td>"+statusBadge+"</td></tr>");
            }
            out.println("</table>");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String classCode = request.getParameter("classCode");
        String attDate = request.getParameter("attDate");
        String[] ids = request.getParameterValues("studentIDs");
        
        try (Connection conn = getDirectConnection()) {
            conn.setAutoCommit(false);
            // Clear existing for that day to prevent duplicates
            PreparedStatement dps = conn.prepareStatement("DELETE FROM attendance WHERE classCode = ? AND attendance_date = ?");
            dps.setString(1, classCode); dps.setString(2, attDate); dps.executeUpdate();

            if (ids != null) {
                PreparedStatement ips = conn.prepareStatement("INSERT INTO attendance (studentID, status, classCode, attendance_date) VALUES (?, 'Present', ?, ?)");
                for (String id : ids) {
                    ips.setString(1, id); ips.setString(2, classCode); ips.setString(3, attDate); ips.addBatch();
                }
                ips.executeBatch();
            }
            conn.commit();
            response.setContentType("text/html");
            response.getWriter().println("<script>alert('Attendance successfully recorded!'); window.location='AttendanceServlet_lecture?action=manual&classCode=" + java.net.URLEncoder.encode(classCode, "UTF-8") + "&attDate=" + attDate + "';</script>");
        } catch (Exception e) { response.getWriter().println("Error saving: " + e.getMessage()); }
    }
}