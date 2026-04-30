<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    // -------------------------------
    // Security check: Only allow Admin to access
    // -------------------------------
    if (session.getAttribute("user") == null || !"Admin".equalsIgnoreCase((String)session.getAttribute("role"))) {
        response.sendRedirect("index.html?error=unauthorized");
        return;
    }
    String adminName = (String)session.getAttribute("firstName");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>EduTrack - User Management</title>

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

        .badge-role { border-radius: 15px; padding: 6px 14px; font-weight: 500; font-size: 0.8rem; display: inline-block; text-transform: capitalize; }
        .role-student { background: #eef2ff; color: #6366f1; }
        .role-lecturer { background: #ecfdf5; color: #10b981; }
        /* Updated Coordinator to Blue */
        .role-coordinator { background: #e0f2fe; color: #0369a1; } 
        .role-admin { background: #fff7ed; color: #f97316; }

        .status-active { background: #f0fdf4; color: #16a34a; border-radius: 8px; padding: 4px 12px; font-size: 0.8rem; font-weight: 600; }
        .status-inactive { background: #fef2f2; color: #dc2626; border-radius: 8px; padding: 4px 12px; font-size: 0.8rem; font-weight: 600; }

        .action-icon { font-size: 1.2rem; margin: 0 8px; transition: 0.2s; text-decoration: none; }
        .card { border: none; border-radius: 12px; box-shadow: 0 4px 6px -1px rgba(0,0,0,0.1); }
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
                <a href="UserManagement" class="active">User Management</a>
                <a href="CourseManagement">Course Management</a>
                <a href="system_settings.jsp">System Settings</a>
                <hr class="mx-3 border-secondary" style="opacity: 0.1;">
                <a href="LogoutServlet" class="text-danger">Logout</a>
            </nav>
        </div>

        <div class="main-content flex-grow-1">
            <h3 class="fw-bold mb-4">User Management</h3>
            
            <c:if test="${not empty param.message}">
                <div class="alert alert-info alert-dismissible fade show shadow-sm" role="alert">
                    <span>${param.message}</span>
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            </c:if>

            <div class="d-flex justify-content-between align-items-center mb-4">
                <form action="UserManagement" method="get" class="input-group" style="max-width: 350px;">
                    <span class="input-group-text bg-white border-end-0 text-muted"><i class="bi bi-search"></i></span>
                    <input type="text" name="search" class="form-control border-start-0 ps-0" placeholder="Search ID or Name...">
                </form>
                <button class="btn btn-primary px-4 py-2 fw-bold shadow-sm" onclick="resetForm()" style="border-radius: 8px;">+ Add New User</button>
            </div>
            
            <div class="card p-4 mb-4">
                <h5 id="formTitle" class="fw-bold mb-4">User Registration</h5>
                <form id="userForm" action="UserManagement" method="post" class="row g-3">
                    <input type="hidden" name="id" id="userId">

                    <div class="col-md-2">
                        <label class="form-label small text-muted">USER ID</label>
                        <input type="text" name="displayId" id="userIdInput" class="form-control" placeholder="e.g., S123" required>
                    </div>

                    <div class="col-md-3">
                        <label class="form-label small text-muted">FULL NAME</label>
                        <input type="text" name="name" id="userName" class="form-control" required>
                    </div>

                    <div class="col-md-3">
                        <label class="form-label small text-muted">EMAIL ADDRESS</label>
                        <input type="email" name="email" id="userEmail" class="form-control" required>
                    </div>

                    <div class="col-md-2">
                        <label class="form-label small text-muted">ROLE</label>
                        <select name="role" id="userRole" class="form-select">
                            <option value="Admin">Admin</option>
                            <option value="Programme Coordinator">Coordinator</option>
                            <option value="Lecturer">Lecturer</option>
                            <option value="Student">Student</option>
                        </select>
                    </div>

                    <div class="col-md-2">
                        <label class="form-label small text-muted">STATUS</label>
                        <select name="status" id="userStatus" class="form-select">
                            <option value="Active">Active</option>
                            <option value="Inactive">Inactive</option>
                        </select>
                    </div>

                    <div class="col-md-12 text-end mt-3">
                        <button type="submit" id="submitBtn" class="btn btn-success px-5 fw-bold py-2">Save User</button>
                    </div>
                </form>
            </div>

            <div class="card p-0 overflow-hidden">
                <table class="table table-hover align-middle mb-0">
                    <thead class="bg-light">
                        <tr>
                            <th class="ps-4 py-3">USER ID</th>
                            <th>NAME</th>
                            <th>EMAIL</th>
                            <th>ROLE</th>
                            <th>STATUS</th>
                            <th class="text-center">ACTIONS</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="u" items="${userList}">
                            <tr>
                                <td class="ps-4 fw-bold">${u.id}</td>
                                <td>${u.name}</td>
                                <td class="text-muted small">${u.email}</td>
                                <td>
                                    <c:set var="roleName" value="${u.role.toLowerCase()}" />
                                    <span class="badge-role ${roleName.contains('coordinator') ? 'role-coordinator' : 'role-'.concat(roleName.trim())}">
                                        ${roleName.contains('coordinator') ? 'Coordinator' : u.role}
                                    </span>
                                </td>
                                <td><span class="${u.status == 'Active' ? 'status-active' : 'status-inactive'}">${u.status}</span></td>
                                <td class="text-center">
                                    <a href="javascript:void(0)" class="action-icon text-primary" 
                                       onclick="editUser('${u.id}', '${u.name}', '${u.email}', '${u.role}', '${u.status}')">
                                        <i class="bi bi-pencil-square"></i>
                                    </a>
                                    <a href="UserManagement?action=delete&id=${u.id}" class="action-icon text-danger" onclick="return confirm('Delete user ${u.id}?')">
                                        <i class="bi bi-trash"></i>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty userList}">
                            <tr><td colspan="6" class="text-center p-5 text-muted">No users found. Ensure you access this page via the UserManagement Servlet.</td></tr>
                        </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <script>
        function editUser(id, name, email, role, status) {
            document.getElementById('userIdInput').value = id;
            document.getElementById('userId').value = id;
            document.getElementById('userName').value = name;
            document.getElementById('userEmail').value = email;
            document.getElementById('userRole').value = role;
            document.getElementById('userStatus').value = status;

            document.getElementById('formTitle').innerText = "Update User: " + id;
            document.getElementById('submitBtn').innerText = "Update Changes";
            document.getElementById('submitBtn').className = "btn btn-primary px-5 fw-bold py-2";

            window.scrollTo({top: 0, behavior: 'smooth'});
        }

        function resetForm() {
            document.getElementById('userForm').reset();
            document.getElementById('userId').value = "";
            document.getElementById('userIdInput').value = "";
            document.getElementById('formTitle').innerText = "User Registration";
            document.getElementById('submitBtn').innerText = "Save User";
            document.getElementById('submitBtn').className = "btn btn-success px-5 fw-bold py-2";
        }
    </script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>