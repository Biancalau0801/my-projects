<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    /* -------------------------------
       Security Check
    ------------------------------- */
    if (session.getAttribute("user") == null ||
        !"Admin".equalsIgnoreCase((String) session.getAttribute("role"))) {
        response.sendRedirect("index.html");
        return;
    }

    /* -------------------------------
       Read current settings
    ------------------------------- */
    String lateThreshold = (String) application.getAttribute("lateThreshold");
    if (lateThreshold == null) lateThreshold = "15";

    String attendanceAlert = (String) application.getAttribute("attendanceAlert");
    if (attendanceAlert == null) attendanceAlert = "80";

    String sessionTimeout = (String) application.getAttribute("sessionTimeout");
    if (sessionTimeout == null) sessionTimeout = "30";

    String activityLog = (String) application.getAttribute("enableActivityLog");
    if (activityLog == null) activityLog = "false"; // default off
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>EduTrack - System Settings</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
    <style>
        :root { --sidebar-bg: #212529; --primary-blue: #0d6efd; }
        body { background-color: #f8f9fa; font-family: 'Segoe UI', sans-serif; }
        .sidebar { height: 100vh; background: var(--sidebar-bg); color: white; min-width: 240px; position: fixed; left: 0; top: 0; }
        .main-content { margin-left: 240px; padding: 40px; }
        .sidebar a { color: #adb5bd; text-decoration: none; display: block; padding: 15px 25px; }
        .sidebar a.active { background-color: var(--primary-blue); color: white; border-radius: 0 25px 25px 0; }
        .config-card { background: white; border-radius: 12px; padding: 30px; margin-bottom: 30px; box-shadow: 0 4px 6px rgba(0,0,0,0.1); }
        .section-header { font-weight: 700; font-size: 1.25rem; margin-bottom: 25px; border-bottom: 2px solid #f0f0f0; padding-bottom: 10px; }
    </style>
</head>

<body>
<div class="d-flex">
    <div class="sidebar">
        <div class="logo-area text-center py-4"><h4>EduTrack</h4></div>
        <nav>
            <a href="AdminDashboard">Dashboard</a>
            <a href="UserManagement">User Management</a>
            <a href="CourseManagement">Course Management</a>
            <a href="system_settings.jsp" class="active">System Settings</a>
            <hr class="mx-3 border-secondary" style="opacity: 0.1;">
            <a href="LogoutServlet" class="text-danger">Logout</a>
        </nav>
    </div>

    <div class="main-content flex-grow-1">
        <h3 class="fw-bold mb-4">System Settings</h3>

        <% if (request.getAttribute("message") != null) { %>
            <div class="alert alert-success alert-dismissible fade show">
                <i class="bi bi-check-circle-fill me-2"></i>
                <%= request.getAttribute("message") %>
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        <% } %>

        <form action="<%= request.getContextPath() %>/SystemSettingsServlet" method="post">

            <div class="config-card">
                <div class="section-header">Attendance Thresholds</div>

                <div class="mb-4">
                    <label class="form-label">Late Arrival Threshold (Minutes)</label>
                    <input type="number" name="lateThreshold" class="form-control"
                           value="<%= lateThreshold %>" min="1" max="60" required
                           style="max-width: 300px;">
                </div>

                <div class="mb-4">
                    <label class="form-label">Low Attendance Alert Threshold (%)</label>
                    <div class="d-flex align-items-center gap-3" style="max-width: 600px;">
                        <input type="range" name="attendanceAlert" class="form-range"
                               min="0" max="100" value="<%= attendanceAlert %>"
                               oninput="document.getElementById('rangeVal').innerText=this.value+'%'">
                        <span class="badge bg-primary fs-6" id="rangeVal"
                              style="min-width:65px;"><%= attendanceAlert %>%</span>
                    </div>
                </div>
            </div>

            <div class="config-card">
                <div class="section-header">Technical Settings</div>
                <div class="mb-3">
                    <label class="form-label">Session Timeout (Minutes)</label>
                    <input type="number" name="sessionTimeout" class="form-control"
                           value="<%= sessionTimeout %>" min="1" max="60" required
                           style="max-width: 300px;">
                </div>

                <!-- Activity Log Switch -->
                <div class="mb-3 form-check form-switch">
                    <input class="form-check-input" type="checkbox" name="enableActivityLog" id="enableActivityLog"
                        <%= "true".equals(activityLog) ? "checked" : "" %> >
                    <label class="form-check-label" for="enableActivityLog">Enable Activity Logging</label>
                </div>
            </div>

            <div class="d-flex justify-content-end gap-3 mb-5">
                <button type="button"
                        onclick="window.location.href='<%= request.getContextPath() %>/ResetSettingsServlet'"
                        class="btn btn-outline-secondary px-4 fw-bold">
                    Reset to Default
                </button>

                <button type="submit"
                        class="btn btn-primary px-5 fw-bold shadow-sm">
                    Save Global Settings
                </button>
            </div>
        </form>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
