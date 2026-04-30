<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>EduTrack | Record Attendance</title>
    <script src="https://unpkg.com/html5-qrcode"></script>
    <style>
        :root { --primary: #3b82f6; --dark: #1e293b; --slate: #64748b; --danger: #ef4444; --success: #10b981; --border: #e2e8f0; }
        body { font-family: 'Inter', sans-serif; background: #f1f5f9; display: flex; align-items: center; justify-content: center; min-height: 100vh; margin: 0; }
        .modal { background: white; width: 90%; max-width: 450px; border-radius: 24px; box-shadow: 0 20px 25px -5px rgba(0,0,0,0.1); padding: 30px; text-align: center; position: relative; }
        
        /* New styling for the Back Button */
        .back-nav { 
            display: block; 
            text-align: left; 
            text-decoration: none; 
            color: var(--slate); 
            font-size: 0.9rem; 
            font-weight: 600; 
            margin-bottom: 20px; 
            transition: color 0.2s;
        }
        .back-nav:hover { color: var(--dark); }

        .btn-upload { background: var(--primary); color: white; border: none; padding: 15px 25px; border-radius: 12px; cursor: pointer; font-weight: 600; width: 100%; font-size: 1rem; }
        .status-card { margin-top: 20px; padding: 15px; border-radius: 12px; font-size: 0.9rem; display: none; }
        .info { background: #e0f2fe; color: #0369a1; }
        .error { background: #fee2e2; color: #b91c1c; }
        .success { background: #dcfce7; color: #15803d; }
    </style>
</head>
<body>

<div class="modal">
    <a href="dashboard.jsp" class="back-nav">← Back to Dashboard</a>

    <h2 style="color: var(--dark); margin-top: 0;">Record Attendance</h2>
    
    <div style="text-align: left; margin-bottom: 20px;">
        <label style="font-size: 0.8rem; font-weight: bold; color: var(--slate);">SELECT SUBJECT:</label>
        <select id="manualSubject" style="width: 100%; padding: 12px; border-radius: 10px; border: 1px solid var(--border); margin-top: 5px;">
            <option value="CS204 Web Programming (S1)">CS204 Web Programming (S1)</option>
            <option value="CS205 Database Systems (S2)">CS205 Database Systems (S2)</option>
        </select>
    </div>

    <div style="display: flex; flex-direction: column; gap: 12px;">
        <button class="btn-upload" style="background: #10b981;" onclick="usePureGeolocation()">
            📍 Verify by Location Only
        </button>

        <div style="color: var(--slate); font-size: 0.7rem; font-weight: bold; margin: 5px 0;">— OR USE QR —</div>
        
        <button class="btn-upload" onclick="document.getElementById('qr-input').click()">
            📷 Upload QR Screenshot
        </button>
    </div>

    <input type="file" id="qr-input" accept="image/*" style="display:none;">
    <div id="reader" style="display:none;"></div>
    <div id="status-message" class="status-card"></div>
</div>

<script>
    // ... rest of your existing JavaScript logic ...
    const html5QrCode = new Html5Qrcode("reader");

    function usePureGeolocation() {
        const subject = document.getElementById('manualSubject').value;
        const today = new Date().toISOString().split('T')[0];
        showStatus("Step 1: Requesting GPS Signal...", "info");

        navigator.geolocation.getCurrentPosition(
            (position) => {
                const lat = position.coords.latitude;
                const lon = position.coords.longitude;
                showStatus(`Step 2: Location Found (${lat.toFixed(4)})`, "info");
                submitAttendance(subject, today, lat, lon);
            },
            (error) => {
                let msg = "GPS Error: ";
                if (error.code === 1) msg += "Permission Denied.";
                if (error.code === 2) msg += "Position Unavailable.";
                if (error.code === 3) msg += "Timeout.";
                showStatus(msg, "error");
            }, 
            { timeout: 5000 }
        );
    }

    document.getElementById('qr-input').addEventListener('change', e => {
        if (e.target.files.length === 0) return;
        const imageFile = e.target.files[0];
        showStatus("Reading QR Code...", "info");

        html5QrCode.scanFile(imageFile, true)
            .then(decodedText => {
                const parts = decodedText.split('|');
                const subject = parts[0]; 
                const date = parts[1] || new Date().toISOString().split('T')[0];
                showStatus("QR Found! Getting location...", "info");
                navigator.geolocation.getCurrentPosition(pos => {
                    submitAttendance(subject, date, pos.coords.latitude, pos.coords.longitude);
                }, err => {
                    showStatus("Location required to verify QR.", "error");
                });
            })
            .catch(err => showStatus("Invalid QR Code.", "error"));
    });

    function submitAttendance(subject, date, lat, lon) {
        const form = document.createElement('form');
        form.method = 'POST';
        form.action = 'RecordAttendanceServlet';
        const params = { 'courseId': subject, 'attendanceDate': date, 'lat': lat, 'lon': lon };
        for (const key in params) {
            const input = document.createElement('input');
            input.type = 'hidden';
            input.name = key;
            input.value = params[key];
            form.appendChild(input);
        }
        document.body.appendChild(form);
        form.submit();
    }

    function showStatus(msg, type) {
        const status = document.getElementById('status-message');
        status.style.display = 'block';
        status.className = 'status-card ' + type;
        status.innerHTML = msg;
    }
</script>
</body>
</html>