<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    if (session.getAttribute("user") == null || !"Admin".equalsIgnoreCase((String)session.getAttribute("role"))) {
        response.sendRedirect("index.html");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>EduTrack - Course Management</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">
    <style>
        :root { --sidebar-bg: #212529; --primary-blue: #0d6efd; }
        body { background-color: #f8f9fa; font-family: 'Segoe UI', sans-serif; }
        .sidebar { height: 100vh; background: var(--sidebar-bg); color: white; min-width: 240px; position: fixed; left: 0; top: 0; z-index: 1000; }
        .main-content { margin-left: 240px; padding: 40px; }
        .sidebar .logo-area { padding: 30px 25px; border-bottom: 1px solid rgba(255,255,255,0.05); }
        .sidebar h4 { font-weight: 800; color: white; margin: 0; }
        .sidebar a { color: #adb5bd; text-decoration: none; display: block; padding: 15px 25px; transition: 0.3s; font-weight: 500; }
        .sidebar a:hover { background: #343a40; color: white; }
        .sidebar a.active { background-color: var(--primary-blue); color: white; border-radius: 0 25px 25px 0; margin-right: 10px; }
        .card { border-radius: 12px; border: none; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.1); }
        .btn-action { background: transparent; border: none; font-size: 1.1rem; padding: 4px 8px; text-decoration: none; display: inline-block; }
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
            <a href="AdminDashboard">Dashboard</a>
            <a href="UserManagement">User Management</a>
            <a href="CourseManagement" class="active">Course Management</a>
            <a href="system_settings.jsp">System Settings</a>
            <hr class="mx-3 border-secondary" style="opacity: 0.1;">
            <a href="LogoutServlet" class="text-danger">Logout</a>
        </nav>
    </div>

    <div class="main-content flex-grow-1">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h3 class="fw-bold">Course Management</h3>
            <button class="btn btn-primary px-4 py-2 fw-bold shadow-sm" 
                    data-bs-toggle="modal" data-bs-target="#courseModal" 
                    onclick="prepareAdd()" style="border-radius: 8px;">
                + Add New Course
            </button>
        </div>

        <c:if test="${not empty param.message}">
            <div class="alert alert-info alert-dismissible fade show shadow-sm mb-4" role="alert">
                ${param.message}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <div class="card p-3 mb-4">
            <form action="CourseManagement" method="get" class="input-group" style="max-width: 450px;">
                <span class="input-group-text bg-white border-end-0 text-muted"><i class="bi bi-search"></i></span>
                <input type="text" name="search" class="form-control border-start-0 ps-0" placeholder="Search Code or Name...">
                <button type="submit" class="btn btn-primary px-3">Search</button>
            </form>
        </div>

        <div class="card bg-white overflow-hidden">
            <table class="table table-hover align-middle mb-0">
                <thead class="bg-light">
                    <tr>
                        <th class="ps-4 py-3">Course Code</th>
                        <th>Course Name</th>
                        <th>Credits</th>
                        <th>Lecturer</th>
                        <th class="text-center">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="c" items="${courses}">
                        <tr>
                            <td class="ps-4 fw-bold">${c.courseCode}</td>
                            <td>${c.courseName}</td>
                            <td>${c.credits}</td>
                            <td>${c.lecturer}</td>
                            <td class="text-center">
                                <button class="btn-action text-primary" data-bs-toggle="modal" data-bs-target="#courseModal" 
                                        onclick="prepareEdit('${c.id}', '${c.courseCode}', '${c.courseName}', '${c.credits}', '${c.lecturer}')">
                                    <i class="bi bi-pencil-square"></i>
                                </button>
                                <a href="CourseManagement?action=delete&id=${c.id}" class="btn-action text-danger" 
                                   onclick="return confirm('Delete this course?')">
                                    <i class="bi bi-trash"></i>
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty courses}">
                        <tr>
                            <td colspan="5" class="text-center p-5 text-muted">
                                No courses found.
                            </td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<div class="modal fade" id="courseModal" tabindex="-1">
    <div class="modal-dialog">
        <form action="CourseManagement" method="post" class="modal-content" style="border-radius: 12px; border:none;">
            <div class="modal-header">
                <h5 class="modal-title fw-bold" id="modalTitle">Add New Course</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <input type="hidden" name="id" id="formId">
                <div class="mb-3">
                    <label class="form-label small fw-bold">Course Code</label>
                    <input type="text" name="courseCode" id="formCode" class="form-control" placeholder="e.g., CS101" required>
                </div>
                <div class="mb-3">
                    <label class="form-label small fw-bold">Course Name</label>
                    <input type="text" name="courseName" id="formName" class="form-control" required>
                </div>
                <div class="mb-3">
                    <label class="form-label small fw-bold">Credits</label>
                    <input type="number" name="credits" id="formCredits" class="form-control" min="1" max="10" required>
                </div>
                <div class="mb-3">
                    <label class="form-label small fw-bold">Lecturer</label>
                    <select name="lecturer" id="formLecturer" class="form-select" required>
                        <option value="">-- Select Lecturer --</option>
                        <c:forEach var="lecName" items="${lecturerList}">
                            <option value="${lecName}">${lecName}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                <button type="submit" class="btn btn-primary px-4">Save Course</button>
            </div>
        </form>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    function prepareAdd() { 
        document.getElementById('modalTitle').innerText = 'Add New Course'; 
        document.getElementById('formId').value = ''; 
        document.getElementById('formCode').value = ''; 
        document.getElementById('formName').value = ''; 
        document.getElementById('formCredits').value = ''; 
        document.getElementById('formLecturer').value = ''; 
    }
    function prepareEdit(id, code, name, credits, lecturer) { 
        document.getElementById('modalTitle').innerText = 'Edit Course: ' + code; 
        document.getElementById('formId').value = id; 
        document.getElementById('formCode').value = code; 
        document.getElementById('formName').value = name; 
        document.getElementById('formCredits').value = credits; 
        // Dropdown automatically selects the matching lecturer name
        document.getElementById('formLecturer').value = lecturer; 
    }
</script>
</body>
</html>