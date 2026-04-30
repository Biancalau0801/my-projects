<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // Security: Only allow logged-in Lecturers
    if (session.getAttribute("user") == null || !"Lecturer".equals(session.getAttribute("role"))) {
        response.sendRedirect("index.html?error=unauthorized");
        return;
    }
    String userTitle = (String) session.getAttribute("title");
    String firstName = (String) session.getAttribute("firstName");
    String staffID = (String) session.getAttribute("user");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>EduTrack | Lecturer Portal</title>
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

        /* --- HEADER --- */
        .header { text-align: center; margin-bottom: 60px; }
        .header h1 { margin: 0; font-size: 52px; font-weight: 900; color: var(--dark); letter-spacing: -2px; }
        .header p { margin: 10px 0; color: var(--slate); font-size: 20px; font-weight: 500; }

        /* --- DASHBOARD GRID --- */
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
        }

        .glass-card:hover { 
            transform: translateY(-12px); 
            border-color: var(--primary); 
            box-shadow: 0 25px 50px rgba(0,0,0,0.1);
            background: white;
        }

        .glass-card h3 { margin: 0 0 12px 0; font-size: 24px; font-weight: 800; color: var(--dark); line-height: 1.2; }
        .glass-card span { font-size: 15px; color: var(--slate); font-weight: 500; line-height: 1.5; }

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
            <span style="font-weight: 700; color: var(--dark);">Welcome,  <%= (userTitle != null && !userTitle.isEmpty() ? userTitle + " " : "") %><%= firstName %></span>
            <div class="profile-icon">
                <%= (firstName != null && !firstName.isEmpty()) ? firstName.substring(0,1).toUpperCase() : "L" %>
            </div>
            <div class="dropdown">
                <a href="lecturerprofile.jsp">My Profile</a>
                <a href="LogoutServlet" class="logout">Log Out</a>
            </div>
        </div>
    </nav>

    <div class="header">
        <h1>EduTrack</h1>
        <p>Lecturer Portal Dashboard</p>
    </div>

    <div class="dashboard-grid">
        <a href="activateGeofence.jsp" class="glass-card">
            <h3>Geofencing</h3>
            <span>Restrict attendance to classroom coordinates.</span>
        </a>

        <a href="generate_qr.jsp" class="glass-card">
            <h3>QR Code Generation</h3>
            <span>Generate a QR code for students to scan.</span>
        </a>

        <a href="AttendanceServlet_lecture?action=selectClass" class="glass-card">
            <h3>Manual Attendance</h3>
            <span>Choose a class section and mark students present.</span>
        </a>

        <a href="AttendanceServlet_lecture?action=vieselectClassAnalytics" class="glass-card">
            <h3>Attendance Analytics</h3>
            <span>View student attendance percentages and risks.</span>
        </a>
    </div>

</body>
</html>