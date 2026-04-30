import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
@WebServlet("/CoordinatorDashboardServlet")
public class CoordinatorDashboardServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        
        try {
            Connection conn = DBConnection.getConnection();
            
            // Total active students
            String sql1 = "SELECT COUNT(*) as total FROM Student WHERE enrollmentStatus='Active'";
            Statement stmt1 = conn.createStatement();
            ResultSet rs1 = stmt1.executeQuery(sql1);
            int totalStudents = 0;
            if(rs1.next()) {
                totalStudents = rs1.getInt("total");
            }
            
            // Average attendance rate
            String sql2 = "SELECT " +
                         "(SUM(CASE WHEN status='Present' THEN 1 ELSE 0 END) * 100.0 / COUNT(*)) as avg " +
                         "FROM AttendanceRecord";
            Statement stmt2 = conn.createStatement();
            ResultSet rs2 = stmt2.executeQuery(sql2);
            double avgAttendance = 0;
            if(rs2.next()) {
                avgAttendance = rs2.getDouble("avg");
            }
            
            // At-risk students (attendance < 75%)
            String sql3 = "SELECT COUNT(DISTINCT studentID) as count FROM (" +
                         "SELECT studentID, " +
                         "(SUM(CASE WHEN status='Present' THEN 1 ELSE 0 END) * 100.0 / COUNT(*)) as rate " +
                         "FROM AttendanceRecord " +
                         "GROUP BY studentID " +
                         "HAVING rate < 75) as sub";
            Statement stmt3 = conn.createStatement();
            ResultSet rs3 = stmt3.executeQuery(sql3);
            int atRisk = 0;
            if(rs3.next()) {
                atRisk = rs3.getInt("count");
            }
            
            // Get attendance trend data (last 7 days)
            String sql4 = "SELECT " +
                         "DATE(ar.timeStamp) as date, " +
                         "(SUM(CASE WHEN ar.status='Present' THEN 1 ELSE 0 END) * 100.0 / COUNT(*)) as rate " +
                         "FROM AttendanceRecord ar " +
                         "WHERE ar.timeStamp >= DATE_SUB(CURDATE(), INTERVAL 7 DAY) " +
                         "GROUP BY DATE(ar.timeStamp) " +
                         "ORDER BY date";
            Statement stmt4 = conn.createStatement();
            ResultSet rs4 = stmt4.executeQuery(sql4);
            
            StringBuilder trendData = new StringBuilder("[");
            boolean first = true;
            while(rs4.next()) {
                if(!first) trendData.append(",");
                trendData.append("{")
                    .append("\"date\":\"").append(rs4.getString("date")).append("\",")
                    .append("\"rate\":").append(String.format("%.1f", rs4.getDouble("rate")))
                    .append("}");
                first = false;
            }
            trendData.append("]");
            
            // Build JSON response
            String json = "{" +
                "\"totalStudents\":" + totalStudents + "," +
                "\"avgAttendance\":" + String.format("%.1f", avgAttendance) + "," +
                "\"atRisk\":" + atRisk + "," +
                "\"trendData\":" + trendData.toString() +
                "}";
            
            out.print(json);
            
            stmt1.close();
            stmt2.close();
            stmt3.close();
            stmt4.close();
            conn.close();
            
        } catch(Exception e) {
            e.printStackTrace();
            out.print("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}