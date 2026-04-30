<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%
    // Session Security Check
    if (session.getAttribute("user") == null) { 
        response.sendRedirect("index.html"); 
        return; 
    }
    
    // Retrieve data forwarded from TimetableServlet
    List<Map<String, String>> data = (List<Map<String, String>>) request.getAttribute("timetableData");
    if (data == null) data = new ArrayList<>(); 
%>
<!DOCTYPE html>
<html>
<head>
    <title>EduTrack | Enrollment & Management</title>
    <style>
        :root { 
            --primary: #3498db; 
            --success: #2ecc71; 
            --danger: #e74c3c; 
            --dark: #333; 
            --gray: #95a5a6;
            --planner-blue: #5b9bd5; 
        }
        
        body { 
            font-family: 'Segoe UI', Arial, sans-serif; 
            background: #eaeeef; 
            margin: 0; 
            padding: 40px; 
        }

        .page-header { 
            text-align: center; 
            font-size: 42px; 
            color: #444; 
            margin-bottom: 20px; 
            font-weight: bold; 
        }

        .container { 
            max-width: 1100px; 
            margin: 0 auto; 
            background: white; 
            padding: 25px; 
            border-radius: 8px; 
            box-shadow: 0 4px 15px rgba(0,0,0,0.1); 
        }
        
        /* Message Styles */
        .alert { padding: 15px; border-radius: 8px; margin-bottom: 20px; font-weight: bold; border: 1px solid; }
        .alert-error { background: #fee2e2; color: #b91c1c; border-color: #f87171; }
        .alert-success { background: #d1fae5; color: #065f46; border-color: #34d399; }

        .nav-bar { 
            display: flex; 
            justify-content: space-between; 
            align-items: center;
            margin-bottom: 25px; 
            padding-bottom: 15px;
            border-bottom: 1px solid #eee;
        }

        .btn-back { 
            text-decoration: none; 
            background: var(--gray); 
            color: white; 
            padding: 10px 18px; 
            border-radius: 4px; 
            font-weight: bold; 
            transition: 0.3s;
        }

        .btn-add { 
            background: var(--primary); 
            color: white; 
            border: none; 
            padding: 12px 25px; 
            border-radius: 4px; 
            cursor: pointer; 
            font-weight: bold; 
            transition: 0.3s;
        }

        .view-footer {
            margin-top: 30px;
            text-align: center; 
            padding-top: 20px;
            border-top: 1px solid #f9f9f9;
        }

        .btn-view-small { 
            text-decoration: none; 
            color: var(--planner-blue); 
            font-weight: 600; 
            font-size: 16px; 
            display: inline-flex;
            align-items: center;
            gap: 8px;
            transition: opacity 0.2s;
        }

        table { width: 100%; border-collapse: collapse; }
        th { background: var(--dark); color: white; padding: 15px; text-align: left; font-size: 14px; }
        td { padding: 15px; border-bottom: 1px solid #eee; font-size: 14px; color: #555; }
        .course-code { color: var(--primary); font-weight: bold; }

        .btn-drop { 
            background: var(--danger); 
            color: white; 
            border: none; 
            padding: 6px 12px; 
            border-radius: 3px; 
            cursor: pointer; 
            text-decoration: none; 
            font-size: 12px; 
        }

        .modal { display: none; position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.6); z-index: 1000; }
        .modal-content { background: white; width: 450px; margin: 60px auto; padding: 30px; border-radius: 8px; position: relative; }
        select, input, textarea { width: 100%; padding: 12px; margin: 10px 0; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box; font-size: 14px; }
        input[readonly], textarea[readonly] { background: #f4f4f4; color: #555; border: 1px dashed #ccc; }
        label { font-weight: bold; color: #444; font-size: 14px; }
    </style>
</head>
<body> 

    <div class="page-header">Class & Timetable Management</div>

    <div class="container">
        <%
            String error = request.getParameter("error");
            String msg = request.getParameter("msg");
            if (error != null) { %>
                <div class="alert alert-error">⚠️ Error: <%= error %></div>
        <%  } if (msg != null) { %>
                <div class="alert alert-success">✅ Success: <%= msg %></div>
        <%  } %>

        <div class="nav-bar">
            <a href="dashboard.jsp" class="btn-back">← Back to Dashboard</a>
            <button class="btn-add" onclick="document.getElementById('addModal').style.display='block'">+ Enrol New Course</button>
        </div>

        <table>
            <thead>
                <tr>
                    <th>#</th>
                    <th>Course Code & Section</th>
                    <th>Description</th>
                    <th>Time Slot</th>
                    <th>Duration</th>
                    <th>Instructor</th>
                    <th style="text-align:center;">Action</th>
                </tr>
            </thead>
            <tbody>
                <% 
                    int count = 1; 
                    if(data.isEmpty()) { 
                %>
                    <tr><td colspan="7" style="text-align:center; padding: 30px; color: #999;">No courses enrolled yet.</td></tr>
                <% 
                    } else {
                        for(Map<String, String> row : data) { 
                %>
                <tr>
                    <td><%= count++ %></td>
                    <td class="course-code"><%= row.get("code") %></td>
                    <td><%= row.get("description") %></td>
                    <td><span style="background: #f1f8ff; padding: 4px 8px; border-radius: 4px; color: #005cc5;"><%= row.get("slot") %></span></td>
                    <td><%= row.get("duration") %> mins</td>
                    <td><%= row.get("instructor") %></td>
                    <td style="text-align:center;">
                        <a href="TimetableServlet?operation=drop&classCode=<%= row.get("code") %>" 
                           class="btn-drop" onclick="return confirm('Are you sure?')">Drop</a>
                    </td>
                </tr>
                <% 
                        } 
                    }
                %>
            </tbody>
        </table>

        <div class="view-footer">
            <a href="TimetableServlet?operation=viewByDay" class="btn-view-small">
                📅 View Weekly Planner
            </a>
        </div>
    </div>

    <div id="addModal" class="modal">
        <div class="modal-content">
            <h3 style="margin-top: 0;">Enroll in a Course</h3>
            <form action="TimetableServlet" method="POST">
                <input type="hidden" name="operation" value="add">
                
                <label>Select Course:</label>
                <select name="classCode" id="courseSelect" onchange="pickSection()" required>
                    <option value="" disabled selected>-- Choose --</option>
                    <optgroup label="CS204 - Web Programming">
                       <option value="CS204 Web Programming (S1)" data-desc="Frontend Basics" data-time="Mon 09:00" data-dur="180" data-inst="Prof. Nyiam Zi Kin">CS204 Web Programming (Sec 01)</option>
                       <option value="CS204 Web Programming (S2)" data-desc="Frontend Basics" data-time="Tue 14:00" data-dur="180" data-inst="Prof. Nyiam Zi Kin">CS204 Web Programming (Sec 02)</option>
                       <option value="CS204 Web Programming (ST)" data-desc="Web Lab" data-time="Fri 11:00" data-dur="120" data-inst="Tutor Ali">CS204 Web Programming (Sec T)</option>
                    </optgroup>

                    <optgroup label="CS205 - Database Systems">
                       <option value="CS205 Database Systems (S1)" data-desc="SQL & Modeling" data-time="Tue 11:00" data-dur="120" data-inst="Dr. Siti binti Ali">CS205 Database Systems (Sec 01)</option>
                       <option value="CS205 Database Systems (S2)" data-desc="SQL & Modeling" data-time="Thu 09:00" data-dur="120" data-inst="Dr. Siti binti Ali">CS205 Database Systems (Sec 02)</option>
                       <option value="CS205 Database Systems (ST)" data-desc="DB Lab" data-time="Mon 10:00" data-dur="120" data-inst="Mr. Wan">CS205 Database Systems (Sec T)</option>
                    </optgroup>

                     <optgroup label="CS206 - Computer Architecture">
                       <option value="CS206 Computer Arch (S1)" data-desc="CPUs & Memory" data-time="Wed 09:00" data-dur="120" data-inst="Prof. Tan Saw Chin">CS206 Computer Arch (Sec 01)</option>
                       <option value="CS206 Computer Arch (S2)" data-desc="CPUs & Memory" data-time="Fri 14:00" data-dur="120" data-inst="Prof. Tan Saw Chin">CS206 Computer Arch (Sec 02)</option>
                    </optgroup> 

                    <optgroup label="CS207 - Data Structures">
                       <option value="CS207 Data Structures (S1)" data-desc="Lists & Trees" data-time="Wed 14:00" data-dur="180" data-inst="Dr. Lim Siew Ling">CS207 Data Structures (Sec 01)</option>
                    </optgroup>
                </select>

                <label>Description:</label>
                <textarea id="d_desc" name="description" rows="2" readonly></textarea>

                <label>Time Slot:</label>
                <input type="text" name="timeSlot" id="d_time" readonly>

                <div style="display: flex; gap: 10px;">
                    <div style="flex: 1;">
                        <label>Duration:</label>
                        <input type="text" name="duration" id="d_dur" readonly>
                    </div>
                    <div style="flex: 1;">
                        <label>Instructor:</label>
                        <input type="text" name="instructor" id="d_inst" readonly>
                    </div>
                </div>

                <button type="submit" style="background: var(--primary); color: white; border: none; padding: 14px; width: 100%; border-radius: 4px; font-weight: bold; cursor: pointer; margin-top: 10px;">Enrol Now</button>
                <button type="button" onclick="document.getElementById('addModal').style.display='none'" style="width: 100%; background: none; border: none; color: #888; margin-top: 10px; cursor: pointer;">Cancel</button>
            </form>
        </div>
    </div>

    <script>
        function pickSection() {
            var sel = document.getElementById("courseSelect");
            var opt = sel.options[sel.selectedIndex];
            
            // Populate the fields - fixes the ABC123/null duration issue
            document.getElementById("d_desc").value = opt.getAttribute("data-desc") || "";
            document.getElementById("d_time").value = opt.getAttribute("data-time") || "";
            document.getElementById("d_dur").value = opt.getAttribute("data-dur") || "";
            document.getElementById("d_inst").value = opt.getAttribute("data-inst") || "";
        }
    </script>
</body>
</html>