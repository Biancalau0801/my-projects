<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Generate Report - EduTrack</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 20px;
        }
        
        .container {
            max-width: 800px;
            margin: 0 auto;
        }
        
        .card {
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
        }
        
        h1 {
            color: #333;
            margin-bottom: 10px;
            font-size: 28px;
        }
        
        .subtitle {
            color: #666;
            margin-bottom: 30px;
            font-size: 14px;
        }
        
        .form-group {
            margin-bottom: 20px;
        }
        
        label {
            display: block;
            color: #333;
            font-weight: 600;
            margin-bottom: 8px;
            font-size: 14px;
        }
        
        select, input[type="date"] {
            width: 100%;
            padding: 12px 15px;
            border: 2px solid #e0e0e0;
            border-radius: 5px;
            font-size: 14px;
            outline: none;
            transition: border-color 0.3s;
        }
        
        select:focus, input[type="date"]:focus {
            border-color: #667eea;
        }
        
        .button-group {
            display: flex;
            gap: 15px;
            margin-top: 30px;
        }
        
        button {
            flex: 1;
            padding: 15px;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: transform 0.2s, box-shadow 0.2s;
        }
        
        button:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(0,0,0,0.15);
        }
        
        .btn-excel {
            background: linear-gradient(135deg, #4CAF50 0%, #45a049 100%);
            color: white;
        }
        
        .btn-pdf {
            background: linear-gradient(135deg, #ff6b6b 0%, #ee5a6f 100%);
            color: white;
        }
        
        .back-link {
            display: inline-block;
            margin-top: 20px;
            color: white;
            text-decoration: none;
            padding: 10px 20px;
            background: rgba(255,255,255,0.2);
            border-radius: 5px;
            transition: background 0.3s;
        }
        
        .back-link:hover {
            background: rgba(255,255,255,0.3);
        }
        
        .info-box {
            background: #e3f2fd;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
            border-left: 4px solid #2196F3;
        }
        
        .info-box h3 {
            color: #1976D2;
            margin-bottom: 5px;
            font-size: 14px;
        }
        
        .info-box p {
            color: #555;
            font-size: 13px;
            line-height: 1.5;
        }
        
        @media (max-width: 768px) {
            .button-group {
                flex-direction: column;
            }
        }
    </style>
</head>
<body>
    <jsp:include page="header.jsp" />
    <div class="container">
        <div class="card">
            <h1>📊 Generate Attendance Report</h1>
            <p class="subtitle">Export attendance data to Excel or PDF format</p>
            
            <div class="info-box">
                <h3>📌 Report Information</h3>
                <p>This report includes: Student ID, Name, Course, Total Classes, Present, Absent, Late, and Attendance Percentage. Choose your preferred format below.</p>
            </div>
            
            <form id="reportForm" method="POST">
                <div class="form-group">
                    <label for="programmeCode">Programme:</label>
                    <select id="programmeCode" name="programmeCode" required>
                        <option value="all">All Programmes</option>
                        <option value="CSE">Computer Science & Engineering</option>
                        <option value="IT">Information Technology</option>
                        <option value="CS">Computer Science</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label for="dateFrom">Date From:</label>
                    <input type="date" id="dateFrom" name="dateFrom" required>
                </div>
                
                <div class="form-group">
                    <label for="dateTo">Date To:</label>
                    <input type="date" id="dateTo" name="dateTo" required>
                </div>
                
                <div class="button-group">
                    <button type="button" class="btn-excel" onclick="generateReport('excel')">
                        📗 Generate Excel Report
                    </button>
                    <button type="button" class="btn-pdf" onclick="generateReport('pdf')">
                        📕 Generate PDF Report
                    </button>
                </div>
            </form>
        </div>
    </div>
    
    <script>
        // Set default dates (last 30 days)
        const today = new Date();
        const thirtyDaysAgo = new Date(today.getTime() - (30 * 24 * 60 * 60 * 1000));
        
        document.getElementById('dateTo').valueAsDate = today;
        document.getElementById('dateFrom').valueAsDate = thirtyDaysAgo;
        
        function generateReport(type) {
            const form = document.getElementById('reportForm');
            const programmeCode = document.getElementById('programmeCode').value;
            const dateFrom = document.getElementById('dateFrom').value;
            const dateTo = document.getElementById('dateTo').value;
            
            // Validation
            if (!dateFrom || !dateTo) {
                alert('Please select both start and end dates');
                return;
            }
            
            if (new Date(dateFrom) > new Date(dateTo)) {
                alert('Start date must be before end date');
                return;
            }
            
            // Create form and submit
            const downloadForm = document.createElement('form');
            downloadForm.method = 'POST';
            downloadForm.action = '<%= request.getContextPath() %>/generateReport';
            
            const fields = {
                reportType: type,
                programmeCode: programmeCode,
                dateFrom: dateFrom,
                dateTo: dateTo
            };
            
            for (const [key, value] of Object.entries(fields)) {
                const input = document.createElement('input');
                input.type = 'hidden';
                input.name = key;
                input.value = value;
                downloadForm.appendChild(input);
            }
            
            document.body.appendChild(downloadForm);
            downloadForm.submit();
            document.body.removeChild(downloadForm);
            
            // Show feedback
            const reportType = type === 'excel' ? 'Excel' : 'PDF';
            alert(`${reportType} report generation started! Your download should begin shortly.`);
        }
    </script>
</body>
</html>
