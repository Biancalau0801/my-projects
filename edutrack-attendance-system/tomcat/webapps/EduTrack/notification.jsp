<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>EduTrack | Notifications</title>
    <style>
        :root { --primary: #10b981; --dark: #0f172a; --slate: #64748b; }
        body { 
            font-family: 'Inter', sans-serif; 
            background: #f8fafc; 
            padding: 40px; 
            display: flex; 
            justify-content: center; 
            margin: 0;
        }
        .notif-container { 
            width: 100%; 
            max-width: 600px; 
            background: white; 
            padding: 30px; 
            border-radius: 24px; 
            box-shadow: 0 10px 30px rgba(0,0,0,0.05); 
        }
        .back-link { 
            text-decoration: none; 
            color: var(--primary); 
            font-weight: 700; 
            margin-bottom: 20px; 
            display: inline-block; 
            transition: 0.2s;
        }
        .back-link:hover { opacity: 0.7; }
        
        h1 { color: var(--dark); font-size: 28px; margin-bottom: 25px; }

        .notif-card { 
            padding: 20px; 
            border-left: 4px solid var(--primary); 
            background: #f0fdf4; 
            border-radius: 12px; 
            margin-bottom: 15px;
            display: flex;
            align-items: center;
            gap: 15px;
            animation: slideIn 0.3s ease-out;
        }

        .notif-content { flex: 1; }
        .notif-message { margin: 0; font-weight: 600; color: var(--dark); line-height: 1.4; }
        .notif-time { color: var(--slate); font-size: 12px; display: block; margin-top: 4px; }

        @keyframes slideIn {
            from { opacity: 0; transform: translateX(-10px); }
            to { opacity: 1; transform: translateX(0); }
        }
    </style>
</head>
<body>
    <div class="notif-container">
        <a href="dashboard.jsp" class="back-link">← Back to Dashboard</a>
        <h1>Notifications History</h1>

        <% 
            // The Servlet now sends a List of Maps (message + time)
            List<Map<String, String>> list = (List<Map<String, String>>) request.getAttribute("notifList");
            
            if (list != null && !list.isEmpty()) {
                for (Map<String, String> notif : list) { 
        %>
            <div class="notif-card">
                <span style="font-size: 24px;">🔔</span>
                <div class="notif-content">
                    <p class="notif-message"><%= notif.get("message") %></p>
                    <small class="notif-time">📅 Received: <%= notif.get("time") %></small>
                </div>
            </div>
        <% 
                }
            } else { 
        %>
            <div style="text-align: center; padding: 60px 20px; color: var(--slate);">
                <div style="font-size: 48px; margin-bottom: 10px;">📩</div>
                <p style="font-weight: 500;">Your notification history is empty.</p>
                <span style="font-size: 14px;">Try enrolling in a class to see alerts here.</span>
            </div>
        <% } %>
    </div>
</body>
</html>