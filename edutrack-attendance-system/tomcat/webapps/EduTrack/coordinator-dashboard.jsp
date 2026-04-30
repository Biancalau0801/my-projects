<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <link rel="stylesheet" href="main.css">
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Coordinator Dashboard - EduTrack</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
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
            max-width: 1400px;
            margin: 0 auto;
        }
        
        .header {
            background: white;
            padding: 25px;
            border-radius: 10px;
            margin-bottom: 30px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
        }
        
        .header h1 {
            color: #333;
            font-size: 28px;
            margin-bottom: 5px;
        }
        
        .header p {
            color: #666;
            font-size: 14px;
        }
        
        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }
        
        .stat-card {
            background: white;
            padding: 25px;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
            position: relative;
            overflow: hidden;
        }
        
        .stat-card::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 4px;
        }
        
        .stat-card:nth-child(1)::before {
            background: linear-gradient(90deg, #667eea, #764ba2);
        }
        
        .stat-card:nth-child(2)::before {
            background: linear-gradient(90deg, #f093fb, #f5576c);
        }
        
        .stat-card:nth-child(3)::before {
            background: linear-gradient(90deg, #4facfe, #00f2fe);
        }
        
        .stat-label {
            color: #888;
            font-size: 14px;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            margin-bottom: 10px;
        }
        
        .stat-value {
            color: #333;
            font-size: 42px;
            font-weight: bold;
            margin-bottom: 5px;
        }
        
        .stat-subtitle {
            color: #999;
            font-size: 12px;
        }
        
        .charts-container {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(500px, 1fr));
            gap: 20px;
            margin-bottom: 30px;
        }
        
        .chart-card {
            background: white;
            padding: 25px;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
        }
        
        .chart-card h3 {
            color: #333;
            margin-bottom: 20px;
            font-size: 18px;
        }
        
        .chart-wrapper {
            position: relative;
            height: 300px;
        }
        
        .filters {
            background: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.1);
            margin-bottom: 20px;
        }
        
        .filters h3 {
            color: #333;
            margin-bottom: 15px;
            font-size: 18px;
        }
        
        .filter-group {
            display: flex;
            gap: 15px;
            flex-wrap: wrap;
            align-items: center;
        }
        
        .filter-group label {
            color: #666;
            font-size: 14px;
            font-weight: 600;
        }
        
        .filter-group select,
        .filter-group input {
            padding: 10px 15px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 14px;
            outline: none;
            transition: border-color 0.3s;
        }
        
        .filter-group select:focus,
        .filter-group input:focus {
            border-color: #667eea;
        }
        
        .filter-group button {
            padding: 10px 25px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            border: none;
            border-radius: 5px;
            font-size: 14px;
            font-weight: 600;
            cursor: pointer;
            transition: transform 0.2s;
        }
        
        .filter-group button:hover {
            transform: translateY(-2px);
        }
        
        .loading {
            text-align: center;
            padding: 40px;
            color: white;
            font-size: 18px;
        }
        
        .error {
            background: #ff4757;
            color: white;
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
            display: none;
        }
        
        @media (max-width: 768px) {
            .charts-container {
                grid-template-columns: 1fr;
            }
            
            .filter-group {
                flex-direction: column;
                align-items: stretch;
            }
        }
    </style>
</head>
<body>
    <jsp:include page="header.jsp" />
    <div class="container">
        <div class="header">
            <h1>📊 Programme Coordinator Dashboard</h1>
            <p>Welcome back! Here's your programme overview.</p>
        </div>
        
        <div class="error" id="errorMessage"></div>
        
        <div class="loading" id="loading">Loading dashboard data...</div>
        
        <div id="dashboardContent" style="display: none;">
            <div class="stats-grid">
                <div class="stat-card">
                    <div class="stat-label">Total Students</div>
                    <div class="stat-value" id="totalStudents">-</div>
                    <div class="stat-subtitle">Active enrollments</div>
                </div>
                
                <div class="stat-card">
                    <div class="stat-label">Average Attendance</div>
                    <div class="stat-value" id="avgAttendance">-</div>
                    <div class="stat-subtitle">Programme-wide average</div>
                </div>
                
                <div class="stat-card">
                    <div class="stat-label">At-Risk Students</div>
                    <div class="stat-value" id="atRisk">-</div>
                    <div class="stat-subtitle">Below 75% attendance</div>
                </div>
            </div>
            
            <div class="filters">
                <h3>📅 Filters</h3>
                <div class="filter-group">
                    <label>Programme:</label>
                    <select id="programmeFilter">
                        <option value="all">All Programmes</option>
                        <option value="CSE">Computer Science & Engineering</option>
                        <option value="IT">Information Technology</option>
                        <option value="CS">Computer Science</option>
                    </select>
                    
                    <label>Date From:</label>
                    <input type="date" id="dateFrom">
                    
                    <label>Date To:</label>
                    <input type="date" id="dateTo">
                    
                    <button onclick="applyFilters()">Apply Filters</button>
                    <button onclick="resetFilters()" style="background: #95a5a6;">Reset</button>
                </div>
            </div>
            
            <div class="charts-container">
                <div class="chart-card">
                    <h3>📈 Attendance Trend (Last 7 Days)</h3>
                    <div class="chart-wrapper">
                        <canvas id="trendChart"></canvas>
                    </div>
                </div>
                
                <div class="chart-card">
                    <h3>📊 Attendance Distribution</h3>
                    <div class="chart-wrapper">
                        <canvas id="distributionChart"></canvas>
                    </div>
                </div>
            </div>
        </div>
    
    <script>
        let trendChart = null;
        let distributionChart = null;
        
        // Load dashboard data
        function loadDashboard() {
            fetch('/EduTrack/CoordinatorDashboardServlet')
                .then(response => {
                    if (!response.ok) {
                        throw new Error('Network response was not ok');
                    }
                    return response.json();
                })
                .then(data => {
                    if (data.error) {
                        showError('Error: ' + data.error);
                        return;
                    }
                    
                    // Update statistics
                    document.getElementById('totalStudents').textContent = data.totalStudents;
                    document.getElementById('avgAttendance').textContent = data.avgAttendance + '%';
                    document.getElementById('atRisk').textContent = data.atRisk;
                    
                    // Hide loading, show content
                    document.getElementById('loading').style.display = 'none';
                    document.getElementById('dashboardContent').style.display = 'block';
                    
                    // Create charts
                    createTrendChart(data.trendData);
                    createDistributionChart(data.avgAttendance);
                })
                .catch(error => {
                    console.error('Error:', error);
                    showError('Failed to load dashboard data. Please check if Tomcat is running and database is accessible.');
                });
        }
        
        // Create trend chart
        function createTrendChart(trendData) {
            const ctx = document.getElementById('trendChart').getContext('2d');
            
            if (trendChart) {
                trendChart.destroy();
            }
            
            const dates = trendData.map(item => item.date);
            const rates = trendData.map(item => item.rate);
            
            trendChart = new Chart(ctx, {
                type: 'line',
                data: {
                    labels: dates,
                    datasets: [{
                        label: 'Attendance Rate (%)',
                        data: rates,
                        borderColor: 'rgb(102, 126, 234)',
                        backgroundColor: 'rgba(102, 126, 234, 0.1)',
                        tension: 0.4,
                        fill: true,
                        pointRadius: 5,
                        pointHoverRadius: 7
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            display: true,
                            position: 'top'
                        }
                    },
                    scales: {
                        y: {
                            beginAtZero: true,
                            max: 100,
                            ticks: {
                                callback: function(value) {
                                    return value + '%';
                                }
                            }
                        }
                    }
                }
            });
        }
        
        // Create distribution chart
        function createDistributionChart(avgAttendance) {
            const ctx = document.getElementById('distributionChart').getContext('2d');
            
            if (distributionChart) {
                distributionChart.destroy();
            }
            
            const presentRate = parseFloat(avgAttendance);
            const absentRate = 100 - presentRate;
            
            distributionChart = new Chart(ctx, {
                type: 'doughnut',
                data: {
                    labels: ['Present/On Time', 'Absent/Late'],
                    datasets: [{
                        data: [presentRate, absentRate],
                        backgroundColor: [
                            'rgba(102, 126, 234, 0.8)',
                            'rgba(245, 87, 108, 0.8)'
                        ],
                        borderWidth: 2,
                        borderColor: '#fff'
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            position: 'bottom'
                        }
                    }
                }
            });
        }
        
        // Apply filters
        function applyFilters() {
            const programme = document.getElementById('programmeFilter').value;
            const dateFrom = document.getElementById('dateFrom').value;
            const dateTo = document.getElementById('dateTo').value;
            
            console.log('Applying filters:', { programme, dateFrom, dateTo });
            
            // TODO: Implement filter logic - reload data with filters
            alert('Filters applied! (Feature to be implemented in next phase)');
        }
        
        // Reset filters
        function resetFilters() {
            document.getElementById('programmeFilter').value = 'all';
            document.getElementById('dateFrom').value = '';
            document.getElementById('dateTo').value = '';
            loadDashboard();
        }
        
        // Show error message
        function showError(message) {
            const errorDiv = document.getElementById('errorMessage');
            errorDiv.textContent = message;
            errorDiv.style.display = 'block';
            document.getElementById('loading').style.display = 'none';
        }
        
        // Load dashboard on page load
        window.onload = function() {
            loadDashboard();
        };
    </script>
</body>
</html>