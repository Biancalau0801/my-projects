<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    if (session.getAttribute("user") == null || !"Lecturer".equals(session.getAttribute("role"))) {
        response.sendRedirect("index.html?error=unauthorized"); return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>EduTrack | Geofence Setup</title>
    <style>
        :root { --primary: #10b981; --dark: #0f172a; --slate: #64748b; }
        body { 
            font-family: 'Inter', sans-serif; background: radial-gradient(circle at top right, #f8fafc, #cbd5e1); 
            margin: 0; padding: 40px; display: flex; flex-direction: column; align-items: center; min-height: 100vh;
        }
        .glass-card {
            background: rgba(255, 255, 255, 0.8); backdrop-filter: blur(10px);
            border: 1px solid rgba(255, 255, 255, 0.3); padding: 40px;
            border-radius: 24px; box-shadow: 0 10px 15px -3px rgba(0,0,0,0.1);
            text-align: center; max-width: 400px; width: 100%;
        }
        .btn {
            background: var(--dark); color: white; border: none; padding: 14px;
            border-radius: 12px; cursor: pointer; font-weight: 600; width: 100%; transition: 0.3s;
        }
        .status-active { color: var(--primary); font-weight: bold; margin-top: 20px; font-family: monospace; }
    </style>
</head>
<body>
    <div class="glass-card">
        <h2 style="color: var(--dark); margin-bottom: 10px;">Geofencing</h2>
        <p style="color: var(--slate); font-size: 0.9rem;">Lock attendance to your current GPS location.</p>
        
        <div style="margin: 20px 0; text-align: left;">
            <label style="font-weight: 600;">Select Subject:</label>
            <select id="subjectSelect" style="width: 100%; padding: 10px; margin-bottom: 15px; border-radius: 8px; border: 1px solid #ddd;">
                <option value="CS204 Web Programming (S1)">CS204 Web Programming (S1)</option>
                <option value="CS205 Database Systems (S2)">CS205 Database Systems (S2)</option>
            </select>

            <label style="font-weight: 600;">Attendance Date:</label>
            <input type="date" id="attDate" style="width: 100%; padding: 10px; border-radius: 8px; border: 1px solid #ddd;">
        </div>

        <button class="btn" onclick="activateGeofence()">Set Location & Activate</button>

        <div id="statusDisplay" style="display:none; margin-top: 20px; padding: 15px; background: #ecfdf5; border: 1px solid #10b981; border-radius: 12px; color: #065f46; text-align: left;">
            <p style="margin: 0; font-weight: bold; color: #059669;">✅ Geofence Active</p>
        <div id="details" style="font-family: monospace; font-size: 0.85rem; margin-top: 5px; line-height: 1.4;">
        </div>
    </div>

    <a href="lecturer_dashboard.jsp" style="margin-top: 30px; color: var(--slate); text-decoration: none;">← Back</a>

    <script>
    // Set default date to today automatically
    document.getElementById('attDate').valueAsDate = new Date();

    function activateGeofence() {
        // 1. Get references to UI elements
        const subject = document.getElementById('subjectSelect').value;
        const date = document.getElementById('attDate').value;
        const display = document.getElementById('statusDisplay');
        const details = document.getElementById('details');

        if (navigator.geolocation) {
            // Provide immediate feedback that we are trying to get GPS
            if(details) details.innerText = "Locating...";
            
            navigator.geolocation.getCurrentPosition(position => {
                const lat = position.coords.latitude;
                const lon = position.coords.longitude;

                // 2. SHOW COORDINATES ON PAGE
                // We use backslashes \${} to prevent JSP from getting confused
                if(display && details) {
                    display.style.display = "block";
                    details.innerHTML = `
                        <b>Subject:</b> \${subject}<br>
                        <b>Latitude:</b> \${lat.toFixed(6)}<br>
                        <b>Longitude:</b> \${lon.toFixed(6)}<br>
                        <span style="color: #059669;">● Connection established</span>
                    `;
                }

                // 3. THE FETCH CALL: This sends the data to RecordAttendanceServlet.java
                // action=setTarget triggers the doGet method in your Java file
                const url = "RecordAttendanceServlet?action=setTarget" + 
                            "&lat=" + lat + 
                            "&lon=" + lon + 
                            "&courseId=" + encodeURIComponent(subject) + 
                            "&date=" + date;
                
                fetch(url)
                .then(response => {
                    if (response.ok) {
                        alert("Database Updated: Geofence is now LIVE.");
                    } else {
                        alert("GPS found, but Server failed to save data.");
                    }
                })
                .catch(err => {
                    console.error("Fetch error:", err);
                    alert("Network error: Could not connect to Servlet.");
                });

            }, err => {
                alert("GPS Error: Please ensure location is enabled and you are using 'localhost' or 'https'.");
            }, { 
                enableHighAccuracy: true, 
                timeout: 20000
            });
        } else {
            alert("Geolocation is not supported by this browser.");
        }
    }
</script>
</body>
</html>