<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    if (session.getAttribute("user") == null || !"Lecturer".equals(session.getAttribute("role"))) {
        response.sendRedirect("index.html?error=unauthorized"); return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <title>EduTrack | QR Generation</title>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/qrcodejs/1.0.0/qrcode.min.js"></script>
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
            background: var(--primary); color: white; border: none; padding: 14px;
            border-radius: 12px; cursor: pointer; font-weight: 600; width: 100%; transition: 0.3s;
        }
        #qrcode { margin: 25px auto; padding: 15px; background: white; border-radius: 12px; display: inline-block; }
    </style>
</head>
<body>
    <div class="glass-card">
        <h2 style="color: var(--dark);">Generate Session QR</h2>
        
        <div style="margin: 20px 0; text-align: left;">
            <label>Subject:</label>
            <select id="subjectSelect" style="width: 100%; padding: 10px; margin-bottom: 10px;">
                <option value="CS204 Web Programming (S1)">CS204 Web Programming (S1)</option>
                <option value="CS205 Database Systems (S2)">CS205 Database Systems (S2)</option>
            </select>

            <label>Date:</label>
            <input type="date" id="attDate" style="width: 100%; padding: 10px;">
        </div>
        
        <div id="qrcode"></div>
        
        <button class="btn" onclick="createQR()">Generate QR Code</button>
    </div>

    <script>
        document.getElementById('attDate').valueAsDate = new Date();

        function createQR() {
            const subject = document.getElementById('subjectSelect').value;
            const date = document.getElementById('attDate').value;
            const container = document.getElementById("qrcode");
            
            container.innerHTML = ""; // Clear old QR
            
            // Format: Subject|Date (e.g., CS204-S1|2023-10-25)
            const qrData = subject + "|" + date;
            
            new QRCode(container, {
                text: qrData,
                width: 200, height: 200
            });
            
            // Also update the server's geofence center automatically when QR is made
            navigator.geolocation.getCurrentPosition(p => {
                fetch(`RecordAttendanceServlet?action=setTarget&lat=${p.coords.latitude}&lon=${p.coords.longitude}&courseId=${subject}`);
            });
        }
    </script>
</body>
</html>