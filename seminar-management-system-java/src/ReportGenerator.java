import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * System Requirement: Reports & Summary with export options and data analytics
 * Generates comprehensive reports for seminars, evaluations, schedules, and awards
 */
public class ReportGenerator {
    
    // --- Helper to get current time ---
    private static String getCurrentDateTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }

    // =================================================================
    // 1. SEMINAR SCHEDULE REPORT
    // =================================================================
    public static String generateSeminarSchedule() {
        StringBuilder sb = new StringBuilder();
        sb.append("══════════════════════════════════════════════════════════════════════════════════\n");
        sb.append("                          SEMINAR PRESENTATION SCHEDULE                           \n");
        sb.append("                          Generated: ").append(getCurrentDateTime()).append("\n");
        sb.append("══════════════════════════════════════════════════════════════════════════════════\n\n");
        
        if (Data.sessionList.isEmpty()) {
            sb.append("   [!] No sessions scheduled.\n");
            return sb.toString();
        }
        
        List<Session> sortedSessions = new ArrayList<>(Data.sessionList);
        sortedSessions.sort(Comparator.comparing(Session::getDate));
        
        for (Session session : sortedSessions) {
            sb.append("┌────────────────────────────────────────────────────────────────────────────────┐\n");
            sb.append(String.format("│ Session ID: %-66s │\n", session.getSessionId()));
            sb.append(String.format("│ Date:       %-66s │\n", session.getDate()));
            sb.append(String.format("│ Time:       %-66s │\n", session.getTime()));
            sb.append(String.format("│ Venue:      %-66s │\n", session.getVenue()));
            sb.append(String.format("│ Type:       %-66s │\n", session.getSessionType()));
            sb.append(String.format("│ Evaluator:  %-66s │\n", session.getEvaluatorName()));
            sb.append(String.format("│ Capacity:   %d / %-62d │\n", 
                Data.getStudentCountForSession(session.getSessionId()), session.getCapacity()));
            
            sb.append("│                                                                                │\n");
            sb.append("│ Presenters:                                                                    │\n");
            
            List<Student> presenters = Data.studentList.stream()
                .filter(s -> session.getSessionId().equals(s.getSessionId()))
                .collect(Collectors.toList());
            
            if (presenters.isEmpty()) {
                sb.append("│   - No presenters assigned yet                                                 │\n");
            } else {
                for (Student s : presenters) {
                    // Truncate title if too long to fit in box
                    String title = s.getResearchTitle();
                    if(title != null && title.length() > 35) title = title.substring(0, 32) + "...";
                    
                    sb.append(String.format("│   - %-15s [%-6s] : %-39s │\n", 
                        s.getName(), s.getId(), title));
                }
            }
            sb.append("└────────────────────────────────────────────────────────────────────────────────┘\n\n");
        }
        return sb.toString();
    }
    
    // =================================================================
    // 2. EVALUATION REPORT (PREVIEW & EXPORT)
    // =================================================================
    
    // --- A. Generate PREVIEW (Neat Text Table for Window) ---
    public static String generateEvaluationReport() {
        StringBuilder sb = new StringBuilder();
        String timestamp = getCurrentDateTime();

        // 1. Header
        sb.append("==========================================================================================================================================================\n");
        sb.append(String.format("%90s\n", "FINAL EVALUATION REPORT"));
        sb.append(String.format("%85s %s\n", "Generated:", timestamp));
        sb.append("==========================================================================================================================================================\n\n");

        // 2. Table Header
        // Redefined column widths to match truncation logic
        // ID(10) | Name(15) | Title(30) | Supervisor(15) | Session(12) | Type(8) | Score(6) | Status(10) | Remark(20)
        String format = "%-10s | %-15s | %-30s | %-15s | %-12s | %-8s | %-6s | %-10s | %-20s\n";
        
        sb.append(String.format(format, 
            "ID", "Name", "Research Title", "Supervisor", "Session", "Type", "Score", "Status", "Remarks"));
        sb.append("----------------------------------------------------------------------------------------------------------------------------------------------------------\n");

        // 3. Table Data
        for (Student s : Data.studentList) {
            String score = (s.getScore() == null || s.getScore().equals("0")) ? "-" : s.getScore();
            String session = (s.getSessionId() == null) ? "Unassigned" : s.getSessionId();
            String remark = (s.getRemark() == null) ? "-" : s.getRemark();
            
            // --- Core Fix: Truncate very long text ---
            // Only truncation ensures the table is always aligned
            
            // Title (Limit 28 chars)
            String title = s.getResearchTitle();
            if (title != null && title.length() > 28) title = title.substring(0, 25) + "...";
            
            // Name (Limit 13 chars)
            String name = s.getName();
            if (name != null && name.length() > 13) name = name.substring(0, 11) + "..";
            
            // Supervisor (Limit 13 chars)
            String sup = s.getSupervisorName();
            if (sup != null && sup.length() > 13) sup = sup.substring(0, 11) + "..";

            // Remark (Limit 18 chars)
            String rem = remark;
            if (rem != null && rem.length() > 18) rem = rem.substring(0, 16) + "..";

            sb.append(String.format(format,
                s.getId(),
                name,
                title,
                sup,
                session,
                s.getPresentationType(),
                score,
                s.getSubmissionStatus(),
                rem
            ));
        }
        sb.append("----------------------------------------------------------------------------------------------------------------------------------------------------------\n");
        sb.append("\n[NOTE] Click 'Yes' to export this report to Excel (.csv) format.\n");
        
        return sb.toString();
    }

    // --- B. Generate CSV Data (For Excel Export) ---
    private static String generateEvaluationCSV() {
        StringBuilder sb = new StringBuilder();
        
        // Excel BOM (Byte Order Mark) to prevent garbled text
        sb.append('\ufeff'); 
        
        // 1. CSV Headers (The 9 columns you requested)
        sb.append("Student ID,Student Name,Research Title,Supervisor,Session,Type,Final Score,Status,Evaluator Remarks\n");

        // 2. Data Rows
        for (Student s : Data.studentList) {
            String score = (s.getScore() == null || s.getScore().equals("0")) ? "-" : s.getScore();
            String session = (s.getSessionId() == null) ? "Unassigned" : s.getSessionId();
            String remark = (s.getRemark() == null) ? "-" : s.getRemark();
            
            // No need to truncate for Excel export, but remove commas
            String cleanTitle = (s.getResearchTitle() == null) ? "" : s.getResearchTitle().replace(",", " ");
            String cleanName = (s.getName() == null) ? "" : s.getName().replace(",", " ");
            String cleanSup = (s.getSupervisorName() == null) ? "" : s.getSupervisorName().replace(",", " ");
            String cleanRemark = remark.replace(",", " ");
            
            sb.append(s.getId()).append(",");
            sb.append(cleanName).append(",");
            sb.append(cleanTitle).append(",");
            sb.append(cleanSup).append(",");
            sb.append(session).append(",");
            sb.append(s.getPresentationType()).append(",");
            sb.append(score).append(",");
            sb.append(s.getSubmissionStatus()).append(",");
            sb.append(cleanRemark).append("\n");
        }
        return sb.toString();
    }
    
    // =================================================================
    // 3. AWARD REPORT
    // =================================================================
    public static String generateAwardReport() {
        StringBuilder sb = new StringBuilder();
        String timestamp = getCurrentDateTime();

        sb.append("═══════════════════════════════════════════════════════════\n");
        sb.append("            AWARD WINNERS & CEREMONY AGENDA                \n");
        sb.append("            Generated: " + timestamp + "\n");
        sb.append("═══════════════════════════════════════════════════════════\n\n");

        // Best Oral
        Student bestOral = Data.studentList.stream()
            .filter(s -> "Oral".equals(s.getPresentationType()))
            .sorted((s1, s2) -> {
                double score1 = 0, score2 = 0;
                try { score1 = Double.parseDouble(String.valueOf(s1.getScore())); } catch(Exception e){}
                try { score2 = Double.parseDouble(String.valueOf(s2.getScore())); } catch(Exception e){}
                return Double.compare(score2, score1);
            })
            .findFirst().orElse(null);

        sb.append("🏆 BEST ORAL PRESENTATION:\n");
        if (bestOral != null && !bestOral.getScore().equals("-") && !bestOral.getScore().equals("0")) {
            sb.append("   WINNER: " + bestOral.getName() + "\n");
            sb.append("   ID:     " + bestOral.getId() + "\n");
            sb.append("   Title:  " + bestOral.getResearchTitle() + "\n");
            sb.append("   Score:  " + bestOral.getScore() + "\n");
        } else {
            sb.append("   [No eligible candidates yet]\n");
        }
        sb.append("\n");

        // Best Poster
        Student bestPoster = Data.studentList.stream()
            .filter(s -> "Poster".equals(s.getPresentationType()))
            .sorted((s1, s2) -> {
                double score1 = 0, score2 = 0;
                try { score1 = Double.parseDouble(String.valueOf(s1.getScore())); } catch(Exception e){}
                try { score2 = Double.parseDouble(String.valueOf(s2.getScore())); } catch(Exception e){}
                return Double.compare(score2, score1);
            })
            .findFirst().orElse(null);

        sb.append("🏆 BEST POSTER PRESENTATION:\n");
        if (bestPoster != null && !bestPoster.getScore().equals("-") && !bestPoster.getScore().equals("0")) {
            sb.append("   WINNER: " + bestPoster.getName() + "\n");
            sb.append("   ID:     " + bestPoster.getId() + "\n");
            sb.append("   Title:  " + bestPoster.getResearchTitle() + "\n");
            sb.append("   Score:  " + bestPoster.getScore() + "\n");
        } else {
            sb.append("   [No eligible candidates yet]\n");
        }
        sb.append("\n");

        sb.append("─────────────────────────────────────────────────────────\n");
        sb.append("CEREMONY AGENDA:\n");
        sb.append("─────────────────────────────────────────────────────────\n");
        sb.append("1. Opening Remarks by Coordinator\n");
        sb.append("2. Presentation Session Highlights\n");
        sb.append("3. Award Announcements\n");
        sb.append("4. Closing Remarks & Networking\n");

        return sb.toString();
    }
    
    // =================================================================
    // 4. ANALYTICS REPORT
    // =================================================================
    public static String generateAnalyticsReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════════════════════\n");
        sb.append("                DATA ANALYTICS & STATISTICS                \n");
        sb.append("           Generated: ").append(getCurrentDateTime()).append("\n");
        sb.append("═══════════════════════════════════════════════════════════\n\n");
        
        sb.append("📊 STUDENT STATISTICS:\n");
        sb.append(String.format("   Total Registered: %d\n", Data.studentList.size()));
        long submittedCount = Data.studentList.stream().filter(s -> s.getSubmissionStatus().equals("Submitted")).count();
        sb.append(String.format("   Submitted Files:  %d\n", submittedCount));
        
        long oralCount = Data.studentList.stream().filter(s -> s.getPresentationType().equals("Oral")).count();
        long posterCount = Data.studentList.stream().filter(s -> s.getPresentationType().equals("Poster")).count();
        sb.append("\n📊 PRESENTATION TYPE DISTRIBUTION:\n");
        sb.append(String.format("   Oral:   %d\n", oralCount));
        sb.append(String.format("   Poster: %d\n", posterCount));
        
        return sb.toString();
    }
    
    // =================================================================
    // 5. SMART EXPORT LOGIC
    // =================================================================
    public static void exportToFile(String previewContent, String reportType) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save " + reportType);
        
        String defaultFileName;
        String contentToSave;
        
        // If user selected "Evaluation Report", force CSV format
        if (reportType.contains("Evaluation")) {
            defaultFileName = "Final_Evaluation_Report.csv";
            contentToSave = generateEvaluationCSV(); // Use CSV generator
        } else {
            // Other reports use TXT
            defaultFileName = reportType.replace(" ", "_") + ".txt";
            contentToSave = previewContent; // Use preview content
        }

        fileChooser.setSelectedFile(new File(defaultFileName));

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try (FileWriter writer = new FileWriter(fileToSave)) {
                writer.write(contentToSave);
                JOptionPane.showMessageDialog(null, "Report exported successfully to:\n" + fileToSave.getAbsolutePath());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error saving file: " + ex.getMessage());
            }
        }
    }
    
    public static void computeAwardWinners() { }
}