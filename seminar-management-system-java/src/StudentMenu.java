import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat; 
import java.util.Date;            

public class StudentMenu extends JFrame {
    Student currentStudent;
    
    // Top Panel Info
    JLabel lblWelcome;
    JLabel lblSessionID;
    JLabel lblEvaluator;
    JLabel lblTime;
    JLabel lblVenue;

    // Center Panel Details
    JLabel lblTitle;
    JLabel lblSupervisor; 
    JLabel lblType;
    JTextArea txtViewAbstract; 
    
    JTable fileTable;
    DefaultTableModel fileModel;
    JLabel lblDueDate; 
    
    // Status
    JLabel lblStatus;
    JLabel lblScore;
    JLabel lblRemark;

    public StudentMenu(Student student) {
        this.currentStudent = student;
        setTitle("Student Dashboard - " + student.getName());
        
        setSize(1000, 650); 
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        setLayout(new BorderLayout(5, 5)); 

        // =================================================================
        // 1. Top Area
        // =================================================================
        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));
        
        // --- 1.1 Top Navigation Bar ---
        JPanel navBar = new JPanel(new BorderLayout());
        navBar.setBorder(BorderFactory.createEmptyBorder(15, 5, 5, 20)); 
        navBar.setAlignmentX(Component.LEFT_ALIGNMENT); 
        navBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50)); 
        
        lblWelcome = new JLabel();
        lblWelcome.setFont(new Font("SansSerif", Font.BOLD, 22)); 
        navBar.add(lblWelcome, BorderLayout.WEST);
        
        JButton btnTopLogout = new JButton("Logout ➜");
        btnTopLogout.setFocusable(false);
        btnTopLogout.setBackground(new Color(255, 200, 200)); 
        btnTopLogout.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnTopLogout.addActionListener(e -> {
            dispose(); 
            new LoginFrame().setVisible(true); 
        });
        navBar.add(btnTopLogout, BorderLayout.EAST);
        
        // --- 1.2 Session Info Area ---
        JPanel sessionInfoPanel = new JPanel();
        sessionInfoPanel.setLayout(new BoxLayout(sessionInfoPanel, BoxLayout.Y_AXIS));
        sessionInfoPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 10, 20)); 
        sessionInfoPanel.setAlignmentX(Component.LEFT_ALIGNMENT); 
        
        lblSessionID = new JLabel();
        lblSessionID.setFont(new Font("SansSerif", Font.BOLD, 15));
        lblSessionID.setForeground(new Color(0, 51, 102));
        lblSessionID.setAlignmentX(Component.LEFT_ALIGNMENT); 

        lblEvaluator = new JLabel();
        lblEvaluator.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblEvaluator.setForeground(new Color(0, 102, 0));
        lblEvaluator.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblTime = new JLabel();
        lblTime.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblTime.setForeground(Color.DARK_GRAY);
        lblTime.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblVenue = new JLabel();
        lblVenue.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblVenue.setForeground(new Color(153, 0, 76));
        lblVenue.setAlignmentX(Component.LEFT_ALIGNMENT);

        sessionInfoPanel.add(lblSessionID);
        sessionInfoPanel.add(Box.createVerticalStrut(5)); 
        sessionInfoPanel.add(lblEvaluator);
        sessionInfoPanel.add(Box.createVerticalStrut(3));
        sessionInfoPanel.add(lblTime);
        sessionInfoPanel.add(Box.createVerticalStrut(3));
        sessionInfoPanel.add(lblVenue);
        
        topContainer.add(navBar);
        topContainer.add(sessionInfoPanel);
        
        add(topContainer, BorderLayout.NORTH);

        // =================================================================
        // 2. Center Panel
        // =================================================================
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBorder(BorderFactory.createTitledBorder("🎓 My Project Details"));

        // Title
        lblTitle = new JLabel();
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 16)); 
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Supervisor
        lblSupervisor = new JLabel();
        lblSupervisor.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblSupervisor.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Type
        lblType = new JLabel();
        lblType.setFont(new Font("SansSerif", Font.ITALIC, 14));
        lblType.setForeground(Color.DARK_GRAY);
        lblType.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Abstract
        JLabel lblAbsHeader = new JLabel("Abstract:");
        lblAbsHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblAbsHeader.setFont(new Font("SansSerif", Font.BOLD, 13));
        
        txtViewAbstract = new JTextArea();
        txtViewAbstract.setEditable(false);
        txtViewAbstract.setLineWrap(true);
        txtViewAbstract.setWrapStyleWord(true);
        txtViewAbstract.setBackground(new Color(240, 240, 240));
        txtViewAbstract.setFont(new Font("SansSerif", Font.PLAIN, 13)); 
        
        JScrollPane scrollAbs = new JScrollPane(txtViewAbstract);
        scrollAbs.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollAbs.setPreferredSize(new Dimension(800, 70)); 
        scrollAbs.setBorder(BorderFactory.createEtchedBorder());

        // File Table
        JLabel lblFilesHeader = new JLabel("Uploaded Files (Double-click to open):");
        lblFilesHeader.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblFilesHeader.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblFilesHeader.setBorder(BorderFactory.createEmptyBorder(8, 0, 3, 0));

        String[] fileCols = {"File Name", "Upload Time"};
        fileModel = new DefaultTableModel(fileCols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; } 
        };
        fileTable = new JTable(fileModel);
        fileTable.setRowHeight(25); 
        fileTable.setFillsViewportHeight(true);
        
        fileTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { 
                    int row = fileTable.getSelectedRow();
                    if (row != -1) openFileAtRow(row);
                }
            }
        });

        JScrollPane scrollFiles = new JScrollPane(fileTable);
        scrollFiles.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollFiles.setPreferredSize(new Dimension(800, 90)); 

        // Due Date
        lblDueDate = new JLabel("Submission Due Date: -");
        lblDueDate.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblDueDate.setForeground(new Color(200, 0, 0)); 
        lblDueDate.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblDueDate.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        // Status Panel
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        statusPanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY)); 
        statusPanel.setMaximumSize(new Dimension(2000, 45));

        lblStatus = new JLabel("Status: -");
        lblStatus.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblScore = new JLabel(" | Score: -");
        lblScore.setFont(new Font("SansSerif", Font.PLAIN, 14));
        lblRemark = new JLabel(" | Remark: -");
        lblRemark.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        statusPanel.add(lblStatus);
        statusPanel.add(lblScore);
        statusPanel.add(lblRemark);
        
        // Compact layout
        centerPanel.add(Box.createVerticalStrut(5));
        centerPanel.add(lblTitle);
        centerPanel.add(Box.createVerticalStrut(3));
        centerPanel.add(lblSupervisor); 
        centerPanel.add(Box.createVerticalStrut(3));
        centerPanel.add(lblType);
        centerPanel.add(Box.createVerticalStrut(8));
        centerPanel.add(lblAbsHeader);
        centerPanel.add(scrollAbs);
        centerPanel.add(lblFilesHeader);
        centerPanel.add(scrollFiles);
        centerPanel.add(lblDueDate); 
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(statusPanel);
        
        add(centerPanel, BorderLayout.CENTER);

        // =================================================================
        // 3. Bottom Panel
        // =================================================================
        JPanel botPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        
        JButton btnUpload = new JButton("📤 Upload New File");
        btnUpload.setPreferredSize(new Dimension(160, 35));
        
        JButton btnEditDetails = new JButton("✏️ Edit Project Details");
        btnEditDetails.setBackground(new Color(255, 255, 204));
        btnEditDetails.setPreferredSize(new Dimension(180, 35));
        
        botPanel.add(btnUpload);
        botPanel.add(btnEditDetails);
        add(botPanel, BorderLayout.SOUTH);

        refreshDashboard();

        // --- Listeners ---
        btnUpload.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File sourceFile = fileChooser.getSelectedFile();
                if (!sourceFile.exists() || !sourceFile.canRead()) {
                    JOptionPane.showMessageDialog(this, "Error: Cannot read selected file.");
                    return;
                }
                try {
                    File storageDir = new File("storage");
                    if (!storageDir.exists()) storageDir.mkdirs();

                    // 👇👇👇 [Modification Start] Custom file naming format 👇👇👇
                    // Format: yyyyMMdd_HHmm (Because Windows doesn't support colons ":")
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmm");
                    String timeStr = sdf.format(new Date());

                    // Handle names and filenames (replace spaces with underscores to prevent format issues)
                    String safeStudentName = currentStudent.getName().replace(" ", "_");
                    String safeFileName = sourceFile.getName().replace(" ", "_");

                    // Combine new filename: ID_Name_Date_Time_OriginalName
                    // Example: S101_Alice_20260206_2230_Report.pdf
                    String newName = currentStudent.getId() + "_" + safeStudentName + "_" + timeStr + "_" + safeFileName;
                    // 👆👆👆 [Modification End] 👆👆👆

                    File destFile = new File(storageDir, newName);
                    Files.copy(sourceFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    currentStudent.addSubmission(newName);
                    JOptionPane.showMessageDialog(this, "File Uploaded Successfully!");
                    refreshDashboard();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Upload Error: " + ex.getMessage());
                }
            }
        });

        btnEditDetails.addActionListener(e -> openEditDialog());
    }

    // --- Helpers ---

    private void openFileAtRow(int row) {
        java.util.List<Submission> list = currentStudent.getSortedSubmissions();
        if (row >= 0 && row < list.size()) {
            Submission sub = list.get(row);
            File f = new File("storage/" + sub.getSavedFileName());
            if (!f.exists()) { JOptionPane.showMessageDialog(this, "File not found!"); return; }
            try {
                java.awt.Desktop.getDesktop().open(f);
            } catch (Exception ex) {
                try {
                    Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler \"" + f.getAbsolutePath() + "\"");
                } catch (Exception ex2) {
                    JOptionPane.showMessageDialog(this, "Cannot open file: " + ex.getMessage());
                }
            }
        }
    }

    private void refreshDashboard() {
        lblWelcome.setText("👋 Welcome, " + currentStudent.getName() + " (" + currentStudent.getId() + ")");
        
        Session s = findSessionById(currentStudent.getSessionId());
        if (s != null) {
            lblSessionID.setText("📅 Session: " + s.getSessionId());
            lblEvaluator.setText("👨‍🏫 Evaluator: " + s.getEvaluatorName());
            
            String presentationDT = s.getDate() + " " + s.getTime();
            lblTime.setText("⏰ Presentation Time: " + presentationDT);
            lblVenue.setText("📍 Venue: " + s.getVenue());
            
            String rawDue = s.getSubmissionDueDate();
            String dueDateDisplay;
            if (rawDue == null || rawDue.equals("-")) {
                dueDateDisplay = "Not Set";
            } else if (rawDue.trim().length() <= 10) { 
                dueDateDisplay = rawDue + " 11:59 PM"; 
            } else {
                dueDateDisplay = rawDue; 
            }
            lblDueDate.setText("⏰ Submission Due Date: " + dueDateDisplay);
        } else {
            lblSessionID.setText("📅 Session: Unassigned (Pending Coordinator)");
            lblEvaluator.setText("👨‍🏫 Evaluator: -");
            lblTime.setText("⏰ Presentation Time: -");
            lblVenue.setText("📍 Venue: -");
            lblDueDate.setText("⏰ Submission Due Date: -");
        }

        lblTitle.setText("Title: " + currentStudent.getResearchTitle());
        lblSupervisor.setText("👨‍💼 Supervisor: " + currentStudent.getSupervisorName());
        lblType.setText("Presentation Type: " + currentStudent.getPresentationType());
        txtViewAbstract.setText(currentStudent.getProjectAbstract());
        
        lblStatus.setText("Submission Status: " + currentStudent.getSubmissionStatus());
        lblRemark.setText("  |  Remark: " + currentStudent.getRemark());
        
        if (currentStudent.getSubmissionStatus().equals("Evaluated")) {
            lblStatus.setForeground(new Color(0, 128, 0));
            lblScore.setText("  |  Score: " + currentStudent.getScore());
            lblScore.setForeground(Color.RED);
        } else {
            lblStatus.setForeground(Color.BLACK);
            lblScore.setText("  |  Score: " + currentStudent.getScore());
            lblScore.setForeground(Color.BLACK);
        }
        
        fileModel.setRowCount(0); 
        for (Submission sub : currentStudent.getSortedSubmissions()) {
            fileModel.addRow(new Object[]{sub.getSavedFileName(), sub.getFormattedTime()});
        }
    }

    private void openEditDialog() {
        JDialog d = new JDialog(this, "Edit Project Details", true);
        d.setSize(650, 550); 
        d.setLocationRelativeTo(this);
        d.setLayout(new BorderLayout());
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Research Title:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0; 
        JTextField txtTitleEdit = new JTextField(currentStudent.getResearchTitle());
        formPanel.add(txtTitleEdit, gbc);
        
        // Supervisor
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Supervisor:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0; 
        JTextField txtSupervisorEdit = new JTextField(currentStudent.getSupervisorName());
        formPanel.add(txtSupervisorEdit, gbc);

        // Type
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; gbc.anchor = GridBagConstraints.EAST;
        formPanel.add(new JLabel("Presentation Type:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0; gbc.anchor = GridBagConstraints.WEST;
        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JRadioButton rbOral = new JRadioButton("Oral Presentation");
        JRadioButton rbPoster = new JRadioButton("Poster Presentation");
        ButtonGroup bg = new ButtonGroup(); bg.add(rbOral); bg.add(rbPoster);
        if ("Poster".equals(currentStudent.getPresentationType())) rbPoster.setSelected(true);
        else rbOral.setSelected(true);
        typePanel.add(rbOral); typePanel.add(Box.createHorizontalStrut(20)); typePanel.add(rbPoster);
        formPanel.add(typePanel, gbc);

        // Abstract
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0; gbc.weighty = 0; gbc.anchor = GridBagConstraints.NORTHEAST;
        formPanel.add(new JLabel("Project Abstract:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 1.0; gbc.weighty = 1.0; gbc.fill = GridBagConstraints.BOTH;
        JTextArea txtAbstractEdit = new JTextArea(currentStudent.getProjectAbstract());
        txtAbstractEdit.setLineWrap(true); txtAbstractEdit.setWrapStyleWord(true);
        formPanel.add(new JScrollPane(txtAbstractEdit), gbc);

        d.add(formPanel, BorderLayout.CENTER);
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnSave = new JButton("💾 Save Changes");
        btnSave.setBackground(new Color(50, 205, 50)); 
        btnSave.addActionListener(ev -> {
            if (txtTitleEdit.getText().trim().isEmpty()) return;
            currentStudent.setResearchTitle(txtTitleEdit.getText().trim());
            currentStudent.setSupervisorName(txtSupervisorEdit.getText().trim()); 
            currentStudent.setPresentationType(rbPoster.isSelected() ? "Poster" : "Oral");
            currentStudent.setProjectAbstract(txtAbstractEdit.getText().trim());
            JOptionPane.showMessageDialog(d, "Details updated!");
            d.dispose();
            refreshDashboard();
        });
        btnPanel.add(btnSave);
        d.add(btnPanel, BorderLayout.SOUTH);
        d.setVisible(true);
    }
    
    private Session findSessionById(String id) {
        for(Session s : Data.sessionList) if(s.getSessionId().equals(id)) return s;
        return null;
    }
}