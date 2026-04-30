import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File; 
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.text.SimpleDateFormat;

public class CoordinatorMenu extends JFrame {
    
    // Global Variables
    Coordinator currentCoordinator;
    JTable sessionTable;
    DefaultTableModel sessionModel;
    
    // Leaderboard Models
    DefaultTableModel oralModel;
    DefaultTableModel posterModel;
    
    // Batch Assign Models
    DefaultTableModel assignModel;
    JTable assignTable;
    JComboBox<String> sessionComboBoxEditor; 

    public CoordinatorMenu(Coordinator coordinator) {
        this.currentCoordinator = coordinator;
        
        setTitle("Coordinator Dashboard - " + coordinator.getName());
        setSize(1280, 700); 
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // --- 1. Top Panel ---
        JPanel topPanel = new JPanel(new BorderLayout()); 
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10)); 
        JLabel lblWelcome = new JLabel("Welcome, Coordinator " + coordinator.getName());
        lblWelcome.setFont(new Font("SansSerif", Font.BOLD, 16));
        topPanel.add(lblWelcome, BorderLayout.WEST);
        JButton btnTopLogout = new JButton("Logout ➜");
        btnTopLogout.setFocusable(false);
        btnTopLogout.setBackground(new Color(255, 200, 200)); 
        btnTopLogout.addActionListener(e -> { dispose(); new LoginFrame().setVisible(true); });
        topPanel.add(btnTopLogout, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // --- 2. Center Area (SplitPane) ---
        // A. Session Table
        String[] columns = { "Session ID", "Date", "Time", "Venue", "Session Type", "Evaluator", "Availability", "Edit Session" };
        sessionModel = new DefaultTableModel(columns, 0) { @Override public boolean isCellEditable(int r, int c) { return c == 7; } };
        sessionTable = new JTable(sessionModel);
        sessionTable.setRowHeight(30); 
        sessionTable.setFillsViewportHeight(true);
        
        sessionTable.getColumnModel().getColumn(0).setPreferredWidth(80);  
        sessionTable.getColumnModel().getColumn(4).setPreferredWidth(80);  
        sessionTable.getColumnModel().getColumn(5).setPreferredWidth(150); 
        sessionTable.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer("Edit"));
        sessionTable.getColumnModel().getColumn(7).setCellEditor(new SessionEditor(new JCheckBox()));
        
        sessionTable.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { 
                    int row = sessionTable.getSelectedRow();
                    if (row != -1 && sessionTable.getSelectedColumn() != 7) 
                        viewSessionDetails((String)sessionModel.getValueAt(row, 0)); 
                }
            }
        });
        JScrollPane sessionScrollPane = new JScrollPane(sessionTable);
        sessionScrollPane.setBorder(BorderFactory.createTitledBorder("📅 Presentation Schedules (Double-click row to view Students)"));
        sessionScrollPane.setPreferredSize(new Dimension(1200, 200)); 

        // B. Bottom Panel
        JPanel bottomPanel = new JPanel(new GridLayout(1, 3, 10, 0)); 
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // Oral & Poster Tables
        String[] rankCols = {"Rank", "Name", "Session", "Score"};
        oralModel = new DefaultTableModel(rankCols, 0);
        JTable oralTable = new JTable(oralModel); oralTable.setEnabled(false); oralTable.setRowHeight(25); 
        oralTable.getTableHeader().setBackground(new Color(230, 240, 255));
        JScrollPane oralScroll = new JScrollPane(oralTable);
        oralScroll.setBorder(BorderFactory.createTitledBorder("🎤 Best Award Oral"));
        
        posterModel = new DefaultTableModel(rankCols, 0);
        JTable posterTable = new JTable(posterModel); posterTable.setEnabled(false); posterTable.setRowHeight(25);
        posterTable.getTableHeader().setBackground(new Color(255, 240, 230));
        JScrollPane posterScroll = new JScrollPane(posterTable);
        posterScroll.setBorder(BorderFactory.createTitledBorder("🖼️ Best Award Poster"));
        
        // Batch Assign
        JPanel assignPanel = new JPanel(new BorderLayout());
        assignPanel.setBorder(BorderFactory.createTitledBorder("📝 Unassigned Students (Direct Assign)"));
        String[] assignCols = {"No", "ID", "Name", "Title", "Type", "Assign Session"};
        assignModel = new DefaultTableModel(assignCols, 0) { @Override public boolean isCellEditable(int r, int c) { return c == 5; } };
        assignTable = new JTable(assignModel); assignTable.setRowHeight(25); assignTable.setFillsViewportHeight(true);
        assignTable.getColumnModel().getColumn(0).setPreferredWidth(30); 
        assignTable.getColumnModel().getColumn(5).setPreferredWidth(180); 
        sessionComboBoxEditor = new JComboBox<>();
        assignTable.getColumnModel().getColumn(5).setCellEditor(new DefaultCellEditor(sessionComboBoxEditor));
        JScrollPane assignScroll = new JScrollPane(assignTable);
        assignPanel.add(assignScroll, BorderLayout.CENTER);
        JButton btnSubmitAssign = new JButton("✅ Assign Session");
        btnSubmitAssign.setBackground(new Color(144, 238, 144));
        btnSubmitAssign.addActionListener(e -> submitAssignments());
        JPanel assignBotPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        assignBotPanel.add(btnSubmitAssign);
        assignPanel.add(assignBotPanel, BorderLayout.SOUTH);

        bottomPanel.add(oralScroll); bottomPanel.add(posterScroll); bottomPanel.add(assignPanel);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, sessionScrollPane, bottomPanel);
        splitPane.setDividerLocation(250); splitPane.setResizeWeight(0.3); 
        add(splitPane, BorderLayout.CENTER);

        refreshAllData();

        // --- 3. Bottom Buttons (Removed Manage Awards) ---
        JPanel botPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton btnCreate = new JButton("➕ Create Session");
        JButton btnDelete = new JButton("🗑️ Delete Session");
        JButton btnAddEvaluator = new JButton("👤 Add Evaluator Account");
        // Removed btnManageAwards
        JButton btnGenerateReports = new JButton("📊 Generate Reports");
        
        botPanel.add(btnCreate); 
        botPanel.add(btnDelete); 
        botPanel.add(btnAddEvaluator);
        botPanel.add(btnGenerateReports);
        
        add(botPanel, BorderLayout.SOUTH);

        // Listeners
        btnCreate.addActionListener(e -> { 
            JTextField txtId = new JTextField(); JTextField txtVenue = new JTextField(); JTextField txtCap = new JTextField("5");
            String[] sessionTypes = {"Oral", "Poster"}; JComboBox<String> cboSessionType = new JComboBox<>(sessionTypes);
            JButton btnDate = new JButton("📅 Select Date & Time");
            final String[] selectedDT = { new SimpleDateFormat("yyyy-MM-dd HH:mm a").format(new Date()) };
            btnDate.setText(selectedDT[0]);
            btnDate.addActionListener(evt -> { DateTimePicker p = new DateTimePicker(this, selectedDT[0]); p.setVisible(true); if(p.isConfirmed()) btnDate.setText(selectedDT[0]=p.getSelectedDateTime()); });
            Object[] msg = { "ID:", txtId, "Date/Time:", btnDate, "Venue:", txtVenue, "Cap:", txtCap, "Type:", cboSessionType };
            if (JOptionPane.showConfirmDialog(null, msg, "Create Session", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                try { Data.sessionList.add(new Session(txtId.getText(), selectedDT[0].split(" ",2)[0], selectedDT[0].split(" ",2)[1], txtVenue.getText(), Integer.parseInt(txtCap.getText()), (String)cboSessionType.getSelectedItem())); refreshAllData(); JOptionPane.showMessageDialog(null, "Created!"); } catch (Exception ex) {}
            }
        });
        
        btnDelete.addActionListener(e -> {
            int row = sessionTable.getSelectedRow();
            if (row != -1 && JOptionPane.showConfirmDialog(null, "Delete?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) { Data.sessionList.remove(row); refreshAllData(); }
        });
        
        btnAddEvaluator.addActionListener(e -> {
            JTextField id = new JTextField(), name = new JTextField(); JPasswordField pass = new JPasswordField();
            if (JOptionPane.showConfirmDialog(null, new Object[]{"ID:", id, "Name:", name, "Pass:", pass}, "New Evaluator", JOptionPane.OK_CANCEL_OPTION)==0) 
                Data.evaluatorList.add(new Evaluator(id.getText(), name.getText(), new String(pass.getPassword())));
        });
        
        // Removed btnManageAwards Listener
        
        // 👇👇👇【Final Report Logic】👇👇👇
        btnGenerateReports.addActionListener(e -> {
            String[] t = {"Seminar Schedule", "Evaluation Report", "Award Report", "Analytics Summary"};
            String ch = (String) JOptionPane.showInputDialog(null, "Report:", "Gen", 3, null, t, t[0]);
            
            if(ch != null) {
                String c = ""; 
                if(ch.contains("Seminar")) c = ReportGenerator.generateSeminarSchedule(); 
                else if(ch.contains("Evaluation")) c = ReportGenerator.generateEvaluationReport(); 
                else if(ch.contains("Award")) c = ReportGenerator.generateAwardReport(); 
                else c = ReportGenerator.generateAnalyticsReport();
                
                // 1. Create text area
                JTextArea ta = new JTextArea(c);
                
                // 2. 🌟 Force Monospaced font to ensure vertical alignment 🌟
                ta.setFont(new Font("Monospaced", Font.PLAIN, 12)); 
                ta.setEditable(false);
                
                // 3. Put into ScrollPane
                JScrollPane scrollPane = new JScrollPane(ta);
                scrollPane.setPreferredSize(new Dimension(1100, 600)); 
                
                // 4. Create panel containing ScrollPane and Bottom Prompt
                JPanel panel = new JPanel(new BorderLayout());
                panel.add(scrollPane, BorderLayout.CENTER);
                JLabel lblPrompt = new JLabel("Do you want to export this report to file? (Click YES to Export)", JLabel.CENTER);
                lblPrompt.setFont(new Font("SansSerif", Font.BOLD, 14));
                lblPrompt.setForeground(Color.BLUE);
                lblPrompt.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); 
                panel.add(lblPrompt, BorderLayout.SOUTH);

                // Show the dialog
                int choice = JOptionPane.showConfirmDialog(null, 
                        panel, 
                        "Preview Report", 
                        JOptionPane.YES_NO_OPTION);
                        
                // Export if YES selected
                if(choice == JOptionPane.YES_OPTION) {
                    ReportGenerator.exportToFile(c, ch);
                }
            }
        });
        // 👆👆👆
    }

    private void submitAssignments() {
        if (assignTable.isEditing()) assignTable.getCellEditor().stopCellEditing();
        int successCount = 0; StringBuilder errorMsg = new StringBuilder(); boolean hasUpdates = false;
        for (int i = 0; i < assignModel.getRowCount(); i++) {
            String selectedSessionStr = (String) assignModel.getValueAt(i, 5);
            if (selectedSessionStr != null && !selectedSessionStr.equals("- Select -")) {
                String studentId = (String) assignModel.getValueAt(i, 1);
                String studentType = (String) assignModel.getValueAt(i, 4);
                String sessionId = selectedSessionStr.split(" ")[0];
                Session targetSession = null;
                for (Session s : Data.sessionList) if (s.getSessionId().equals(sessionId)) targetSession = s;
                if (targetSession != null) {
                    if (!targetSession.getSessionType().equalsIgnoreCase(studentType)) { errorMsg.append("❌ Type Mismatch: ").append(studentId).append("\n"); continue; }
                    int currentCount = Data.getStudentCountForSession(sessionId);
                    if (currentCount >= targetSession.getCapacity()) { errorMsg.append("⚠️ Full: ").append(sessionId).append("\n"); continue; }
                    for (Student s : Data.studentList) { if (s.getId().equals(studentId)) { s.setSessionId(sessionId); successCount++; hasUpdates = true; break; } }
                }
            }
        }
        if (hasUpdates) { refreshAllData(); JOptionPane.showMessageDialog(this, "Assigned " + successCount + " students!\n" + errorMsg.toString()); } 
        else if (errorMsg.length() > 0) JOptionPane.showMessageDialog(this, errorMsg.toString(), "Error", 0);
    }

    private void refreshAllData() {
        sessionModel.setRowCount(0);
        for (Session s : Data.sessionList) sessionModel.addRow(new Object[]{s.getSessionId(), s.getDate(), s.getTime(), s.getVenue(), s.getSessionType(), s.getEvaluatorName(), Data.getStudentCountForSession(s.getSessionId()) + " / " + s.getCapacity(), "Edit"});
        
        oralModel.setRowCount(0); posterModel.setRowCount(0);
        Comparator<Student> c = (s1, s2) -> { double v1=0, v2=0; try{v1=Double.parseDouble(s1.getScore());}catch(Exception e){} try{v2=Double.parseDouble(s2.getScore());}catch(Exception e){} return Double.compare(v2, v1); };
        List<Student> o = Data.studentList.stream().filter(s->"Oral".equals(s.getPresentationType())).sorted(c).collect(Collectors.toList());
        List<Student> p = Data.studentList.stream().filter(s->"Poster".equals(s.getPresentationType())).sorted(c).collect(Collectors.toList());
        int r=1; for(Student s:o) oralModel.addRow(new Object[]{r++, s.getName(), s.getSessionId(), s.getScore()});
        r=1; for(Student s:p) posterModel.addRow(new Object[]{r++, s.getName(), s.getSessionId(), s.getScore()});
        
        assignModel.setRowCount(0);
        sessionComboBoxEditor.removeAllItems(); sessionComboBoxEditor.addItem("- Select -");
        for (Session s : Data.sessionList) {
            int count = Data.getStudentCountForSession(s.getSessionId());
            if (count < s.getCapacity()) sessionComboBoxEditor.addItem(String.format("%s (%s) [%d/%d]", s.getSessionId(), s.getSessionType(), count, s.getCapacity()));
        }
        int no = 1;
        for (Student s : Data.studentList) {
            if (s.getSessionId() == null || s.getSessionId().equals("Unassigned")) {
                assignModel.addRow(new Object[]{no++, s.getId(), s.getName(), s.getResearchTitle(), s.getPresentationType(), "- Select -"});
            }
        }
    }

    // =========================================================================
    // Session Details: Document Button + Change Session
    // =========================================================================
    private void viewSessionDetails(String sessionId) {
        Session targetSession = null;
        for (Session s : Data.sessionList) { if (s.getSessionId().equals(sessionId)) { targetSession = s; break; } }
        if (targetSession == null) return;

        JDialog d = new JDialog(this, "Session Details: " + sessionId, true);
        d.setSize(1000, 500); d.setLocationRelativeTo(this);
        d.setLayout(new BorderLayout(10, 10));

        JPanel headPanel = new JPanel();
        headPanel.setLayout(new BoxLayout(headPanel, BoxLayout.Y_AXIS));
        headPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headPanel.setBackground(new Color(245, 245, 245));

        JLabel l1 = new JLabel("📍 Venue: " + targetSession.getVenue() + " | 📅 Date: " + targetSession.getDate() + " | ⏰ Time: " + targetSession.getTime());
        l1.setFont(new Font("SansSerif", Font.BOLD, 14));
        JLabel l2 = new JLabel("👨‍🏫 Evaluator: " + targetSession.getEvaluatorName());
        l2.setFont(new Font("SansSerif", Font.PLAIN, 13));
        JLabel l3 = new JLabel("🏷️ Type: " + targetSession.getSessionType());
        JLabel l4 = new JLabel("👥 Students: " + Data.getStudentCountForSession(sessionId) + " / " + targetSession.getCapacity());
        
        headPanel.add(l1); headPanel.add(Box.createVerticalStrut(5));
        headPanel.add(l2); headPanel.add(l3); headPanel.add(l4);
        d.add(headPanel, BorderLayout.NORTH);

        // Updated Columns: "Document" instead of Last Upload
        String[] sCols = {"ID", "Name", "Type", "Title", "Score", "Document", "Action"};
        
        DefaultTableModel sModel = new DefaultTableModel(sCols, 0) {
            // Index 5 (Doc) and 6 (Action) are editable (clickable)
            @Override public boolean isCellEditable(int r, int c) { return c == 5 || c == 6; } 
        };
        
        for (Student s : Data.studentList) {
            if (sessionId.equals(s.getSessionId())) {
                String score = (s.getScore() == null || s.getScore().equals("0")) ? "-" : s.getScore();
                // Determine button text based on file existence
                boolean hasFile = !s.getSortedSubmissions().isEmpty();
                String docStatus = hasFile ? "View File" : "No File";
                
                sModel.addRow(new Object[]{
                    s.getId(), s.getName(), s.getPresentationType(), s.getResearchTitle(), score,
                    docStatus, // This will be rendered as a button
                    "Change Session"
                });
            }
        }

        JTable sTable = new JTable(sModel);
        sTable.setRowHeight(35);
        sTable.getColumnModel().getColumn(0).setPreferredWidth(60); 
        sTable.getColumnModel().getColumn(3).setPreferredWidth(180);
        sTable.getColumnModel().getColumn(4).setPreferredWidth(50);
        sTable.getColumnModel().getColumn(5).setPreferredWidth(100); 
        sTable.getColumnModel().getColumn(6).setPreferredWidth(120); 
        
        // 1. Document Button (Index 5)
        sTable.getColumnModel().getColumn(5).setCellRenderer(new DocumentButtonRenderer());
        sTable.getColumnModel().getColumn(5).setCellEditor(new DocumentButtonEditor(new JCheckBox(), d));

        // 2. Action Button (Index 6)
        sTable.getColumnModel().getColumn(6).setCellRenderer(new DetailButtonRenderer());
        sTable.getColumnModel().getColumn(6).setCellEditor(new StudentMoveEditor(new JCheckBox(), d));

        d.add(new JScrollPane(sTable), BorderLayout.CENTER);

        JPanel bot = new JPanel();
        JButton btnClose = new JButton("Close");
        btnClose.setPreferredSize(new Dimension(100, 30));
        btnClose.addActionListener(e -> { d.dispose(); refreshAllData(); });
        bot.add(btnClose);
        d.add(bot, BorderLayout.SOUTH);

        d.setVisible(true);
        refreshAllData(); 
    }
    
    // --- File Opener Helper ---
    private void openFileSafe(File f, Component parent) {
        if (!f.exists()) { JOptionPane.showMessageDialog(parent, "File not found locally!\nPath: " + f.getAbsolutePath()); return; }
        try { java.awt.Desktop.getDesktop().open(f); } 
        catch (Exception ex) {
            try { Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler \"" + f.getAbsolutePath() + "\""); } 
            catch (Exception ex2) { JOptionPane.showMessageDialog(parent, "Could not open file."); }
        }
    }

    // --- Renderers & Editors for Main Menu ---
    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer(String t) { setText(t); setOpaque(true); setBackground(new Color(230, 240, 255)); }
        public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) { return this; }
    }
    
    class SessionEditor extends DefaultCellEditor {
        JButton button; boolean isPushed; int currentRow;
        public SessionEditor(JCheckBox c) { super(c); button = new JButton(); button.addActionListener(e -> fireEditingStopped()); }
        public Component getTableCellEditorComponent(JTable t, Object v, boolean s, int r, int c) { button.setText("Edit"); isPushed=true; currentRow=r; return button; }
        public Object getCellEditorValue() { if(isPushed) performEdit(currentRow); isPushed=false; return "Edit"; }
        private void performEdit(int row) {
            Session s = Data.sessionList.get(row);
            String dt = s.getDate() + " " + s.getTime();
            JButton btnD = new JButton("📅 "+dt);
            btnD.addActionListener(e -> { DateTimePicker p = new DateTimePicker(CoordinatorMenu.this, dt); p.setVisible(true); if(p.isConfirmed()) btnD.setText("📅 "+p.getSelectedDateTime()); });
            JTextField v = new JTextField(s.getVenue()), c = new JTextField(""+s.getCapacity());
            String[] ev = Data.evaluatorList.stream().map(ee->ee.getName()+" ("+ee.getId()+")").toArray(String[]::new);
            JComboBox<String> cbE = new JComboBox<>(ev);
            String[] sessionTypes = {"Oral", "Poster"}; JComboBox<String> cbType = new JComboBox<>(sessionTypes); cbType.setSelectedItem(s.getSessionType());
            
            Object[] msg = {"ID: "+s.getSessionId(), "Date:", btnD, "Venue:", v, "Cap:", c, "Type:", cbType, "Eval:", cbE};
            if(JOptionPane.showConfirmDialog(null, msg, "Edit", 2)==0) {
                try {
                    s.setCapacity(Integer.parseInt(c.getText())); s.setVenue(v.getText());
                    String[] t = btnD.getText().replace("📅 ","").split(" ",2); s.setDate(t[0]); s.setTime(t[1]);
                    s.setSessionType((String)cbType.getSelectedItem());
                    if(cbE.getSelectedItem()!=null) s.setEvaluatorName((String)cbE.getSelectedItem());
                    refreshAllData();
                } catch(Exception e){}
            }
        }
    }

    // --- ✨ Renderers & Editors for Detail Dialog ---
    
    // 1. Change Session Button
    class DetailButtonRenderer extends JButton implements TableCellRenderer {
        public DetailButtonRenderer() { setText("Change Session"); setOpaque(true); setBackground(new Color(230, 240, 250)); setFont(new Font("SansSerif", Font.BOLD, 12)); }
        public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) { return this; }
    }

    class StudentMoveEditor extends DefaultCellEditor {
        JButton button; boolean isPushed; int row; JTable table; JDialog parent;
        public StudentMoveEditor(JCheckBox c, JDialog p) { super(c); parent=p; button=new JButton(); button.addActionListener(e -> fireEditingStopped()); }
        public Component getTableCellEditorComponent(JTable t, Object v, boolean s, int r, int c) { button.setText("Change Session"); isPushed=true; row=r; table=t; return button; }
        public Object getCellEditorValue() { if(isPushed) moveStudent(row); isPushed=false; return "Change Session"; }
        private void moveStudent(int r) {
            String sId = (String) table.getModel().getValueAt(r, 0); String sType = (String) table.getModel().getValueAt(r, 2);
            Student student = null; for(Student s:Data.studentList) if(s.getId().equals(sId)) student=s;
            if(student==null) return;
            JComboBox<String> box = new JComboBox<>(); box.addItem("Unassigned"); 
            String currentSessionLabel = null; 
            for(Session s : Data.sessionList) {
                if(s.getSessionType().equalsIgnoreCase(sType)) {
                    int count = Data.getStudentCountForSession(s.getSessionId());
                    if(count < s.getCapacity() || s.getSessionId().equals(student.getSessionId())) {
                        String label = s.getSessionId() + " (" + count + "/" + s.getCapacity() + ")";
                        box.addItem(label);
                        if(s.getSessionId().equals(student.getSessionId())) currentSessionLabel = label;
                    }
                }
            }
            if (currentSessionLabel != null) box.setSelectedItem(currentSessionLabel); else box.setSelectedItem("Unassigned");
            if(JOptionPane.showConfirmDialog(parent, box, "Move " + student.getName() + " to:", JOptionPane.OK_CANCEL_OPTION) == 0) {
                String sel = (String) box.getSelectedItem();
                if(sel.equals("Unassigned")) student.setSessionId("Unassigned"); else student.setSessionId(sel.split(" ")[0]);
                JOptionPane.showMessageDialog(parent, "Moved Successfully!"); parent.dispose(); 
            }
        }
    }

    // 2. View Document Button
    class DocumentButtonRenderer extends JButton implements TableCellRenderer {
        public DocumentButtonRenderer() { setOpaque(true); setFont(new Font("SansSerif", Font.BOLD, 12)); }
        public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) { 
            String text = (String) v;
            setText(text);
            if("No File".equals(text)) { setBackground(Color.LIGHT_GRAY); setEnabled(false); }
            else { setBackground(new Color(144, 238, 144)); setEnabled(true); } // Green for active file
            return this; 
        }
    }

    class DocumentButtonEditor extends DefaultCellEditor {
        JButton button; boolean isPushed; int row; JTable table; JDialog parent;
        public DocumentButtonEditor(JCheckBox c, JDialog p) { super(c); parent=p; button=new JButton(); button.addActionListener(e -> fireEditingStopped()); }
        public Component getTableCellEditorComponent(JTable t, Object v, boolean s, int r, int c) { 
            String text = (String) v; button.setText(text); isPushed=true; row=r; table=t; 
            return button; 
        }
        public Object getCellEditorValue() { if(isPushed) viewFile(row); isPushed=false; return button.getText(); }
        
        private void viewFile(int r) {
            String btnText = button.getText();
            if("No File".equals(btnText)) { return; } // Do nothing if no file

            String sId = (String) table.getModel().getValueAt(r, 0);
            Student student = null; for(Student s:Data.studentList) if(s.getId().equals(sId)) student=s;
            
            if(student != null && !student.getSortedSubmissions().isEmpty()) {
                Submission latest = student.getSortedSubmissions().get(0); // Get latest file
                File f = new File("storage/" + latest.getSavedFileName());
                openFileSafe(f, parent);
            } else {
                JOptionPane.showMessageDialog(parent, "Error: File record not found.");
            }
        }
    }
    
    // Date Picker Class
    class DateTimePicker extends JDialog {
        private String res; private boolean ok; private Calendar cal=Calendar.getInstance();
        private JSpinner h, m, ap;
        public DateTimePicker(Frame p, String init) {
            super(p, "Date/Time", true); setSize(400,450); setLocationRelativeTo(p); setLayout(new BorderLayout());
            try{ cal.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm a").parse(init)); }catch(Exception e){}
            JPanel top=new JPanel(); JButton b1=new JButton("<"), b2=new JButton(">"); JLabel l=new JLabel();
            b1.addActionListener(e->{cal.add(2,-1); upd(l,null);}); b2.addActionListener(e->{cal.add(2,1); upd(l,null);});
            top.add(b1); top.add(l); top.add(b2); add(top, "North");
            JPanel cen=new JPanel(new GridLayout(0,7)); add(cen, "Center");
            JPanel bot=new JPanel(); h=new JSpinner(new SpinnerNumberModel(1,1,12,1)); m=new JSpinner(new SpinnerNumberModel(0,0,59,1)); ap=new JSpinner(new SpinnerListModel(new String[]{"AM","PM"}));
            bot.add(h); bot.add(new JLabel(":")); bot.add(m); bot.add(ap);
            JButton k=new JButton("OK"); k.addActionListener(e->{
                res=String.format("%tF %02d:%02d %s", cal, h.getValue(), m.getValue(), ap.getValue()); ok=true; setVisible(false);
            });
            bot.add(k); add(bot, "South");
            upd(l, cen);
            h.setValue(cal.get(10)==0?12:cal.get(10)); m.setValue(cal.get(12)); ap.setValue(cal.get(9)==0?"AM":"PM");
        }
        private void upd(JLabel l, JPanel p) {
            if(p!=null) p.removeAll();
            l.setText(String.format("%tB %tY", cal, cal));
            if(p==null) return;
            Calendar c=(Calendar)cal.clone(); c.set(5,1);
            for(int i=1;i<c.get(7);i++) p.add(new JLabel(""));
            for(int i=1;i<=c.getActualMaximum(5);i++) {
                int d=i; JButton b=new JButton(""+i); b.setBackground(Color.WHITE);
                if(i==cal.get(5)) b.setBackground(Color.CYAN);
                b.addActionListener(e->{cal.set(5,d); upd(l,p);}); p.add(b);
            }
            p.revalidate(); p.repaint();
        }
        public boolean isConfirmed(){return ok;} public String getSelectedDateTime(){return res;}
    }
}