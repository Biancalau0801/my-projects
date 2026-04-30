<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    // Security: Only allow logged-in Lecturers
    if (session.getAttribute("user") == null || !"Lecturer".equals(session.getAttribute("role"))) {
        response.sendRedirect("index.html?error=unauthorized");
        return;
    }
    String staffID = (String) session.getAttribute("user");
    String firstName = (String) session.getAttribute("firstName");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Lecturer Profile | EduTrack</title>
    <style>
        :root { --primary: #10b981; --dark: #0f172a; --slate: #64748b; }
        body { 
            font-family: 'Inter', sans-serif; 
            background: radial-gradient(circle at top right, #f8fafc, #cbd5e1); 
            display: flex; justify-content: center; align-items: center; 
            height: 100vh; margin: 0; 
        }
        .profile-card { 
            background: white; padding: 40px; border-radius: 32px; 
            box-shadow: 0 20px 40px rgba(0,0,0,0.1); text-align: center; width: 380px; 
        }
        .avatar { 
            width: 80px; height: 80px; background: var(--primary); color: white; 
            border-radius: 50%; display: flex; align-items: center; justify-content: center; 
            font-size: 32px; font-weight: 800; margin: 0 auto 20px; 
        }
        h2 { margin: 10px 0; color: var(--dark); font-weight: 800; }
        .info-group { margin-top: 25px; text-align: left; background: #f8fafc; padding: 20px; border-radius: 16px; }
        .info-label { color: var(--slate); font-size: 11px; text-transform: uppercase; font-weight: 700; letter-spacing: 1px; }
        .info-value { font-size: 16px; color: var(--dark); font-weight: 600; margin-bottom: 15px; }
        .back-btn { 
            text-decoration: none; color: var(--primary); font-weight: 700; 
            display: inline-block; margin-top: 30px; font-size: 14px;
        }
    </style>
</head>
<body>

    <div class="profile-card">
        <div class="avatar"><%= firstName.substring(0,1).toUpperCase() %></div>
        <h2>Lecturer Profile</h2>
        
        <div class="info-group">
            <div class="info-label">Lecturer Name</div>
            <div class="info-value">Prof. <%= firstName %></div>

            <div class="info-label">Staff ID Number</div>
            <div class="info-value"><%= staffID %></div> </div>

        <a href="lecturer_dashboard.jsp" class="back-btn">← Back to Dashboard</a>
    </div>

</body>
</html>