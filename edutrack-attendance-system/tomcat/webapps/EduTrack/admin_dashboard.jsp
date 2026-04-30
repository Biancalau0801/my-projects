<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    if (session.getAttribute("user") == null || !"Admin".equalsIgnoreCase((String)session.getAttribute("role"))) {
        response.sendRedirect("index.html?error=unauthorized");
        return;
    }
    String adminName = (String)session.getAttribute("userName");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>EduTrack - Admin Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
    <style>
        :root { --sidebar-bg: #212529; --primary-blue: #0d6efd; }
        body { background-color: #f8f9fa; font-family: 'Segoe UI', sans-serif; }
        .sidebar { height: 100vh; background: var(--sidebar-bg); color: white; min-width: 240px; position: fixed; left: 0; top: 0; z-index: 1000; }
        .main-content { margin-left: 240px; padding: 40px; }
        .sidebar .logo-area { padding: 30px 25px; border-bottom: 1px solid rgba(255,255,255,0.05); }
        .sidebar a { color: #adb5bd; text-decoration: none; display: block; padding: 15px 25px; transition: 0.3s; }
        .sidebar a:hover, .sidebar a.active { background: #343a40; color: white; }
        .sidebar a.active { background-color: var(--primary-blue); border-radius: 0 25px 25px 0; margin-right: 10px; }
        .stat-card { border-radius: 12px; border: none; padding: 25px; box-shadow: 0 4px 6px rgba(0,0,0,0.05); transition: transform 0.2s; background: white; }
        .stat-card:hover { transform: translateY(-5px); }
        .stat-label { font-size: 0.85rem; color: #64748b; font-weight: 600; text-transform: uppercase; }
        .stat-value { font-size: 2rem; font-weight: 800; margin: 5px 0; }
        .activity-item { border-left: 3px solid var(--primary-blue); padding: 10px 15px; margin-bottom: 15px; background: #fbfcfd; border-radius: 0 8px 8px 0; }
    </style>
</head>
<body>
<div class="d-flex">
    <div class="sidebar shadow">
        <div class="logo-area text-center">
            <h4>EduTrack</h4>
            <small class="text-muted">Admin System</small>
        </div>
        <nav class="mt-2">
            <a href="AdminDashboard" class="active">Dashboard</a>
            <a href="UserManagement">User Management</a>
            <a href="CourseManagement">Course Management</a>
            <a href="system_settings.jsp">System Settings</a>
            <hr class="mx-3 border-secondary" style="opacity: 0.1;">
            <a href="LogoutServlet" class="text-danger">Logout</a>
        </nav>
    </div>

    <div class="main-content flex-grow-1">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <div>
                <h3 class="fw-bold">Welcome, <%= (adminName != null ? adminName : "Admin") %></h3>
                <p class="text-muted">Real-time system statistics</p>
            </div>
            <span class="badge bg-success-subtle text-success border border-success px-3 py-2">System: Online</span>
        </div>

        <div class="row g-4 mb-5">
            <div class="col-md-3">
                <div class="card stat-card">
                    <div class="stat-label">Total Users</div>
                    <div class="stat-value">${totalUsers}</div>
                    <small class="text-muted">Registered in system</small>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card stat-card">
                    <div class="stat-label text-primary">Students</div>
                    <div class="stat-value text-primary">${studentCount}</div>
                    <small class="text-muted">Active enrollments</small>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card stat-card">
                    <div class="stat-label text-info">Lecturers</div>
                    <div class="stat-value text-info">${lecturerCount}</div>
                    <small class="text-muted">Academic staff</small>
                </div>
            </div>
            <div class="col-md-3">
                <div class="card stat-card">
                    <div class="stat-label text-success">Active Classes</div>
                    <div class="stat-value text-success">${activeCourses}</div>
                    <small class="text-muted">From timetable</small>
                </div>
            </div>
        </div>

        <div class="card p-4 shadow-sm border-0" style="border-radius: 12px;">
            <h5 class="fw-bold mb-4">System Activity Log</h5>
            
            <c:forEach var="log" items="${recentLogs}">
                <div class="activity-item">
                    <div class="d-flex justify-content-between align-items-center">
                        <div>
                            <p class="mb-0 fw-semibold text-dark">${log.description}</p>
                            <small class="text-muted"><i class="bi bi-clock me-1"></i> ${log.timestamp}</small>
                        </div>
                        <span class="badge bg-light text-dark border">Log #${log.id}</span>
                    </div>
                </div>
            </c:forEach>

            <c:if test="${empty recentLogs}">
                <div class="text-center py-5">
                    <i class="bi bi-journal-x text-muted" style="font-size: 2.5rem;"></i>
                    <p class="text-muted mt-2 italic">No recent activities logged.</p>
                </div>
            </c:if>
        </div>
    </div>
</div>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>