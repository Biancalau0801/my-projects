<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>

<%
String messageAttr = (String) request.getAttribute("message");
String errorAttr   = (String) request.getAttribute("error");

String successParam = request.getParameter("success");
String errorParam   = request.getParameter("error");

String successMsg = null;
if (successParam != null) {
    if ("created".equals(successParam)) successMsg = "Rule created successfully!";
    else if ("deleted".equals(successParam)) successMsg = "Rule deleted successfully!";
    else if ("updated".equals(successParam)) successMsg = "Rule updated successfully!";
}
%>
<%
    if (session.getAttribute("user") == null || !"Programme Coordinator".equalsIgnoreCase((String)session.getAttribute("role"))) {
        response.sendRedirect("index.html?error=unauthorized");
        return;
    }
%>


<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Notification Rules - EduTrack</title>
    <style>
        * { margin:0; padding:0; box-sizing:border-box; }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 20px;
        }
        .container { max-width: 1200px; margin: 0 auto; }
        .card {
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
            margin-bottom: 20px;
        }
        h1 { color:#333; margin-bottom:10px; }
        .subtitle { color:#666; margin-bottom:20px; }

        .success-message, .error-message {
            padding: 15px; border-radius: 5px; margin-bottom: 20px;
        }
        .success-message { background:#d4edda; color:#155724; border:1px solid #c3e6cb; }
        .error-message { background:#f8d7da; color:#721c24; border:1px solid #f5c6cb; }

        table { width:100%; border-collapse:collapse; margin-top:20px; }
        th, td { padding:12px; text-align:left; border-bottom:1px solid #ddd; }
        th { background:#667eea; color:white; font-weight:600; }
        tr:hover { background:#f5f5f5; }

        .badge { padding:5px 10px; border-radius:12px; font-size:12px; font-weight:600; }
        .badge-attendance { background:#e3f2fd; color:#1976d2; }
        .badge-lateness { background:#fff3e0; color:#f57c00; }
        .badge-email { background:#e8f5e9; color:#388e3c; }
        .badge-sms { background:#f3e5f5; color:#7b1fa2; }
        .badge-system { background:#fce4ec; color:#c2185b; }

        .btn { padding:8px 15px; border:none; border-radius:5px; cursor:pointer; font-size:13px; font-weight:600; margin-right:5px; transition: transform 0.2s; }
        .btn:hover { transform: translateY(-2px); }
        .btn-delete { background:#f44336; color:white; }

        .form-group { margin-bottom:20px; }
        label { display:block; color:#333; font-weight:600; margin-bottom:8px; font-size:14px; }
        select, input[type="number"] {
            width:100%; padding:12px; border:2px solid #e0e0e0; border-radius:5px; font-size:14px; outline:none;
        }
        select:focus, input:focus { border-color:#667eea; }

        .btn-primary {
            width:100%; padding:15px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color:white; border:none; border-radius:5px; font-size:16px; font-weight:600; cursor:pointer;
        }

        .grid { display:grid; grid-template-columns:2fr 1fr; gap:20px; }
        @media (max-width: 768px) { .grid { grid-template-columns:1fr; } }
    </style>
</head>

<body>
    <jsp:include page="header.jsp" />

    <div class="container">
        <div class="card">
            <h1>🔔 Notification Rules Management</h1>
            <p class="subtitle">Create and manage automated attendance alerts</p>

            <%-- show attribute messages from servlet --%>
            <% if (messageAttr != null) { %>
                <div class="success-message">✓ <%= messageAttr %></div>
            <% } %>
            <% if (errorAttr != null) { %>
                <div class="error-message">✗ <%= errorAttr %></div>
            <% } %>

            <%-- show redirect query-param messages --%>
            <% if (successMsg != null) { %>
                <div class="success-message">✓ <%= successMsg %></div>
            <% } %>
            <% if (errorParam != null) { %>
                <div class="error-message">✗ Error: <%= errorParam %></div>
            <% } %>
        </div>

        <div class="grid">
            <!-- Existing Rules -->
            <div class="card">
                <h2>📋 Existing Rules</h2>
                <table id="rulesTable">
                    <thead>
                        <tr>
                            <th>Programme</th>
                            <th>Type</th>
                            <th>Threshold</th>
                            <th>Method</th>
                            <th>Frequency</th>
                            <th>Last Triggered</th>
                            <th>Actions</th>
                        </tr>
                    </thead>

                    <tbody id="rulesTableBody">
                        <tr>
                            <td colspan="7" style="text-align: center; padding: 40px;">Loading rules...</td>
                        </tr>
                    </tbody>
                </table>
            </div>

            <!-- Create New Rule -->
            <div class="card">
                <h2>➕ Create New Rule</h2>
                <form action="<%= request.getContextPath() %>/NotificationRulesServlet" method="post">
                    <input type="hidden" name="action" value="create">

                    <div class="form-group">
                        <label>Programme:</label>
                        <select name="programmeCode" required>
                            <option value="CSE">Computer Science & Engineering</option>
                            <option value="IT">Information Technology</option>
                            <option value="CS">Computer Science</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label>Threshold Type:</label>
                        <select name="thresholdType" required>
                            <option value="Attendance">Attendance Rate</option>
                            <option value="Lateness">Lateness Rate</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label>Threshold Value (%):</label>
                        <input type="number" name="thresholdValue" min="0" max="100" step="0.1" value="75" required>
                    </div>

                    <div class="form-group">
                        <label>Notification Method:</label>
                        <select name="notificationMethod" required>
                            <option value="System">System Notification</option>
                            <option value="Email">Email</option>
                            <option value="SMS">SMS</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label>Frequency:</label>
                        <select name="frequency" required>
                            <option value="Daily">Daily</option>
                            <option value="Weekly">Weekly</option>
                        </select>
                    </div>

                    <button type="submit" class="btn-primary">Create Rule</button>
                </form>
            </div>
        </div>
    </div>

    <script>
        // If you REALLY have a JSON endpoint, use contextPath and forward slashes.
        // Otherwise, you can remove loadRules() completely because server-side rendering already works.

        function loadRules() {
            fetch('<%= request.getContextPath() %>/NotificationRulesServlet')
                .then(r => {
                    const ct = r.headers.get("content-type") || "";
                    if (!ct.includes("application/json")) throw new Error("Not JSON response");
                    return r.json();
                })
                .then(data => {
                    const tbody = document.getElementById('rulesTableBody');
                    if (!Array.isArray(data) || data.length === 0) {
                        tbody.innerHTML = '<tr><td colspan="7" style="text-align:center; padding:40px;">No rules found. Create one on the right!</td></tr>';
                        return;
                    }

                    tbody.innerHTML = '';
                    data.forEach(rule => {
                        const typeBadgeClass = rule.thresholdType === 'Attendance' ? 'badge-attendance' : 'badge-lateness';
                        const methodBadgeClass = rule.notificationMethod === 'Email' ? 'badge-email'
                            : rule.notificationMethod === 'SMS' ? 'badge-sms' : 'badge-system';

                        const last = rule.lastTriggered ?? '-';

                        const row = document.createElement('tr');
                        row.innerHTML = `
                            <td>${rule.programmeCode}</td>
                            <td><span class="badge ${typeBadgeClass}">${rule.thresholdType}</span></td>
                            <td>${rule.thresholdValue}%</td>
                            <td><span class="badge ${methodBadgeClass}">${rule.notificationMethod}</span></td>
                            <td>${rule.frequency}</td>
                            <td>${last}</td>
                            <td>
                                <button class="btn btn-delete" onclick="deleteRule('${rule.ruleID}')">Delete</button>
                            </td>
                        `;
                        tbody.appendChild(row);
                    });
                })
                .catch(() => {
                    // Ignore if endpoint is not JSON; server-side table already shown.
                });
        }

        function deleteRule(ruleID) {
            if (!confirm('Are you sure you want to delete this rule?')) return;

            const form = document.createElement('form');
            form.method = 'POST';
            form.action = '<%= request.getContextPath() %>/NotificationRulesServlet';

            const actionInput = document.createElement('input');
            actionInput.type = 'hidden';
            actionInput.name = 'action';
            actionInput.value = 'delete';
            form.appendChild(actionInput);

            const ruleIDInput = document.createElement('input');
            ruleIDInput.type = 'hidden';
            ruleIDInput.name = 'ruleID';
            ruleIDInput.value = ruleID;
            form.appendChild(ruleIDInput);

            document.body.appendChild(form);
            form.submit();
        }

        window.onload = function() {
            loadRules(); // safe even if endpoint is not JSON
        };
    </script>
</body>
</html>
