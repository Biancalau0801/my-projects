import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {
    JTextField txtId = new JTextField(15);
    JPasswordField txtPass = new JPasswordField(15);
    JButton btnLogin = new JButton("Login");
    JButton btnRegister = new JButton("Register Student");

    public LoginFrame() {
        setTitle("Seminar Management System - Login");
        setSize(400, 250); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        setLayout(new GridLayout(4, 2, 10, 10)); 

        // --- 1. Add components to the UI ---
        add(new JLabel("  User ID:", SwingConstants.CENTER)); 
        add(txtId);
        
        add(new JLabel("  Password:", SwingConstants.CENTER)); 
        add(txtPass);
        
        add(new JLabel("")); // Empty placeholder
        add(btnLogin); 
        
        add(new JLabel("")); // Empty placeholder
        add(btnRegister);

        // --- 2. Login Button Logic ---
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = txtId.getText().trim();
                String pass = new String(txtPass.getPassword());
                
                if (id.isEmpty() || pass.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please enter ID and Password!");
                    return;
                }

                // --- Role 1: Check if Student ---
                for (Student s : Data.studentList) {
                    if (s.getId().equals(id) && s.getPassword().equals(pass)) {
                        JOptionPane.showMessageDialog(null, "Login Success! Welcome Student: " + s.getName());
                        new StudentMenu(s).setVisible(true); // Go to Student Menu
                        dispose(); // Close Login Window
                        return;
                    }
                }

                // --- Role 2: Check if Evaluator ---
                for (Evaluator ev : Data.evaluatorList) {
                    if (ev.getId().equals(id) && ev.getPassword().equals(pass)) {
                        JOptionPane.showMessageDialog(null, "Login Success! Welcome Evaluator: " + ev.getName());
                        new EvaluatorMenu(ev).setVisible(true); // Go to Evaluator Menu
                        dispose();
                        return;
                    }
                }

                // --- Role 3: Check if Coordinator ---
                for (Coordinator c : Data.coordinatorList) {
                    if (c.getId().equals(id) && c.getPassword().equals(pass)) {
                        JOptionPane.showMessageDialog(null, "Login Success! Welcome Coordinator: " + c.getName());
                        new CoordinatorMenu(c).setVisible(true); // Go to Coordinator Menu
                        dispose();
                        return;
                    }
                }

                // If not found in any list
                JOptionPane.showMessageDialog(null, "Login Failed! Invalid ID or Password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // --- 3. Register Button Logic ---
        // Links to RegisterFrame (the one with subject selection)
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open RegisterFrame, but keep LoginFrame open (convenient for login after registration)
                new RegisterFrame().setVisible(true);
            }
        });
    }
}