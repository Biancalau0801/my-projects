<%@ page import="java.util.*" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String studentID = (String) session.getAttribute("user");
    String firstName = (String) session.getAttribute("firstName");
    List<Map<String, Object>> reports = (List<Map<String, Object>>) request.getAttribute("courseReports");
%>
<!DOCTYPE html>
<html>
<head>
    <title>My Attendance Dashboard</title>
    <style>
        :root { --primary: #3b82f6; --success: #22c55e; --danger: #ef4444; --warning: #f59e0b; --dark: #1e293b; --bg: #f1f5f9; }
        body { font-family: 'Inter', sans-serif; background: var(--bg); margin: 0; padding: 40px; color: var(--dark); }
        .container { max-width: 900px; margin: auto; } /* Narrower container for better single-column look */
        
        /* Profile Header */
        .profile-card { background: white; border-radius: 16px; padding: 30px; display: flex; align-items: center; justify-content: space-between; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.1); margin-bottom: 30px; }
        .profile-info { display: flex; align-items: center; gap: 20px; }
        .avatar { width: 80px; height: 80px; background: #6366f1; border-radius: 50%; display: flex; align-items: center; justify-content: center; color: white; font-size: 32px; font-weight: bold; }
        
        /* Stats Grid */
        .stats-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 20px; margin-bottom: 40px; }
        .stat-box { padding: 20px; border-radius: 12px; color: white; display: flex; flex-direction: column; }
        .stat-box.blue { background: #3b82f6; } .stat-box.green { background: #10b981; } .stat-box.red { background: #ef4444; } .stat-box.purple { background: #8b5cf6; }
        .stat-val { font-size: 28px; font-weight: 800; margin-bottom: 5px; }
        .stat-label { font-size: 12px; opacity: 0.9; font-weight: 500; }

        /* Course Grid - UPDATED FOR SINGLE COLUMN */
        .course-grid { display: grid; grid-template-columns: 1fr; gap: 20px; }
        .course-card { background: white; border-radius: 16px; padding: 24px; border: 1px solid #e2e8f0; position: relative; box-shadow: 0 2px 4px rgba(0,0,0,0.05); }
        .course-header { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 10px; }
        .course-code { font-weight: 800; color: var(--dark); font-size: 22px; margin: 0; }
        .instructor { font-size: 14px; color: #64748b; margin-top: 4px; }
        .pct-large { font-size: 44px; font-weight: 900; margin: 15px 0; }
        .progress-bar { height: 12px; background: #f1f5f9; border-radius: 6px; overflow: hidden; margin-bottom: 15px; }
        .progress-fill { height: 100%; border-radius: 6px; transition: width 1s; }
        .badge { font-size: 12px; font-weight: 700; padding: 4px 12px; border-radius: 20px; }
    </style>
</head>
<body>

<div class="container">
    <div class="mb-8">
        <h1 style="font-size: 36px; margin: 0;">My Attendance Dashboard</h1>
        <p style="color: #64748b; margin: 10px 0 30px 0;">Track your academic standing across all courses</p>
    </div>

    <div class="profile-card">
        <div class="profile-info">
            <div class="avatar"><%= firstName.substring(0,1).toUpperCase() %></div>
            <div>
                <h2 style="margin: 0;"><%= firstName %> <span class="badge" style="background: #dcfce7; color: #166534; vertical-align: middle; margin-left: 10px;">Good Standing</span></h2>
                <p style="color: #64748b; margin: 5px 0;">Student ID: <%= studentID %></p>
            </div>
        </div>
        <div style="text-align: right;">
            <p style="color: #64748b; font-size: 14px; margin: 0;">Overall Attendance</p>
            <div style="font-size: 44px; font-weight: 900;"><%= request.getAttribute("overallPct") %>%</div>
        </div>
    </div>

    <div class="stats-grid">
        <div class="stat-box blue"><span class="stat-val"><%= request.getAttribute("totalCourses") %></span><span class="stat-label">Enrolled Courses</span></div>
        <div class="stat-box green"><span class="stat-val"><%= request.getAttribute("totalAttended") %></span><span class="stat-label">Classes Attended</span></div>
        <div class="stat-box red"><span class="stat-val"><%= request.getAttribute("totalMissed") %></span><span class="stat-label">Classes Missed</span></div>
        <div class="stat-box purple"><span class="stat-val"><%= request.getAttribute("overallPct") %>%</span><span class="stat-label">Average Attendance</span></div>
    </div>

    <div class="course-grid">
        <% 
            if (reports != null && !reports.isEmpty()) {
                for (Map<String, Object> r : reports) { 
                    double pct = Double.parseDouble((String)r.get("pct"));
                    String color = (pct >= 90.0) ? "#22c55e" : (pct >= 75.0 ? "#f59e0b" : "#ef4444");
                    String status = (pct >= 90.0) ? "Excellent" : (pct >= 75.0 ? "Good" : "At Risk");
        %>
            <div class="course-card">
                <div class="course-header">
                    <div>
                        <h3 class="course-code"><%= r.get("className") %></h3>
                        <div class="instructor">Total Classes: <%= r.get("total") %></div>
                    </div>
                    <span class="badge" style="background: <%= color %>22; color: <%= color %>;"><%= status %></span>
                </div>
                <div class="pct-large"><%= r.get("pct") %>%</div>
                <div class="progress-bar">
                    <div class="progress-fill" style="width: <%= r.get("pct") %>%; background: <%= color %>;"></div>
                </div>
                <div style="display: flex; justify-content: space-between; font-size: 14px; color: #64748b;">
                    <span>Attended: <b><%= r.get("present") %></b></span>
                    <span>Next class: Monday, 9:00 AM</span>
                </div>
            </div>
        <% } } else { %>
            <div style="text-align: center; background: white; padding: 60px; border-radius: 16px;">
                <h3 style="color: #64748b;">No attendance records found for ID: <%= studentID %></h3>
            </div>
        <% } %>
    </div>

    <div style="text-align: center; margin-top: 50px;">
        <a href="dashboard.jsp" style="color: var(--primary); font-weight: 700; text-decoration: none;">← Back to Portal</a>
    </div>
</div>

</body>
</html>