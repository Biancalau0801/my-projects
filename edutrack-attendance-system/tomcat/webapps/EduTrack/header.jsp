<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<nav class="navbar">
    <div class="navbar-container">
        <div class="navbar-brand">
            <h2>📚 EduTrack</h2>
        </div>
        
        <ul class="navbar-menu">
            <%
            String userRole = (String) session.getAttribute("role");
            
            if ("Programme Coordinator".equals(userRole) || userRole == null) {
            %>
                <li><a href="coordinator-dashboard.jsp">Dashboard</a></li>
                <li><a href="generate-report.jsp">Reports</a></li>
                <li><a href="notification-rules.jsp">Notification Rules</a></li>
            <%
            }
            %>
            
            <li><a href="LogoutServlet">Logout</a></li>
        </ul>
    </div>
</nav>

<style>
.navbar {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    padding: 0;
    box-shadow: 0 2px 10px rgba(0,0,0,0.1);
}

.navbar-container {
    max-width: 1400px;
    margin: 0 auto;
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 15px 30px;
}

.navbar-brand h2 {
    color: white;
    font-size: 24px;
    margin: 0;
}

.navbar-menu {
    list-style: none;
    display: flex;
    gap: 30px;
    margin: 0;
    padding: 0;
}

.navbar-menu li a {
    color: white;
    text-decoration: none;
    font-weight: 600;
    font-size: 15px;
    transition: opacity 0.3s;
}

.navbar-menu li a:hover {
    opacity: 0.8;
}

@media (max-width: 768px) {
    .navbar-container {
        flex-direction: column;
        gap: 15px;
    }
    
    .navbar-menu {
        flex-direction: column;
        text-align: center;
        gap: 10px;
    }
}
</style>