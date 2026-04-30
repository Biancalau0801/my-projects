import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    public RegisterFrame() {
        setTitle("Student Registration");
        setSize(450, 650); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- Title ---
        JLabel lblHeader = new JLabel("🎓 Student Registration", JLabel.CENTER);
        lblHeader.setFont(new Font("SansSerif", Font.BOLD, 20));
        lblHeader.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(lblHeader, BorderLayout.NORTH);

        // --- Form Panel ---
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 5, 8, 5); 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // 1. Student ID
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        formPanel.add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        JTextField txtId = new JTextField();
        formPanel.add(txtId, gbc);

        // 2. Name
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        formPanel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        JTextField txtName = new JTextField();
        formPanel.add(txtName, gbc);

        // 3. Password
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0;
        formPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
        JPasswordField txtPass = new JPasswordField();
        formPanel.add(txtPass, gbc);

        // 4. Research Title
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0;
        formPanel.add(new JLabel("Research Title:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 1.0;
        JTextField txtTitle = new JTextField();
        formPanel.add(txtTitle, gbc);

        // 5. Supervisor Name
        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0;
        formPanel.add(new JLabel("Supervisor Name:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; gbc.weightx = 1.0;
        JTextField txtSupervisor = new JTextField();
        formPanel.add(txtSupervisor, gbc);

        gbc.gridx = 0; gbc.gridy = 5; gbc.weightx = 0; 
        gbc.anchor = GridBagConstraints.NORTHWEST; 
        formPanel.add(new JLabel("Abstract:"), gbc);

        gbc.gridx = 1; gbc.gridy = 5; gbc.weightx = 1.0; gbc.weighty = 0.5; 
        gbc.fill = GridBagConstraints.BOTH; 
        
        JTextArea txtAbstract = new JTextArea(4, 20);
        txtAbstract.setLineWrap(true);  
        txtAbstract.setWrapStyleWord(true);  
        JScrollPane scrollAbs = new JScrollPane(txtAbstract); 
        formPanel.add(scrollAbs, gbc);

        
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        // 7. Presentation Type
        gbc.gridx = 0; gbc.gridy = 6; gbc.weightx = 0;
        formPanel.add(new JLabel("Preferred Type:"), gbc);
        gbc.gridx = 1; gbc.gridy = 6; gbc.weightx = 1.0;
        
        JPanel typePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        JRadioButton rbOral = new JRadioButton("Oral");
        JRadioButton rbPoster = new JRadioButton("Poster");
        rbOral.setSelected(true); 
        ButtonGroup bg = new ButtonGroup();
        bg.add(rbOral); bg.add(rbPoster);
        typePanel.add(rbOral);
        typePanel.add(Box.createHorizontalStrut(15));
        typePanel.add(rbPoster);
        formPanel.add(typePanel, gbc);

        add(formPanel, BorderLayout.CENTER);

        // --- Bottom Buttons ---
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton btnRegister = new JButton("Register Now");
        btnRegister.setBackground(new Color(50, 205, 50));
        btnRegister.setForeground(Color.WHITE);
        
        JButton btnCancel = new JButton("Cancel");

        btnPanel.add(btnRegister);
        btnPanel.add(btnCancel);
        add(btnPanel, BorderLayout.SOUTH);

        // --- Logic ---
        
        btnRegister.addActionListener(e -> {
            String id = txtId.getText().trim();
            String name = txtName.getText().trim();
            String pass = new String(txtPass.getPassword());
            String title = txtTitle.getText().trim();
            String supervisor = txtSupervisor.getText().trim();
            String abstractText = txtAbstract.getText().trim(); 
            
            if (id.isEmpty() || name.isEmpty() || pass.isEmpty() || title.isEmpty() || supervisor.isEmpty() || abstractText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields (including Abstract)!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            for (Student s : Data.studentList) {
                if (s.getId().equals(id)) {
                    JOptionPane.showMessageDialog(this, "Student ID already exists!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Create new student
            Student newStudent = new Student(id, name, pass, title, supervisor);
            
            newStudent.setProjectAbstract(abstractText);
            
            // set Presentation Type
            if (rbPoster.isSelected()) {
                newStudent.setPresentationType("Poster");
            } else {
                newStudent.setPresentationType("Oral");
            }

            // save data to list
            Data.studentList.add(newStudent);

            JOptionPane.showMessageDialog(this, "Registration Successful!\nPlease login.");
            dispose(); 
            new LoginFrame().setVisible(true); 
        });

        btnCancel.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
    }
}