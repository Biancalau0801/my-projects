import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import javax.servlet.annotation.WebServlet;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import javax.servlet.annotation.WebServlet; // This is the most important line
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// Remove the wildcard import and use specific imports
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@WebServlet("generateReport")
public class ReportGenerationServlet extends HttpServlet {
    
    private static final String DB_USER_ID = "COORD001"; // Consider getting from session
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String reportType = request.getParameter("reportType");
        String programmeCode = request.getParameter("programmeCode");
        String dateFrom = request.getParameter("dateFrom");
        String dateTo = request.getParameter("dateTo");
        
        // Validate inputs
        if (reportType == null || reportType.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Report type is required");
            return;
        }
        
        try {
            if ("excel".equals(reportType)) {
                generateExcelReport(response, programmeCode, dateFrom, dateTo);
            } else if ("pdf".equals(reportType)) {
                generatePDFReport(response, programmeCode, dateFrom, dateTo);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println("Invalid report type. Use 'excel' or 'pdf'.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Error generating report: " + e.getMessage());
        }
    }
    
    private void generateExcelReport(HttpServletResponse response, String programmeCode, 
                                     String dateFrom, String dateTo) throws IOException {
        
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=attendance_report_" + 
                          new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".xlsx");
        
        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("Attendance Report");
        
        // Create header style - Use POI Font explicitly
        CellStyle headerStyle = workbook.createCellStyle();
        org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        
        // Create header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Student ID", "Student Name", "Course Code", "Total Classes", 
                           "Present", "Absent", "Late", "Attendance %"};
        
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        Connection conn = null;
        PreparedStatement ps = null;
        PreparedStatement insertPS = null;
        ResultSet rs = null;
        
        try {
            conn = DBConnection.getConnection();
            
            StringBuilder sql = new StringBuilder(
                "SELECT " +
                "s.studentID, " +
                "u.name, " +
                "c.courseCode, " +
                "COUNT(*) as total, " +
                "SUM(CASE WHEN ar.status='Present' THEN 1 ELSE 0 END) as present, " +
                "SUM(CASE WHEN ar.status='Absent' THEN 1 ELSE 0 END) as absent, " +
                "SUM(CASE WHEN ar.status='Late' THEN 1 ELSE 0 END) as late, " +
                "(SUM(CASE WHEN ar.status='Present' THEN 1 ELSE 0 END) * 100.0 / COUNT(*)) as attendance_rate " +
                "FROM Student s " +
                "JOIN User u ON s.studentID = u.userID " +
                "JOIN AttendanceRecord ar ON s.studentID = ar.studentID " +
                "JOIN ClassSection cs ON ar.sectionID = cs.sectionID " +
                "JOIN Course c ON cs.courseCode = c.courseCode "
            );
            
            // Add filters
            boolean hasWhere = false;
            if (dateFrom != null && !dateFrom.isEmpty()) {
                sql.append("WHERE DATE(ar.timeStamp) >= ? ");
                hasWhere = true;
            }
            if (dateTo != null && !dateTo.isEmpty()) {
                sql.append(hasWhere ? "AND " : "WHERE ");
                sql.append("DATE(ar.timeStamp) <= ? ");
                hasWhere = true;
            }
            if (programmeCode != null && !programmeCode.isEmpty() && !"all".equals(programmeCode)) {
                sql.append(hasWhere ? "AND " : "WHERE ");
                sql.append("s.programmeCode = ? ");
            }
            
            sql.append("GROUP BY s.studentID, u.name, c.courseCode " +
                      "ORDER BY s.studentID, c.courseCode");
            
            ps = conn.prepareStatement(sql.toString());
            
            // Set parameters
            int paramIndex = 1;
            if (dateFrom != null && !dateFrom.isEmpty()) {
                ps.setString(paramIndex++, dateFrom);
            }
            if (dateTo != null && !dateTo.isEmpty()) {
                ps.setString(paramIndex++, dateTo);
            }
            if (programmeCode != null && !programmeCode.isEmpty() && !"all".equals(programmeCode)) {
                ps.setString(paramIndex++, programmeCode);
            }
            
            rs = ps.executeQuery();
            
            // Create data rows
            int rowNum = 1;
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setAlignment(HorizontalAlignment.CENTER);
            dataStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            
            CellStyle percentStyle = workbook.createCellStyle();
            percentStyle.cloneStyleFrom(dataStyle);
            percentStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00\"%\""));
            
            CellStyle nameStyle = workbook.createCellStyle();
            nameStyle.cloneStyleFrom(dataStyle);
            nameStyle.setAlignment(HorizontalAlignment.LEFT);
            
            while (rs.next()) {
                Row row = sheet.createRow(rowNum++);
                
                Cell cell0 = row.createCell(0);
                cell0.setCellValue(rs.getString("studentID"));
                cell0.setCellStyle(dataStyle);
                
                Cell cell1 = row.createCell(1);
                cell1.setCellValue(rs.getString("name"));
                cell1.setCellStyle(nameStyle);
                
                Cell cell2 = row.createCell(2);
                cell2.setCellValue(rs.getString("courseCode"));
                cell2.setCellStyle(dataStyle);
                
                Cell cell3 = row.createCell(3);
                cell3.setCellValue(rs.getInt("total"));
                cell3.setCellStyle(dataStyle);
                
                Cell cell4 = row.createCell(4);
                cell4.setCellValue(rs.getInt("present"));
                cell4.setCellStyle(dataStyle);
                
                Cell cell5 = row.createCell(5);
                cell5.setCellValue(rs.getInt("absent"));
                cell5.setCellStyle(dataStyle);
                
                Cell cell6 = row.createCell(6);
                cell6.setCellValue(rs.getInt("late"));
                cell6.setCellStyle(dataStyle);
                
                Cell cell7 = row.createCell(7);
                cell7.setCellValue(rs.getDouble("attendance_rate") / 100.0); // Divide by 100 for percentage format
                cell7.setCellStyle(percentStyle);
            }
            
            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
                // Add some padding
                sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 512);
            }
            
            // Save report metadata to database
            String reportID = "REP" + System.currentTimeMillis();
            String insertReportSQL = "INSERT INTO Report (reportID, reportType, generationDate, " +
                                    "generatedBy, dataRange, fileFormat, filePath) VALUES (?, ?, ?, ?, ?, ?, ?)";
            insertPS = conn.prepareStatement(insertReportSQL);
            insertPS.setString(1, reportID);
            insertPS.setString(2, "Attendance");
            insertPS.setTimestamp(3, new java.sql.Timestamp(System.currentTimeMillis()));
            insertPS.setString(4, DB_USER_ID);
            insertPS.setString(5, (dateFrom != null ? dateFrom : "N/A") + " to " + (dateTo != null ? dateTo : "N/A"));
            insertPS.setString(6, "XLSX");
            insertPS.setString(7, "/reports/" + reportID + ".xlsx");
            insertPS.executeUpdate();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new IOException("JDBC Driver not found: " + e.getMessage());  
        } catch (SQLException e) {
            e.printStackTrace();
            throw new IOException("Database error: " + e.getMessage());
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (insertPS != null) insertPS.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        // Write to response
        try (ServletOutputStream out = response.getOutputStream()) {
            workbook.write(out);
        } finally {
            workbook.close();
        }
    }
    
    private void generatePDFReport(HttpServletResponse response, String programmeCode, 
                                   String dateFrom, String dateTo) throws IOException {
        
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=attendance_report_" + 
                          new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".pdf");
        
        Document document = null;
        Connection conn = null;
        PreparedStatement ps = null;
        PreparedStatement insertPS = null;
        ResultSet rs = null;
        
        try {
            document = new Document(PageSize.A4.rotate());
            PdfWriter.getInstance(document, response.getOutputStream());
            
            document.open();
            
            // Title - Use iText Font explicitly
            com.itextpdf.text.Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, BaseColor.DARK_GRAY);
            Paragraph title = new Paragraph("Attendance Report", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);
            document.add(title);
            
            // Report info
            com.itextpdf.text.Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY);
            String dateRange = (dateFrom != null && !dateFrom.isEmpty() ? dateFrom : "N/A") + 
                              " to " + 
                              (dateTo != null && !dateTo.isEmpty() ? dateTo : "N/A");
            String programme = (programmeCode != null && programmeCode.equals("all")) ? "All Programmes" : 
                              (programmeCode != null ? programmeCode : "N/A");
            
            Paragraph info = new Paragraph(
                "Generated: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()) + "\n" +
                "Date Range: " + dateRange + "\n" +
                "Programme: " + programme,
                infoFont
            );
            info.setSpacingAfter(20);
            document.add(info);
            
            // Create table
            PdfPTable table = new PdfPTable(8);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);
            
            // Set column widths
            float[] columnWidths = {1.2f, 2f, 1.5f, 1f, 1f, 1f, 1f, 1.5f};
            table.setWidths(columnWidths);
            
            // Header cells
            com.itextpdf.text.Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
            String[] headers = {"Student ID", "Student Name", "Course Code", "Total Classes", 
                               "Present", "Absent", "Late", "Attendance %"};
            
            for (String header : headers) {
                PdfPCell cell = new PdfPCell(new Phrase(header, headerFont));
                cell.setBackgroundColor(new BaseColor(102, 126, 234));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPadding(8);
                table.addCell(cell);
            }
            
            // Fetch data from database
            conn = DBConnection.getConnection();
            
            StringBuilder sql = new StringBuilder(
                "SELECT " +
                "s.studentID, " +
                "u.name, " +
                "c.courseCode, " +
                "COUNT(*) as total, " +
                "SUM(CASE WHEN ar.status='Present' THEN 1 ELSE 0 END) as present, " +
                "SUM(CASE WHEN ar.status='Absent' THEN 1 ELSE 0 END) as absent, " +
                "SUM(CASE WHEN ar.status='Late' THEN 1 ELSE 0 END) as late, " +
                "(SUM(CASE WHEN ar.status='Present' THEN 1 ELSE 0 END) * 100.0 / COUNT(*)) as attendance_rate " +
                "FROM Student s " +
                "JOIN User u ON s.studentID = u.userID " +
                "JOIN AttendanceRecord ar ON s.studentID = ar.studentID " +
                "JOIN ClassSection cs ON ar.sectionID = cs.sectionID " +
                "JOIN Course c ON cs.courseCode = c.courseCode "
            );
            
            boolean hasWhere = false;
            if (dateFrom != null && !dateFrom.isEmpty()) {
                sql.append("WHERE DATE(ar.timeStamp) >= ? ");
                hasWhere = true;
            }
            if (dateTo != null && !dateTo.isEmpty()) {
                sql.append(hasWhere ? "AND " : "WHERE ");
                sql.append("DATE(ar.timeStamp) <= ? ");
                hasWhere = true;
            }
            if (programmeCode != null && !programmeCode.isEmpty() && !"all".equals(programmeCode)) {
                sql.append(hasWhere ? "AND " : "WHERE ");
                sql.append("s.programmeCode = ? ");
            }
            
            sql.append("GROUP BY s.studentID, u.name, c.courseCode " +
                      "ORDER BY s.studentID, c.courseCode");
            
            ps = conn.prepareStatement(sql.toString());
            
            int paramIndex = 1;
            if (dateFrom != null && !dateFrom.isEmpty()) {
                ps.setString(paramIndex++, dateFrom);
            }
            if (dateTo != null && !dateTo.isEmpty()) {
                ps.setString(paramIndex++, dateTo);
            }
            if (programmeCode != null && !programmeCode.isEmpty() && !"all".equals(programmeCode)) {
                ps.setString(paramIndex++, programmeCode);
            }
            
            rs = ps.executeQuery();
            
            // Data cells
            com.itextpdf.text.Font dataFont = FontFactory.getFont(FontFactory.HELVETICA, 9);
            int rowCount = 0;
            
            while (rs.next()) {
                rowCount++;
                BaseColor rowColor = (rowCount % 2 == 0) ? BaseColor.WHITE : new BaseColor(245, 245, 245);
                
                // Student ID
                PdfPCell cell = new PdfPCell(new Phrase(rs.getString("studentID"), dataFont));
                cell.setBackgroundColor(rowColor);
                cell.setPadding(5);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell);
                
                // Name
                cell = new PdfPCell(new Phrase(rs.getString("name"), dataFont));
                cell.setBackgroundColor(rowColor);
                cell.setPadding(5);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell);
                
                // Course
                cell = new PdfPCell(new Phrase(rs.getString("courseCode"), dataFont));
                cell.setBackgroundColor(rowColor);
                cell.setPadding(5);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell);
                
                // Total
                cell = new PdfPCell(new Phrase(String.valueOf(rs.getInt("total")), dataFont));
                cell.setBackgroundColor(rowColor);
                cell.setPadding(5);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell);
                
                // Present
                cell = new PdfPCell(new Phrase(String.valueOf(rs.getInt("present")), dataFont));
                cell.setBackgroundColor(rowColor);
                cell.setPadding(5);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell);
                
                // Absent
                cell = new PdfPCell(new Phrase(String.valueOf(rs.getInt("absent")), dataFont));
                cell.setBackgroundColor(rowColor);
                cell.setPadding(5);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell);
                
                // Late
                cell = new PdfPCell(new Phrase(String.valueOf(rs.getInt("late")), dataFont));
                cell.setBackgroundColor(rowColor);
                cell.setPadding(5);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell);
                
                // Attendance %
                double rate = rs.getDouble("attendance_rate");
                com.itextpdf.text.Font percentFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
                percentFont.setColor(rate < 75 ? BaseColor.RED : new BaseColor(76, 175, 80));
                cell = new PdfPCell(new Phrase(String.format("%.1f%%", rate), percentFont));
                cell.setBackgroundColor(rowColor);
                cell.setPadding(5);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell);
            }
            
            document.add(table);
            
            // Footer
            Paragraph footer = new Paragraph(
                "\nTotal Records: " + rowCount + " | Generated by EduTrack System",
                infoFont
            );
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);
            
            // Save report metadata
            String reportID = "REP" + System.currentTimeMillis();
            String insertReportSQL = "INSERT INTO Report (reportID, reportType, generationDate, " +
                                    "generatedBy, dataRange, fileFormat, filePath) VALUES (?, ?, ?, ?, ?, ?, ?)";
            insertPS = conn.prepareStatement(insertReportSQL);
            insertPS.setString(1, reportID);
            insertPS.setString(2, "Attendance");
            insertPS.setTimestamp(3, new java.sql.Timestamp(System.currentTimeMillis()));
            insertPS.setString(4, DB_USER_ID);
            insertPS.setString(5, dateRange);
            insertPS.setString(6, "PDF");
            insertPS.setString(7, "/reports/" + reportID + ".pdf");
            insertPS.executeUpdate();
            
        } catch (DocumentException e) {
            e.printStackTrace();
            throw new IOException("Error creating PDF document: " + e.getMessage());
       } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new IOException("JDBC Driver not found: " + e.getMessage());
       } catch (SQLException e) {
            e.printStackTrace();
            throw new IOException("Database error: " + e.getMessage());
        } finally {
            // Close document
            if (document != null && document.isOpen()) {
                document.close();
            }
            
            // Close database resources
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (insertPS != null) insertPS.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}