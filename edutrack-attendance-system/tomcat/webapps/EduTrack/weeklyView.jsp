<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%
    if (session.getAttribute("user") == null) { response.sendRedirect("index.html"); return; }
    Map<String, List<Map<String, String>>> weeklyData = (Map<String, List<Map<String, String>>>) request.getAttribute("weeklyData");
%>
<!DOCTYPE html>
<html>
<head>
    <title>EduTrack | Weekly Planner</title>
    <style>
        :root { --primary: #3498db; --bg: #f4f7f6; --card-bg: #ffffff; }
        body { font-family: 'Inter', 'Segoe UI', sans-serif; background: var(--bg); margin: 0; padding: 40px; }
        
        .header-nav { max-width: 1200px; margin: 0 auto 30px; display: flex; justify-content: space-between; align-items: center; }
        .btn-return { text-decoration: none; background: white; color: var(--primary); padding: 10px 20px; border-radius: 30px; font-weight: bold; border: 2px solid var(--primary); transition: 0.3s; }
        .btn-return:hover { background: var(--primary); color: white; }

        .calendar-grid { 
            display: grid; 
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); 
            gap: 15px; 
            max-width: 1200px; 
            margin: 0 auto; 
        }

        .day-column { background: var(--card-bg); border-radius: 12px; padding: 15px; box-shadow: 0 4px 6px rgba(0,0,0,0.05); min-height: 400px; }
        .day-header { border-bottom: 3px solid #eee; padding-bottom: 10px; margin-bottom: 15px; text-align: center; }
        .day-header.today { border-bottom-color: var(--primary); }
        .day-header h3 { margin: 0; color: #444; font-size: 18px; text-transform: uppercase; letter-spacing: 1px; }

        .class-card { 
            background: #ebf5fb; 
            border-left: 4px solid var(--primary); 
            padding: 10px; 
            margin-bottom: 10px; 
            border-radius: 4px; 
            font-size: 13px; 
        }
        .class-time { font-weight: bold; color: #2980b9; display: block; margin-bottom: 4px; }
        .class-code { font-weight: 800; color: #333; }
        .empty-msg { color: #ccc; text-align: center; font-style: italic; margin-top: 20px; font-size: 13px; }
    </style>
</head>
<body>

    <div class="header-nav">
        <h1 style="margin:0; color:#2c3e50;">Weekly Planner</h1>
        <a href="TimetableServlet?operation=view" class="btn-return">← Back to Management</a>
    </div>

    <div class="calendar-grid">
        <% 
            String[] displayDays = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday"};
            for (String day : displayDays) { 
                List<Map<String, String>> classes = weeklyData.get(day);
        %>
            <div class="day-column">
                <div class="day-header">
                    <h3><%= day %></h3>
                </div>

                <% if (classes == null || classes.isEmpty()) { %>
                    <div class="empty-msg">Free Day</div>
                <% } else { 
                    for (Map<String, String> c : classes) { %>
                    <div class="class-card">
                        <span class="class-time"><%= c.get("time").substring(4) %></span>
                        <span class="class-code"><%= c.get("code") %></span>
                        <div style="color:#7f8c8d; font-size:11px; margin-top:5px;"><%= c.get("instructor") %></div>
                    </div>
                <% } } %>
            </div>
        <% } %>
    </div>

</body>
</html>