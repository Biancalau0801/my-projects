<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*, java.util.*" %>
<%
    if (session.getAttribute("user") == null) {
        response.sendRedirect("index.html?error=unauthorized");
        return;
    }
    String firstName = (String) session.getAttribute("firstName");
    String studentID = (String) session.getAttribute("user");
    
    // --- UPDATED NOTIFICATION LOGIC ---
    int unreadCount = 0;
    String dbUrl = "jdbc:mysql://localhost:3306/edutrack";
    String dbUser = "root";
    String dbPass = "bianca0801";

    if (studentID != null) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass)) {
                // This query counts logs that are newer than the student's last check-in time
                String sql = "SELECT COUNT(*) AS total FROM notification_logs nl " +
                             "LEFT JOIN notification_read_status nrs ON nl.studentID = nrs.studentID " +
                             "WHERE nl.studentID = ? " +
                             "AND (nrs.last_checked IS NULL OR nl.created_at > nrs.last_checked)";

                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, studentID);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        unreadCount = rs.getInt("total");
                    }
                }
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        }
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>EduTrack | Student Portal</title>
    <style>
        :root { --primary: #10b981; --dark: #0f172a; --slate: #64748b; --danger: #f43f5e; }
        
        body { 
            font-family: 'Inter', sans-serif; 
            background: radial-gradient(circle at top right, #f8fafc, #cbd5e1); 
            margin: 0;
            padding-top: 120px;
            display: flex;
            flex-direction: column;
            align-items: center;
            min-height: 100vh;
        }

        /* --- TOP NAVIGATION --- */
        .top-bar {
            position: fixed;
            top: 0;
            width: 100%;
            height: 80px;
            background: rgba(255, 255, 255, 0.9);
            backdrop-filter: blur(10px);
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 0 60px;
            box-sizing: border-box;
            border-bottom: 1px solid rgba(0,0,0,0.05);
            z-index: 1000;
        }

        .logo-text { font-weight: 900; font-size: 26px; color: var(--dark); letter-spacing: -1px; }

        .profile-area {
            position: relative;
            display: flex;
            align-items: center;
            gap: 12px;
            cursor: pointer;
            padding: 30px 0; 
        }

        .profile-icon {
            width: 42px;
            height: 42px;
            background: var(--primary);
            color: white;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: 800;
            box-shadow: 0 4px 12px rgba(16, 185, 129, 0.2);
        }

        .dropdown {
            display: none;
            position: absolute;
            top: 75px;
            right: 0;
            background: white;
            box-shadow: 0 15px 40px rgba(0,0,0,0.15);
            border-radius: 20px;
            width: 240px;
            overflow: hidden;
            border: 1px solid #eee;
            animation: fadeIn 0.2s ease-out;
        }

        .profile-area:hover .dropdown { display: block; }

        .id-badge { background: #f8fafc; padding: 15px 20px; border-bottom: 1px solid #f1f5f9; }
        .id-badge span { display: block; font-size: 11px; color: var(--slate); font-weight: 700; text-transform: uppercase; }
        .id-badge b { font-size: 14px; color: var(--dark); }

        .dropdown a { display: block; padding: 14px 20px; text-decoration: none; color: var(--dark); font-size: 14px; font-weight: 600; transition: 0.2s; }
        .dropdown a:hover { background: #f8fafc; color: var(--primary); }
        .dropdown .logout { color: var(--danger); border-top: 1px solid #f1f5f9; }

        .header { text-align: center; margin-bottom: 60px; }
        .header h1 { margin: 0; font-size: 52px; font-weight: 900; color: var(--dark); letter-spacing: -2px; }
        .header p { margin: 10px 0; color: var(--slate); font-size: 20px; font-weight: 500; }

        .dashboard-grid { 
            display: grid; 
            grid-template-columns: repeat(2, 1fr); 
            gap: 30px; 
            max-width: 900px; 
            width: 95%;
            margin-bottom: 50px;
        }

        .glass-card { 
            background: rgba(255,255,255,0.8); 
            padding: 50px 30px; 
            border-radius: 32px; 
            text-align: center; 
            text-decoration: none; 
            color: var(--dark); 
            border: 1px solid white; 
            transition: all 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275); 
            box-shadow: 0 10px 20px rgba(0,0,0,0.03);
            display: flex;
            flex-direction: column;
            justify-content: center;
            min-height: 220px;
            position: relative; 
        }

        .glass-card:hover { 
            transform: translateY(-12px); 
            border-color: var(--primary); 
            box-shadow: 0 25px 50px rgba(0,0,0,0.1);
            background: white;
        }

        .glass-card h3 { margin: 0 0 12px 0; font-size: 24px; font-weight: 800; color: var(--dark); line-height: 1.2; }
        .glass-card span { font-size: 15px; color: var(--slate); font-weight: 500; line-height: 1.5; }

        .notif-badge {
            position: absolute;
            top: 20px;
            right: 20px;
            background: var(--danger);
            color: white;
            font-size: 12px;
            font-weight: 800;
            padding: 4px 10px;
            border-radius: 20px;
            box-shadow: 0 4px 10px rgba(244, 63, 94, 0.3);
            border: 2px solid white;
        }

        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(10px); }
            to { opacity: 1; transform: translateY(0); }
        }
    </style>
</head>
<body>

    <nav class="top-bar">
        <div class="logo-text">EduTrack</div>
        
        <div class="profile-area">
            <span style="font-weight: 700; color: var(--dark);">Welcome, <%= firstName %></span>
            <div class="profile-icon">
                <%= firstName.substring(0,1).toUpperCase() %>
            </div>
            <div class="dropdown">
                <a href="profile.jsp">My Profile</a>
                <a href="LogoutServlet" class="logout">Log Out</a>
            </div>
        </div>
    </nav>

    <div class="header">
        <h1>EduTrack</h1>
        <p>Student Portal Dashboard</p>
    </div>

    <div class="dashboard-grid">
        <a href="scan_attendance.jsp" class="glass-card">
            <h3>Scan QR & Geolocation</h3>
            <span>Securely record your presence using smart location verification.</span>
        </a>

        <a href="AttendanceServlet" class="glass-card">
            <h3>Attendance Percentage Report</h3>
            <span>Monitor your academic standing with real-time percentage tracking.</span>
        </a>

        <a href="TimetableServlet?operation=view" class="glass-card">
           <h3>Class & Timetable Management</h3>
           <span>View your schedule or easily add, drop, and update your classes.</span>
        </a>

        <a href="NotificationServlet" class="glass-card">
            <% if (unreadCount > 0) { %>
                <div class="notif-badge"><%= unreadCount %> New</div>
            <% } %>
            <h3>Notifications</h3>
            <span>Stay informed with alerts regarding schedule changes and campus news.</span>
        </a>
    </div>

</body>
</html>