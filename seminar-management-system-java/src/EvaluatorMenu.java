import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EvaluatorMenu extends JFrame {
    Evaluator currentEvaluator;
    JPanel mainPanel;
    CardLayout cardLayout;
    
    // View 1: Session List
    JTable sessionTable;
    DefaultTableModel sessionModel;
    
    // View 2: Student List
    JTable studentTable;
    DefaultTableModel studentModel;
    JLabel lblCurrentSession;
    
    // Track currently selected Session
    Session currentSelectedSession = null; 

    public EvaluatorMenu(Evaluator evaluator) {
        this.currentEvaluator = evaluator;
        
        // --- 1. Set Window Properties ---
        setTitle("Evaluator Dashboard - " + evaluator.getName());
        setSize(1200, 750); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); 

        // =================================================================================
        // --- 2. Top Panel ---
        // =================================================================================
        JPanel topPanel = new JPanel(new BorderLayout()); 
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10)); 

        JLabel lblWelcome = new JLabel("Welcome, Evaluator " + evaluator.getName());
        lblWelcome.setFont(new Font("SansSerif", Font.BOLD, 16));
        topPanel.add(lblWelcome, BorderLayout.WEST);

        JButton btnTopLogout = new JButton("Logout ➜");
        btnTopLogout.setFocusable(false);
        btnTopLogout.setBackground(new Color(255, 200, 200)); 
        btnTopLogout.addActionListener(e -> {
            dispose(); 
            new LoginFrame().setVisible(true); 
        });
        topPanel.add(btnTopLogout, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // =================================================================================
        // --- 3. Main Center Area (CardLayout) ---
        // =================================================================================
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createSessionView(), "view_sessions");
        mainPanel.add(createStudentView(), "view_students");
        
        add(mainPanel, BorderLayout.CENTER);
        
        cardLayout.show(mainPanel, "view_sessions");
    }

    // --- View 1: Session View ---
    private JPanel createSessionView() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("👉 Please select a session to evaluate:")); 
        panel.add(top, BorderLayout.NORTH);

        String[] cols = { "Session ID", "Presentation Date & Time", "Due Date & Time", "Venue", "Students" };
        sessionModel = new DefaultTableModel(cols, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        sessionTable = new JTable(sessionModel);
        sessionTable.setRowHeight(30);
        
        sessionTable.getColumnModel().getColumn(0).setPreferredWidth(80);  
        sessionTable.getColumnModel().getColumn(1).setPreferredWidth(200); 
        sessionTable.getColumnModel().getColumn(2).setPreferredWidth(180); 
        sessionTable.getColumnModel().getColumn(3).setPreferredWidth(100); 
        sessionTable.getColumnModel().getColumn(4).setPreferredWidth(80);  

        sessionTable.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) { if (e.getClickCount() == 2) enterSession(); }
        });

        loadSessions(); 
        panel.add(new JScrollPane(sessionTable), BorderLayout.CENTER);

        JPanel bot = new JPanel();
        JButton btnEnter = new JButton("Enter Session ➜");
        bot.add(btnEnter);
        panel.add(bot, BorderLayout.SOUTH);
        btnEnter.addActionListener(e -> enterSession());
        return panel;
    }

    private void enterSession() {
        int row = sessionTable.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Please select a session first!"); return; }
        String sid = (String) sessionModel.getValueAt(row, 0);
        currentSelectedSession = findSessionById(sid); 
        loadStudentsForSession(sid);
        updateSessionLabel(); 
        cardLayout.show(mainPanel, "view_students");
    }
    
    private void updateSessionLabel() {
        if(currentSelectedSession != null) {
            String rawDue = currentSelectedSession.getSubmissionDueDate();
            String displayDue;
            if (rawDue == null || rawDue.equals("-")) { displayDue = "Not Set"; } 
            else if (rawDue.trim().length() <= 10) { displayDue = rawDue + " 11:59 PM"; } 
            else { displayDue = rawDue; }
            lblCurrentSession.setText("Session: " + currentSelectedSession.getSessionId() + " | 📅 Due Date: " + displayDue);
        }
    }

    // --- View 2: Student View ---
    private JPanel createStudentView() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JPanel top = new JPanel(new BorderLayout());
        
        lblCurrentSession = new JLabel("Current Session: -");
        lblCurrentSession.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblCurrentSession.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        JPanel rightBtnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSetDueDate = new JButton("📅 Set Due Date");
        btnSetDueDate.setBackground(new Color(255, 255, 204));
        JButton btnBack = new JButton("⬅️ Back");
        rightBtnPanel.add(btnSetDueDate); rightBtnPanel.add(btnBack);
        
        top.add(lblCurrentSession, BorderLayout.WEST); top.add(rightBtnPanel, BorderLayout.EAST);
        panel.add(top, BorderLayout.NORTH);

        String[] cols = {"ID", "Name", "Research Title", "Status", "Score", "Last Upload", "Remark"};
        studentModel = new DefaultTableModel(cols, 0) { @Override public boolean isCellEditable(int r, int c) { return false; } };
        studentTable = new JTable(studentModel); studentTable.setRowHeight(30);
        studentTable.setDefaultRenderer(Object.class, new StudentStatusRenderer());

        studentTable.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { 
                    int row = studentTable.getSelectedRow();
                    if (row != -1) {
                        String id = (String) studentModel.getValueAt(row, 0);
                        Student s = findStudentById(id);
                        if (s != null) openRubricGradingDialog(s);
                    }
                }
            }
        });

        panel.add(new JScrollPane(studentTable), BorderLayout.CENTER);

        JPanel bot = new JPanel();
        JButton btnGrade = new JButton("📝 Grade with Rubric"); 
        JButton btnFiles = new JButton("📂 View All Files");
        btnGrade.setBackground(new Color(204, 255, 204)); 
        bot.add(btnGrade); bot.add(btnFiles);
        panel.add(bot, BorderLayout.SOUTH);

        btnBack.addActionListener(e -> { loadSessions(); cardLayout.show(mainPanel, "view_sessions"); });

        btnSetDueDate.addActionListener(e -> {
            if (currentSelectedSession == null) return;
            String currentDue = currentSelectedSession.getSubmissionDueDate();
            if (currentDue == null || currentDue.equals("-")) currentDue = new SimpleDateFormat("yyyy-MM-dd HH:mm a").format(new Date());
            DateTimePicker picker = new DateTimePicker(this, currentDue);
            picker.setVisible(true);
            if (picker.isConfirmed()) {
                String newDue = picker.getSelectedDateTime(); 
                currentSelectedSession.setSubmissionDueDate(newDue);
                updateSessionLabel(); studentTable.repaint(); 
                JOptionPane.showMessageDialog(this, "Due Date Updated Successfully!");
            }
        });

        btnGrade.addActionListener(e -> {
            int row = studentTable.getSelectedRow();
            if(row == -1) { JOptionPane.showMessageDialog(this, "Please select a student first!"); return; }
            String id = (String) studentModel.getValueAt(row, 0);
            Student s = findStudentById(id);
            if (s != null) openRubricGradingDialog(s);
        });

        btnFiles.addActionListener(e -> {
            int row = studentTable.getSelectedRow();
            if(row == -1) return;
            Student s = findStudentById((String) studentModel.getValueAt(row, 0));
            if (s != null) openFilesDialog(s);
        });

        return panel;
    }

    // --- File Opener ---
    private void openFileSafe(File f, Component parent) {
        if (!f.exists()) { JOptionPane.showMessageDialog(parent, "File not found locally!\nPath: " + f.getAbsolutePath()); return; }
        try { java.awt.Desktop.getDesktop().open(f); } 
        catch (Exception ex) {
            try { Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler \"" + f.getAbsolutePath() + "\""); } 
            catch (Exception ex2) { JOptionPane.showMessageDialog(parent, "Could not open file."); }
        }
    }

    // =========================================================================
    // Rubric Grading Dialog
    // =========================================================================
    private void openRubricGradingDialog(Student student) {
        JDialog dialog = new JDialog(this, "Grading: " + student.getName(), true);
        dialog.setSize(750, 750); dialog.setLocationRelativeTo(this); dialog.setLayout(new BorderLayout(10, 10));

        JPanel mainContainer = new JPanel(); mainContainer.setLayout(new BoxLayout(mainContainer, BoxLayout.Y_AXIS));
        mainContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Info
        JPanel infoPanel = new JPanel(new GridLayout(4, 1, 3, 3)); 
        infoPanel.setBorder(BorderFactory.createTitledBorder("Student Information"));
        infoPanel.setMaximumSize(new Dimension(2000, 160)); infoPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblName = new JLabel("  🎓 Student: " + student.getName() + " (" + student.getId() + ")"); lblName.setFont(new Font("SansSerif", Font.BOLD, 14));
        JLabel lblTitle = new JLabel("  📄 Title: " + student.getResearchTitle()); lblTitle.setFont(new Font("SansSerif", Font.PLAIN, 13));
        JLabel lblType = new JLabel("  🎤 Presentation Type: " + student.getPresentationType()); lblType.setFont(new Font("SansSerif", Font.ITALIC, 13)); lblType.setForeground(new Color(102, 51, 0)); 

        JPanel filePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JButton btnViewLatest = new JButton("📂 Open Latest Document");
        btnViewLatest.setBackground(new Color(230, 240, 255)); 
        List<Submission> history = student.getSortedSubmissions();
        if (history.isEmpty()) { btnViewLatest.setText("❌ No files uploaded"); btnViewLatest.setEnabled(false); } 
        else {
            Submission latest = history.get(0);
            btnViewLatest.setText("📂 Open: " + latest.getSavedFileName());
            btnViewLatest.addActionListener(e -> { File f = new File("storage/" + latest.getSavedFileName()); openFileSafe(f, dialog); });
        }
        filePanel.add(btnViewLatest);
        infoPanel.add(lblName); infoPanel.add(lblTitle); infoPanel.add(lblType); infoPanel.add(filePanel);
        
        // Abstract
        JPanel absPanel = new JPanel(new BorderLayout()); absPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 10, 0));
        absPanel.setMaximumSize(new Dimension(2000, 110)); absPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel lblAbsHeader = new JLabel("Project Abstract:"); lblAbsHeader.setFont(new Font("SansSerif", Font.BOLD, 12));
        JTextArea txtAbs = new JTextArea(student.getProjectAbstract()); txtAbs.setEditable(false); txtAbs.setLineWrap(true); txtAbs.setWrapStyleWord(true);
        txtAbs.setBackground(new Color(245, 245, 245));
        JScrollPane scrollAbs = new JScrollPane(txtAbs); scrollAbs.setPreferredSize(new Dimension(600, 80));
        absPanel.add(lblAbsHeader, BorderLayout.NORTH); absPanel.add(scrollAbs, BorderLayout.CENTER);

        // Rubric
        JLabel lblRubricHeader = new JLabel("Marking Rubric:"); lblRubricHeader.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblRubricHeader.setForeground(new Color(0, 51, 102)); lblRubricHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblRubricHeader.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));

        int[] savedScores = student.getRubricScores(); 
        String[] columns = {"Criteria", "Max Mark", "Marks Obtained"};
        Object[][] data = new Object[Evaluator.RUBRIC_CRITERIA.length][3];
        for (int i = 0; i < Evaluator.RUBRIC_CRITERIA.length; i++) {
            data[i][0] = Evaluator.RUBRIC_CRITERIA[i]; data[i][1] = Evaluator.MAX_RUBRIC_SCORE;   
            data[i][2] = (i < savedScores.length) ? savedScores[i] : 0; 
        }

        DefaultTableModel rubricModel = new DefaultTableModel(data, columns) {
            @Override public boolean isCellEditable(int row, int column) { return column == 2; }
            @Override public Class<?> getColumnClass(int columnIndex) { return (columnIndex >= 1) ? Integer.class : String.class; }
        };
        JTable rubricTable = new JTable(rubricModel); rubricTable.setRowHeight(35);
        
        // Single click to edit
        DefaultCellEditor singleClickEditor = new DefaultCellEditor(new JTextField());
        singleClickEditor.setClickCountToStart(1);
        rubricTable.getColumnModel().getColumn(2).setCellEditor(singleClickEditor);

        JScrollPane scrollTable = new JScrollPane(rubricTable);
        scrollTable.setPreferredSize(new Dimension(600, 165)); scrollTable.setMaximumSize(new Dimension(2000, 165));
        scrollTable.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Bottom
        JPanel botPanel = new JPanel(); botPanel.setLayout(new BoxLayout(botPanel, BoxLayout.Y_AXIS));
        botPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); botPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        int initTotal = 0; for (int s : savedScores) initTotal += s;
        JLabel lblTotal = new JLabel("Total Score: " + initTotal + " / 100");
        lblTotal.setFont(new Font("SansSerif", Font.BOLD, 18)); lblTotal.setForeground(new Color(0, 102, 0));
        lblTotal.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JTextArea txtRemark = new JTextArea(3, 30); txtRemark.setText(student.getRemark()); 
        txtRemark.setBorder(BorderFactory.createTitledBorder("Teacher's Remark"));
        
        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton btnSave = new JButton("💾 Save Grade"); JButton btnCancel = new JButton("Cancel");
        btnPanel.add(btnSave); btnPanel.add(btnCancel);

        botPanel.add(lblTotal); botPanel.add(Box.createVerticalStrut(10));
        botPanel.add(new JScrollPane(txtRemark)); botPanel.add(Box.createVerticalStrut(10));
        botPanel.add(btnPanel);

        mainContainer.add(infoPanel); mainContainer.add(absPanel); mainContainer.add(lblRubricHeader); 
        mainContainer.add(scrollTable); mainContainer.add(botPanel);

        rubricModel.addTableModelListener(e -> {
            int total = 0;
            for (int i = 0; i < rubricModel.getRowCount(); i++) {
                try { total += Integer.parseInt(rubricModel.getValueAt(i, 2).toString()); } catch (Exception ex) {}
            }
            lblTotal.setText("Total Score: " + total + " / 100");
            if(total > 100) lblTotal.setForeground(Color.RED); else lblTotal.setForeground(new Color(0, 102, 0));
        });

        btnSave.addActionListener(e -> {
            int finalTotal = 0; int[] newScores = new int[Evaluator.RUBRIC_CRITERIA.length]; boolean hasError = false;
            for (int i = 0; i < rubricModel.getRowCount(); i++) {
                try {
                    int score = Integer.parseInt(rubricModel.getValueAt(i, 2).toString());
                    int max = Integer.parseInt(rubricModel.getValueAt(i, 1).toString());
                    if (score < 0 || score > max) { JOptionPane.showMessageDialog(dialog, "Score error in row " + (i+1)); hasError = true; break; }
                    newScores[i] = score; finalTotal += score;  
                } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(dialog, "Please enter valid numbers."); hasError = true; break; }
            }
            if (hasError) return;
            currentEvaluator.evaluateStudent(student, newScores, txtRemark.getText());
            loadStudentsForSession(student.getSessionId()); 
            JOptionPane.showMessageDialog(dialog, "Saved!"); dialog.dispose();
        });
        btnCancel.addActionListener(e -> dialog.dispose());
        dialog.add(mainContainer, BorderLayout.CENTER); dialog.setVisible(true);
    }
    
    // =========================================================================
    // ✨【Modify】View Files Dialog -> Changed to Table format
    // =========================================================================
    private void openFilesDialog(Student s) {
        JDialog d = new JDialog(this, "Files: " + s.getName(), true);
        d.setSize(600, 350); // Make it slightly wider for the table
        d.setLocationRelativeTo(this);
        d.setLayout(new BorderLayout(10, 10));

        // 1. Set Table Columns
        String[] cols = {"File Name", "Upload Time"};
        DefaultTableModel fileModel = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        
        // 2. Fill Data
        List<Submission> subList = s.getSortedSubmissions();
        for (Submission sub : subList) {
            fileModel.addRow(new Object[]{ sub.getSavedFileName(), sub.getFormattedTime() });
        }
        
        // 3. Create JTable
        JTable fileTable = new JTable(fileModel);
        fileTable.setRowHeight(25);
        fileTable.getColumnModel().getColumn(0).setPreferredWidth(350); // Filename wider
        fileTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Time narrower
        
        // 4. Double click to open
        fileTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2) {
                    int row = fileTable.getSelectedRow();
                    if(row != -1) {
                        File f = new File("storage/" + subList.get(row).getSavedFileName());
                        openFileSafe(f, d);
                    }
                }
            }
        });

        d.add(new JScrollPane(fileTable), BorderLayout.CENTER);

        // 5. Bottom Button
        JButton open = new JButton("Open Selected File");
        open.addActionListener(ev -> {
            int row = fileTable.getSelectedRow();
            if (row != -1) {
                File f = new File("storage/" + subList.get(row).getSavedFileName());
                openFileSafe(f, d);
            } else {
                JOptionPane.showMessageDialog(d, "Please select a file first.");
            }
        });
        
        JPanel btnPanel = new JPanel();
        btnPanel.add(open);
        d.add(btnPanel, BorderLayout.SOUTH);
        
        d.setVisible(true);
    }

    // --- Helpers ---
    class StudentStatusRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            String status = (String) table.getModel().getValueAt(row, 3);
            String uploadTime = (String) table.getModel().getValueAt(row, 5); 
            if (!isSelected) { 
                if ("Pending".equals(status)) { c.setForeground(Color.GRAY); c.setBackground(Color.WHITE); } 
                else if ("Evaluated".equals(status)) { c.setForeground(Color.BLACK); c.setBackground(new Color(220, 255, 220)); } 
                else if ("Submitted".equals(status)) {
                    c.setForeground(Color.BLACK);
                    boolean isLate = false;
                    if (currentSelectedSession != null && !uploadTime.equals("Never")) {
                        String fullDueDate = currentSelectedSession.getSubmissionDueDate(); 
                        try {
                            if (fullDueDate != null && !fullDueDate.equals("-")) {
                                String dueDateOnly = fullDueDate.length() >= 10 ? fullDueDate.substring(0, 10) : fullDueDate;
                                String uploadDateOnly = uploadTime.length() >= 10 ? uploadTime.substring(0, 10) : uploadTime;
                                if (uploadDateOnly.compareTo(dueDateOnly) > 0) isLate = true;
                            }
                        } catch (Exception ex) {}
                    }
                    if (isLate) c.setBackground(new Color(255, 220, 220)); else c.setBackground(Color.WHITE); 
                }
            } else {
                c.setForeground(table.getSelectionForeground()); c.setBackground(table.getSelectionBackground());
            }
            return c;
        }
    }

    // Load Sessions
    private void loadSessions() {
        sessionModel.setRowCount(0);
        for (Session s : Data.sessionList) {
            if (s.getEvaluatorName().contains(currentEvaluator.getId())) {
                int count = Data.getStudentCountForSession(s.getSessionId());
                String presentationDT = s.getDate() + " " + s.getTime();
                String rawDue = s.getSubmissionDueDate();
                String dueDate;
                if (rawDue == null || rawDue.equals("-")) { dueDate = "Not Set"; } 
                else if (rawDue.trim().length() <= 10) { dueDate = rawDue + " 11:59 PM"; } 
                else { dueDate = rawDue; }
                sessionModel.addRow(new Object[]{ s.getSessionId(), presentationDT, dueDate, s.getVenue(), count + " / " + s.getCapacity() });
            }
        }
    }

    private void loadStudentsForSession(String sid) {
        studentModel.setRowCount(0);
        for (Student s : Data.studentList) {
            if (s.getSessionId().equals(sid)) {
                studentModel.addRow(new Object[]{s.getId(), s.getName(), s.getResearchTitle(), s.getSubmissionStatus(), s.getScore(), s.getLastUploadTime(), s.getRemark()});
            }
        }
    }
    
    private Student findStudentById(String id) { for(Student s:Data.studentList) if(s.getId().equals(id)) return s; return null; }
    
    private Session findSessionById(String id) { for(Session s : Data.sessionList) { if(s.getSessionId().equals(id)) return s; } return null; }

    // ===========================================================================
    // ✨ CUSTOM CALENDAR
    // ===========================================================================
    class DateTimePicker extends JDialog {
        private String selectedDateTime; private boolean confirmed = false; private JLabel lblMonthYear; private JPanel pnlDays; private Calendar cal = Calendar.getInstance();
        private JSpinner hourSpin, minSpin, ampmSpin;
        public DateTimePicker(Frame parent, String initDateTime) {
            super(parent, "Select Date & Time", true); setSize(420, 450); setLocationRelativeTo(parent); setLayout(new BorderLayout(10, 10));
            try { SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm a"); cal.setTime(sdf.parse(initDateTime)); } catch(Exception e) { try { SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd"); cal.setTime(sdfDate.parse(initDateTime)); } catch (Exception ex) {} }
            JPanel pnlTop = new JPanel(new BorderLayout()); pnlTop.setBorder(new EmptyBorder(10, 10, 0, 10));
            JButton btnPrev = new JButton(" < "); JButton btnNext = new JButton(" > ");
            lblMonthYear = new JLabel("", JLabel.CENTER); lblMonthYear.setFont(new Font("SansSerif", Font.BOLD, 16));
            btnPrev.addActionListener(e -> { cal.add(Calendar.MONTH, -1); updateCalendar(); });
            btnNext.addActionListener(e -> { cal.add(Calendar.MONTH, 1); updateCalendar(); });
            pnlTop.add(btnPrev, BorderLayout.WEST); pnlTop.add(lblMonthYear, BorderLayout.CENTER); pnlTop.add(btnNext, BorderLayout.EAST); add(pnlTop, BorderLayout.NORTH);
            pnlDays = new JPanel(new GridLayout(0, 7, 5, 5)); pnlDays.setBorder(new EmptyBorder(10, 15, 10, 15)); add(pnlDays, BorderLayout.CENTER);
            JPanel pnlBottom = new JPanel(new BorderLayout()); JPanel pnlTime = new JPanel(new FlowLayout(FlowLayout.CENTER)); pnlTime.setBorder(BorderFactory.createTitledBorder("Time Selection"));
            hourSpin = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1)); minSpin = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1)); minSpin.setEditor(new JSpinner.NumberEditor(minSpin, "00")); ampmSpin = new JSpinner(new SpinnerListModel(new String[]{"AM", "PM"}));
            int h = cal.get(Calendar.HOUR); hourSpin.setValue(h == 0 ? 12 : h); minSpin.setValue(cal.get(Calendar.MINUTE)); ampmSpin.setValue(cal.get(Calendar.AM_PM) == Calendar.AM ? "AM" : "PM");
            pnlTime.add(new JLabel("Hour:")); pnlTime.add(hourSpin); pnlTime.add(new JLabel(":")); pnlTime.add(minSpin); pnlTime.add(ampmSpin);
            JPanel pnlBtn = new JPanel(new FlowLayout(FlowLayout.RIGHT)); JButton btnOK = new JButton("OK"); JButton btnCancel = new JButton("Cancel");
            btnOK.addActionListener(e -> {
                int y = cal.get(Calendar.YEAR); int m = cal.get(Calendar.MONTH) + 1; int d = cal.get(Calendar.DAY_OF_MONTH);
                String dateStr = String.format("%04d-%02d-%02d", y, m, d);
                String timeStr = String.format("%02d:%02d %s", hourSpin.getValue(), minSpin.getValue(), ampmSpin.getValue());
                selectedDateTime = dateStr + " " + timeStr; confirmed = true; setVisible(false);
            });
            btnCancel.addActionListener(e -> setVisible(false));
            pnlBtn.add(btnOK); pnlBtn.add(btnCancel); pnlBottom.add(pnlTime, BorderLayout.NORTH); pnlBottom.add(pnlBtn, BorderLayout.SOUTH); add(pnlBottom, BorderLayout.SOUTH);
            updateCalendar();
        }
        private void updateCalendar() {
            pnlDays.removeAll(); String[] days = {"Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"};
            for(String d : days) { JLabel l = new JLabel(d, JLabel.CENTER); l.setFont(new Font("SansSerif", Font.BOLD, 12)); if(d.equals("Su") || d.equals("Sa")) l.setForeground(Color.RED); pnlDays.add(l); }
            Calendar temp = (Calendar) cal.clone(); temp.set(Calendar.DAY_OF_MONTH, 1);
            lblMonthYear.setText(new SimpleDateFormat("MMMM yyyy").format(cal.getTime()));
            int firstDayOfWeek = temp.get(Calendar.DAY_OF_WEEK); int maxDays = temp.getActualMaximum(Calendar.DAY_OF_MONTH); int selectedDay = cal.get(Calendar.DAY_OF_MONTH);
            for(int i=1; i<firstDayOfWeek; i++) pnlDays.add(new JLabel(""));
            for(int i=1; i<=maxDays; i++) {
                final int day = i; JButton btn = new JButton(String.valueOf(i)); btn.setMargin(new Insets(2,2,2,2)); btn.setFocusable(false); btn.setBackground(Color.WHITE);
                if(i == selectedDay) { btn.setBackground(new Color(180, 220, 255)); btn.setFont(new Font("SansSerif", Font.BOLD, 12)); }
                btn.addActionListener(e -> { cal.set(Calendar.DAY_OF_MONTH, day); updateCalendar(); }); pnlDays.add(btn);
            }
            pnlDays.revalidate(); pnlDays.repaint();
        }
        public boolean isConfirmed() { return confirmed; } public String getSelectedDateTime() { return selectedDateTime; }
    }
}