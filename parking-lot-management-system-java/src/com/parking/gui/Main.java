package com.parking.gui;

import com.parking.logic.*;
import com.parking.model.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PrinterException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter; 
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.FontUIResource;
import com.parking.db.DatabaseHelper;
import java.text.SimpleDateFormat;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import java.awt.print.PrinterJob;
import java.awt.print.PageFormat;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    java.util.Locale.setDefault(java.util.Locale.ENGLISH);
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    setGlobalFont(new Font("Microsoft YaHei UI", Font.PLAIN, 18));
                } catch (Exception e) { e.printStackTrace(); }
                new ParkingApp().setVisible(true);
            }
        });
    }

    private static void setGlobalFont(Font font) {
        FontUIResource fontRes = new FontUIResource(font);
        Enumeration<Object> keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof FontUIResource) {
                UIManager.put(key, fontRes);
            }
        }
    }
}

class ParkingApp extends JFrame {
    private ParkingLotSystem system;
    private CardLayout cardLayout;
    private DefaultTableModel tableModel; // 必须定义这个变量
    private JPanel mainPanel;
    private JPanel homePanel;

    public ParkingApp() {
        system = ParkingLotSystem.getInstance();
        setTitle("University Parking Kiosk System");
        setSize(1400, 950);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        homePanel = createHomePanel();
        mainPanel.add(homePanel, "HOME");
        mainPanel.add(new EntryPanel(), "ENTRY");
        mainPanel.add(new ExitPanel(), "EXIT");
        mainPanel.add(createLoginPanel(), "LOGIN");
        
        //adminTabs = new JTabbedPane();
        //mainPanel.add(adminTabs, "ADMIN_DASHBOARD");
        mainPanel.add(new AdminPanel(), "ADMIN_DASHBOARD");
        
        add(mainPanel);
        cardLayout.show(mainPanel, "HOME");
    }

    private JPanel createHomePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(40, 44, 52));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        JLabel title = new JLabel("WELCOME TO UNIVERSITY PARKING", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 48));
        title.setForeground(Color.WHITE);
        gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(title, gbc);
        JLabel subtitle = new JLabel("Please select a service:", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        subtitle.setForeground(Color.LIGHT_GRAY);
        gbc.gridy = 1;
        panel.add(subtitle, gbc);
        JPanel btnPanel = new JPanel(new GridLayout(1, 2, 80, 0));
        btnPanel.setOpaque(false);
        JButton btnEntry = createBigButton("🅿️ PARK VEHICLE", "Find a spot & Check-in", new Color(100, 220, 100));
        btnEntry.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { cardLayout.show(mainPanel, "ENTRY"); } });
        JButton btnExit = createBigButton("💳 PAY & LEAVE", "Pay parking fee & Exit", new Color(255, 100, 100));
        btnExit.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { cardLayout.show(mainPanel, "EXIT"); } });
        btnPanel.add(btnEntry); btnPanel.add(btnExit);
        gbc.gridy = 2; gbc.ipady = 150; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(btnPanel, gbc);
        JButton btnAdmin = new JButton("Staff Login");
        btnAdmin.setFocusPainted(false); btnAdmin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnAdmin.setBackground(new Color(135, 206, 250)); btnAdmin.setForeground(Color.BLACK);
        btnAdmin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdmin.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { cardLayout.show(mainPanel, "LOGIN"); } });
        gbc.gridy = 3; gbc.ipady = 0; gbc.anchor = GridBagConstraints.SOUTHEAST;
        panel.add(btnAdmin, gbc);
        return panel;
    }

    private JButton createBigButton(String title, String subtitle, Color bgColor) {
        JButton btn = new JButton("<html><center><h1 style='color:black; margin-bottom:10px; font-size:24px'>"+title+"</h1><p style='color:black; font-size:16px'>"+subtitle+"</p></center></html>");
        btn.setBackground(bgColor); btn.setForeground(Color.BLACK); btn.setFocusPainted(false); btn.setFont(new Font("Segoe UI", Font.PLAIN, 18)); btn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 4));
        return btn;
    }

    private JPanel createLoginPanel() {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(new Color(230, 240, 250));
        JPanel card = new JPanel(new GridLayout(5, 2, 10, 10));
        card.setBackground(Color.WHITE); card.setBorder(BorderFactory.createCompoundBorder(new LineBorder(Color.GRAY, 1), new EmptyBorder(30, 50, 30, 50)));
        JLabel lblTitle = new JLabel("STAFF ACCESS", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 30)); lblTitle.setForeground(new Color(60, 60, 60));
        JTextField txtUser = new JTextField(15); txtUser.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JPasswordField txtPass = new JPasswordField(15); txtPass.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        JButton btnLogin = new JButton("LOGIN"); btnLogin.setBackground(new Color(144, 238, 144)); btnLogin.setForeground(Color.BLACK); btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14)); btnLogin.setFocusPainted(false);
        JButton btnBack = new JButton("Back to Kiosk"); btnBack.setForeground(Color.GRAY); btnBack.setContentAreaFilled(false); btnBack.setBorderPainted(false);
        btnBack.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { cardLayout.show(mainPanel, "HOME"); } });
        card.add(new JLabel("Username:")); card.add(txtUser); card.add(new JLabel("Password:")); card.add(txtPass);
        card.add(new JLabel("")); card.add(btnLogin); card.add(new JLabel("")); card.add(btnBack);
        GridBagConstraints gbc = new GridBagConstraints(); gbc.gridy = 0; gbc.insets = new Insets(0, 0, 20, 0); loginPanel.add(lblTitle, gbc);
        gbc.gridy = 1; gbc.insets = new Insets(0, 0, 0, 0); loginPanel.add(card, gbc);
        btnLogin.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent e) {
                if (system.login(txtUser.getText().trim(), new String(txtPass.getPassword()))) { loadAdminDashboard(); cardLayout.show(mainPanel, "ADMIN_DASHBOARD"); txtUser.setText(""); txtPass.setText(""); } 
                else { JOptionPane.showMessageDialog(ParkingApp.this, "Access Denied! Invalid credentials.", "Error", JOptionPane.ERROR_MESSAGE); }
            }
        });
        return loginPanel;
    }

    private void loadAdminDashboard() {
        for (Component c : mainPanel.getComponents()) {
            if (c instanceof AdminPanel) {
                ((AdminPanel) c).refreshData(); // 调用 AdminPanel 的刷新方法
                break;
            }
        }
    }

    // ==========================================
    // 🚗 3. ENTRY PANEL (Fixed: Signs OUTSIDE road, 3 Vertical Roads)
    // ==========================================
    class EntryPanel extends JPanel {
        private JComboBox<VehicleType> typeCombo;
        private JCheckBox chkHandicappedCard;
        private JTextField txtPlate;
        private JButton btnHome;
        private JTabbedPane floorTabs;
        private Map<String, ModernSpotButton> spotButtons = new HashMap<>(); 
        
        public EntryPanel() {
            setLayout(new BorderLayout());
            setBackground(new Color(240, 242, 245)); 
            
            // --- LEFT SIDEBAR (Input Controls) ---
            JPanel sidePanel = new JPanel();
            sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
            sidePanel.setBackground(Color.WHITE);
            sidePanel.setPreferredSize(new Dimension(320, 0)); 
            sidePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY), 
                new EmptyBorder(20, 20, 20, 20)
            ));
            
            btnHome = new JButton("HOME");
            btnHome.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnHome.setBackground(new Color(230, 230, 230));
            btnHome.setForeground(Color.BLACK);
            btnHome.setFocusPainted(false);
            btnHome.setMaximumSize(new Dimension(280, 40));
            btnHome.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    txtPlate.setText("");
                    typeCombo.setSelectedIndex(0);
                    chkHandicappedCard.setSelected(false);
                    updateMapState();
                    cardLayout.show(mainPanel, "HOME");
                }
            });
            
            JLabel lblTitle = new JLabel("<html><center>PARKING<br>ENTRY</center></html>");
            lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
            lblTitle.setForeground(new Color(60, 60, 60));
            lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            txtPlate = new JTextField();
            txtPlate.setFont(new Font("Segoe UI", Font.BOLD, 22));
            txtPlate.setHorizontalAlignment(JTextField.CENTER);
            txtPlate.setBorder(BorderFactory.createTitledBorder("1. Enter Plate Number"));
            txtPlate.setMaximumSize(new Dimension(280, 60));
            
            typeCombo = new JComboBox<>(VehicleType.values());
            typeCombo.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            typeCombo.setBorder(BorderFactory.createTitledBorder("2. Select Vehicle Type"));
            typeCombo.setMaximumSize(new Dimension(280, 60));
            typeCombo.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) { updateMapState(); }
            });
            
            chkHandicappedCard = new JCheckBox("Handicapped Card");
            chkHandicappedCard.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            chkHandicappedCard.setBorder(BorderFactory.createTitledBorder("3. Handicapped Card?"));
            chkHandicappedCard.setOpaque(false);
            chkHandicappedCard.setMaximumSize(new Dimension(280, 60));
            chkHandicappedCard.addActionListener(new ActionListener() {
                @Override public void actionPerformed(ActionEvent e) {
                    if (chkHandicappedCard.isSelected()) {
                        int confirm = JOptionPane.showConfirmDialog(EntryPanel.this, "Have Handicapped Card", "Verify Status", JOptionPane.YES_NO_OPTION);
                        if (confirm != JOptionPane.YES_OPTION) chkHandicappedCard.setSelected(false);
                    }
                    updateMapState();
                }
            });
            
            JPanel legendPanel = new JPanel(new GridLayout(4, 1, 5, 5));
            legendPanel.setOpaque(false);
            legendPanel.setBorder(BorderFactory.createTitledBorder("Map Legend"));
            legendPanel.setMaximumSize(new Dimension(280, 150));
            legendPanel.add(createLegendItem("Available", new Color(144, 238, 144)));
            legendPanel.add(createLegendItem("Occupied", new Color(255, 100, 100)));
            legendPanel.add(createLegendItem("Handicapped", new Color(173, 216, 230)));
            legendPanel.add(createLegendItem("Reserved", new Color(255, 255, 153)));

            sidePanel.add(btnHome); sidePanel.add(Box.createVerticalStrut(20));
            sidePanel.add(lblTitle); sidePanel.add(Box.createVerticalStrut(30));
            sidePanel.add(txtPlate); sidePanel.add(Box.createVerticalStrut(15));
            sidePanel.add(typeCombo); sidePanel.add(Box.createVerticalStrut(15));
            sidePanel.add(chkHandicappedCard); sidePanel.add(Box.createVerticalStrut(30));
            sidePanel.add(legendPanel); sidePanel.add(Box.createVerticalGlue());
            add(sidePanel, BorderLayout.WEST);
            
            floorTabs = new JTabbedPane();
            floorTabs.setFont(new Font("Segoe UI", Font.BOLD, 16));
            floorTabs.setBackground(Color.WHITE);
            add(floorTabs, BorderLayout.CENTER);
            
            initializeMap();
        }
        
        private JPanel createLegendItem(String text, Color color) {
            JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            p.setOpaque(false);
            JPanel box = new JPanel(); box.setPreferredSize(new Dimension(20, 20)); box.setBackground(color);
            box.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); 
            p.add(box); p.add(new JLabel(text)); 
            return p;
        }
        
        private void initializeMap() {
            List<ParkingSpot> allSpots = system.getAllSpots();
            floorTabs.removeAll(); 
            spotButtons.clear();
            
            for (int f = 1; f <= 5; f++) {
                // Use GridBagLayout for flexible row heights
                JPanel floorContainer = new JPanel(new GridBagLayout());
                floorContainer.setBackground(new Color(240, 242, 245)); 
                floorContainer.setBorder(new EmptyBorder(10, 20, 10, 20));

                GridBagConstraints gbc = new GridBagConstraints();
                gbc.gridx = 0; 
                gbc.fill = GridBagConstraints.HORIZONTAL; 
                gbc.weightx = 1.0;
                
                final int cf = f;
                boolean isFloor1 = (f == 1); 
                int gridY = 0;

                // 🌟 FLOOR 1: Header (Entry/Exit) - OUTSIDE the road
                if (isFloor1) {
                    gbc.gridy = gridY++; 
                    gbc.weighty = 0.0;
                    floorContainer.add(createHeaderPanel(), gbc);
                }

                // 1. TOP ROAD
                gbc.gridy = gridY++; 
                gbc.weighty = 0.0; 
                floorContainer.add(createHorizontalRoad(), gbc);
                
                // 2. Block (Row 1-2)
                gbc.gridy = gridY++; 
                gbc.weighty = 1.0; 
                gbc.fill = GridBagConstraints.BOTH;
                floorContainer.add(createTwoRowBlock(cf, 1, 2, allSpots), gbc);
                
                // 3. Middle Road
                gbc.gridy = gridY++; 
                gbc.weighty = 0.0; 
                gbc.fill = GridBagConstraints.HORIZONTAL;
                floorContainer.add(createHorizontalRoad(), gbc);
                
                // 4. Block (Row 3-4)
                gbc.gridy = gridY++; 
                gbc.weighty = 1.0; 
                gbc.fill = GridBagConstraints.BOTH;
                floorContainer.add(createTwoRowBlock(cf, 3, 4, allSpots), gbc);
                
                // 5. Middle Road
                gbc.gridy = gridY++; 
                gbc.weighty = 0.0; 
                gbc.fill = GridBagConstraints.HORIZONTAL;
                floorContainer.add(createHorizontalRoad(), gbc);
                
                // 6. Block (Row 5-6)
                gbc.gridy = gridY++; 
                gbc.weighty = 1.0; 
                gbc.fill = GridBagConstraints.BOTH;
                floorContainer.add(createTwoRowBlock(cf, 5, 6, allSpots), gbc);
                
                // 7. BOTTOM ROAD
                gbc.gridy = gridY++; 
                gbc.weighty = 0.0; 
                gbc.fill = GridBagConstraints.HORIZONTAL;
                floorContainer.add(createHorizontalRoad(), gbc);
                
                // 🌟 FLOOR 1: Footer (Faculty) - OUTSIDE the road
                if (isFloor1) {
                    gbc.gridy = gridY++; 
                    gbc.weighty = 0.0;
                    floorContainer.add(createFooterPanel(), gbc);
                }

                floorTabs.addTab("  Floor " + f + "  ", floorContainer);
            }
            updateMapState();
        }

        // 🌟 Header Panel: Entry (Left) and Exit (Right)
        private JPanel createHeaderPanel() {
            JPanel header = new JPanel(new BorderLayout());
            header.setOpaque(false);
            header.setBorder(new EmptyBorder(0, 5, 5, 5)); // Spacing

            JLabel lblEntry = new JLabel("  ⬇ ENTRY  ", SwingConstants.CENTER);
            lblEntry.setOpaque(true);
            lblEntry.setBackground(new Color(40, 160, 60)); // Green
            lblEntry.setForeground(Color.WHITE);
            lblEntry.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lblEntry.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

            JLabel lblExit = new JLabel("  EXIT ➡  ", SwingConstants.CENTER);
            lblExit.setOpaque(true);
            lblExit.setBackground(new Color(200, 50, 50)); // Red
            lblExit.setForeground(Color.WHITE);
            lblExit.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lblExit.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

            JPanel leftP = new JPanel(new FlowLayout(FlowLayout.LEFT)); 
            leftP.setOpaque(false); 
            leftP.add(lblEntry);
            
            JPanel rightP = new JPanel(new FlowLayout(FlowLayout.RIGHT)); 
            rightP.setOpaque(false); 
            rightP.add(lblExit);

            header.add(leftP, BorderLayout.WEST);
            header.add(rightP, BorderLayout.EAST);
            return header;
        }

        // 🌟 Footer Panel: Faculty Entrance
        private JPanel createFooterPanel() {
            JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER));
            footer.setOpaque(false);
            footer.setBorder(new EmptyBorder(5, 0, 10, 0)); // Spacing

            JLabel lblFac = new JLabel("   ⬇ TO FACULTY ENTRANCE ⬇   ", SwingConstants.CENTER);
            lblFac.setOpaque(true);
            lblFac.setBackground(new Color(0, 100, 200)); // Blue
            lblFac.setForeground(Color.WHITE);
            lblFac.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lblFac.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            
            footer.add(lblFac);
            return footer;
        }

        // 🛣️ Horizontal Road (Just the road)
        private JPanel createHorizontalRoad() {
            JPanel road = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    // Road Background
                    g2.setColor(new Color(220, 220, 220)); 
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    
                    // Dashed Line (Center)
                    g2.setColor(Color.WHITE);
                    Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{15}, 0);
                    g2.setStroke(dashed);
                    g2.drawLine(20, getHeight()/2, getWidth()-20, getHeight()/2);
                    
                    // Arrows
                    g2.setColor(new Color(180, 180, 180)); 
                    g2.setFont(new Font("Arial", Font.BOLD, 20));
                    g2.drawString("➤", 20, getHeight()/2 + 7);
                    g2.drawString("➤", getWidth()-30, getHeight()/2 + 7);
                }
            };
            road.setMinimumSize(new Dimension(0, 40));
            road.setPreferredSize(new Dimension(1000, 40));
            road.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
            return road;
        }

        // 🅿️ Block with 3 Vertical Roads (Left, Middle, Right)
        private JPanel createTwoRowBlock(int floor, int rowTop, int rowBot, List<ParkingSpot> allSpots) {
            JPanel block = new JPanel(new GridBagLayout()); 
            block.setOpaque(false);
            block.setBorder(new EmptyBorder(5, 0, 5, 0));
            
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.BOTH; 
            gbc.weighty = 1.0;

            for (int rIndex = 0; rIndex < 2; rIndex++) {
                int targetRow = (rIndex == 0) ? rowTop : rowBot;
                
                // 1. Add Left Vertical Road (Col 0)
                if (rIndex == 0) {
                    gbc.gridx = 0; gbc.gridy = 0; gbc.gridheight = 2; gbc.weightx = 0.05;
                    block.add(createVerticalRoad(), gbc);
                }

                // Spots 1-8
                for (int s = 1; s <= 8; s++) {
                    int colIndex;
                    if (s <= 4) colIndex = s;      // 1,2,3,4
                    else colIndex = s + 1;         // 6,7,8,9 (Skip 5)

                    gbc.gridx = colIndex; 
                    gbc.gridy = rIndex; 
                    gbc.gridheight = 1; 
                    gbc.weightx = 1.0;
                    gbc.insets = new Insets(2, 2, 2, 2);
                    
                    addSpotToPanel(block, floor, targetRow, s, allSpots, gbc);

                    // 2. Add Middle Vertical Road (After Spot 4, Col 5)
                    if (s == 4 && rIndex == 0) {
                        gbc.gridx = 5; gbc.gridy = 0; gbc.gridheight = 2; gbc.weightx = 0.05; 
                        gbc.insets = new Insets(0, 5, 0, 5);
                        block.add(createVerticalRoad(), gbc);
                    }
                }
                
                // 3. Add Right Vertical Road (Col 10)
                if (rIndex == 0) {
                    gbc.gridx = 10; gbc.gridy = 0; gbc.gridheight = 2; gbc.weightx = 0.05;
                    block.add(createVerticalRoad(), gbc);
                }
            }
            block.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260)); 
            return block;
        }

        private JPanel createVerticalRoad() {
            JPanel vRoad = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setColor(new Color(220, 220, 220)); 
                    g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.setColor(Color.WHITE);
                    Stroke dashed = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{10}, 0);
                    g2.setStroke(dashed); 
                    g2.drawLine(getWidth()/2, 5, getWidth()/2, getHeight()-5);
                }
            };
            vRoad.setMinimumSize(new Dimension(25, 0));
            vRoad.setPreferredSize(new Dimension(25, 0));
            return vRoad;
        }

        private void addSpotToPanel(JPanel panel, int floor, int row, int spot, List<ParkingSpot> allSpots, GridBagConstraints gbc) {
            String id = "F" + floor + "-R" + row + "-S" + spot;
            ParkingSpot pSpot = null;
            for (ParkingSpot s : allSpots) { if (s.getId().equals(id)) { pSpot = s; break; } }

            if (pSpot != null) {
                ModernSpotButton btn = new ModernSpotButton(pSpot);
                final ParkingSpot finalSpot = pSpot;
                btn.addActionListener(e -> showSpotDialog(finalSpot));
                spotButtons.put(id, btn);
                panel.add(btn, gbc);
            } else {
                panel.add(new JLabel(""), gbc);
            }
        }
        
        // 🌟🌟🌟 RESTORED ORIGINAL DIALOG CONTENT 🌟🌟🌟
        private void showSpotDialog(ParkingSpot spot) {
            String plate = txtPlate.getText().trim();
            if (plate.isEmpty()) { JOptionPane.showMessageDialog(this, "Please Enter Plate Number First!", "Missing Info", JOptionPane.WARNING_MESSAGE); txtPlate.requestFocus(); return; }
            
            // 1. Determine "Suitable For" Text
            String suitableFor = "";
            switch (spot.getType()) { 
                case COMPACT: suitableFor = "Motorcycles, Compact Cars"; break; 
                case REGULAR: suitableFor = "All Cars, SUVs, Trucks"; break; 
                case HANDICAPPED: suitableFor = "Handicapped Card Holders Only"; break; 
                case RESERVED: suitableFor = "VIP / Reserved Only"; break; 
            }

            // 2. Calculate Rate
            double displayRate = spot.getHourlyRate();
            if (spot.getType() == SpotType.HANDICAPPED) displayRate = 2.0; 
            if (chkHandicappedCard.isSelected()) displayRate -= 2.0;
            if (displayRate < 0) displayRate = 0.0;
            String rateString = (displayRate == 0) ? "RM 0.00 / Hour (Free)" : String.format("RM %.2f / hour", displayRate);
            
            // 3. Get Current Fine Scheme for Warning
            // 3. Get Current Fine Scheme for Warning
            String fineInfo = "Fixed Fine\nRM 50.00 (Flat Rate)"; // Default
            FineScheme scheme = system.getFineScheme();

            if (scheme == FineScheme.PROGRESSIVE) {
                // 🌟 重点：在这里使用 <br> 标签手动换行，确保排版和图片 2 一致
                fineInfo = "Progressive Fine<br>" +
                           "First 24 hours: RM 50<br>" +
                           "Hours 24-48: Additional RM 100<br>" +
                           "Hours 48-72: Additional RM 150<br>" +
                           "Above 72 hours: Additional RM 200";
            } else if (scheme == FineScheme.HOURLY) {
                fineInfo = "Hourly Fine Scheme<br>"+
                           "RM 20.00 / hour";
            }
            // 4. Construct HTML Message matching image_0ca423.png
            String msg = "<html><body style='width: 350px; font-family: Segoe UI;'>" +
                     "<h2 style='color: #0078D7; margin-bottom: 5px;'>🅿 Spot Information</h2>" +
                     "<hr>" +
                     "<p style='margin-top:5px;'><b>Spot ID:</b> " + spot.getId() + "</p>" +
                     "<p><b>Type:</b> " + spot.getType() + "</p>" +
                     "<p><b>Rate:</b> " + rateString + "</p>" +
                     "<p><b>Suitable For:</b> " + suitableFor + "</p><br>" +
                     
                     // Yellow Warning Box
                     "<div style='background-color: #FFFFCC; border: 1px solid #CCCC99; padding: 8px;'>" +
                     "<b>⚠ Overstay Fine (>24h):</b><br>" +
                     "<span style='color: red;'>" + fineInfo + "</span>" +
                     "</div><br>" +
                     
                     // Assign Plate Footer
                     "<p><b>Assign to Plate:</b> <span style='color: red; font-weight: bold; font-size: 14px;'>" + plate + "</span></p>" +
                     "</body></html>";
            
            int choice = JOptionPane.showOptionDialog(this, msg, "Confirm Parking", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[]{"PARK HERE", "CANCEL"}, "PARK HERE");
            if (choice == 0) performParking(spot);
        }

        // 👇 替换掉原有的 performParking 方法
        private void performParking(ParkingSpot spot) {
            String plate = txtPlate.getText().trim();
            if (system.getActiveTicket(plate) != null) { 
                JOptionPane.showMessageDialog(this, "Vehicle [" + plate + "] is Already Parked!", "Error", JOptionPane.ERROR_MESSAGE); 
                return; 
            }
            Vehicle v = new Vehicle(plate, (VehicleType)typeCombo.getSelectedItem(), chkHandicappedCard.isSelected());
            Ticket t = system.parkVehicle(v, spot.getId());
            if (t != null) {
                // 🌟🌟🌟 新增：调用详细的票据打印弹窗 🌟🌟🌟
                printParkingTicket(t, spot);
                
                txtPlate.setText(""); 
                updateMapState();
            }
        }
        
        // 👇 在 performParking 下方添加这个新方法
        private void printParkingTicket(Ticket t, ParkingSpot s) {
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            String entryTimeStr = t.getEntryTime().format(fmt);
            
            String fineRule = "Option A: Fixed Fine";
            String fineDetail = "RM 50.00 flat rate for overstay (>24 hours).";
            FineScheme scheme = t.getLockedScheme();
            if (scheme == FineScheme.PROGRESSIVE) {
                fineRule = "Option B: Progressive Fine";
                fineDetail = "Increases from RM 50 to RM 200 based on duration.";
            } else if (scheme == FineScheme.HOURLY) {
                fineRule = "Option C: Hourly Fine";
                fineDetail = "RM 20.00 per hour overstay.";
            }
            
            StringBuilder sb = new StringBuilder();
            sb.append("\n  |=========================================================|\n");
            sb.append("  |                       Parking Ticket                    |\n");
            sb.append("  |                UNIVERSITY PARKING SYSTEM                |\n");
            sb.append("  |=========================================================|\n");
            sb.append(String.format("  %-20s : %s\n", "Ticket ID", t.toString()));
            sb.append(String.format("  %-20s : %s\n", "Parking Spot", t.getSpotId()));
            sb.append(String.format("  %-20s : %s\n", "Parking Spot Type", s.getType()));
            sb.append(String.format("  %-20s : %s\n", "Vehicle Type", t.getVehicleType()));
            sb.append(String.format("  %-20s : %s\n", "Plate Number", t.getPlateNumber()));
            sb.append(String.format("  %-20s : %s\n", "Entry Date & Time", entryTimeStr));
            sb.append("  -----------------------------------------------------------\n");
            sb.append("  Fine Type Calculation:\n");
            sb.append("  " + fineRule + "\n");
            sb.append("  " + fineDetail + "\n\n");
            sb.append("  Notes: Please keep parking ticket safe to avoid fines\n");
            sb.append("         for lost ticket (RM 50)\n");
            sb.append("  ===========================================================\n");
            sb.append("          Please contact admin if there is any enquiries.\n");
            sb.append("                           Thank You\n");
            sb.append("  -----------------------------------------------------------\n");

            JTextArea area = new JTextArea(sb.toString());
            area.setFont(new Font("Monospaced", Font.BOLD, 12));
            area.setEditable(false);
            JScrollPane scroll = new JScrollPane(area);
            scroll.setPreferredSize(new Dimension(480, 450)); // 调整了尺寸以适应内容
            
            JOptionPane.showMessageDialog(this, scroll, "Ticket Generated Successfully!", JOptionPane.PLAIN_MESSAGE);
        }
        
        private void updateMapState() {
            VehicleType type = (VehicleType) typeCombo.getSelectedItem();
            for (ModernSpotButton btn : spotButtons.values()) {
                btn.updateState(type);
            }
            repaint();
        }
    }

    class ExitPanel extends JPanel {
        private JTextField txtSearchPlate;
        private JTextArea txtReceipt; 
        private JTable tableFines;
        private DefaultTableModel finesModel;
        private JButton btnSearch, btnPay, btnHome;
        private double currentFee = 0.0;
        private double currentOverstayFine = 0.0;
        private double selectedHistoricalFineTotal = 0.0;
        private double totalToPay = 0.0;
        private Ticket currentTicket;
        private LocalDateTime exitTime;
        private String currentPlate = "";
        
        public ExitPanel() {
            setLayout(new BorderLayout(10, 10));
            setBorder(new EmptyBorder(10, 20, 10, 20));
            JPanel topPanel = new JPanel(new BorderLayout());
            btnHome = new JButton("HOME");
            btnHome.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnHome.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { cardLayout.show(mainPanel, "HOME"); } });
            JLabel lblTitle = new JLabel("STEP 2: PAYMENT & EXIT", SwingConstants.CENTER);
            lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
            JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
            txtSearchPlate = new JTextField(15);
            txtSearchPlate.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            btnSearch = new JButton("Search Vehicle No");
            btnSearch.setFont(new Font("Segoe UI", Font.BOLD, 16));
            btnSearch.setBackground(new Color(100, 149, 237));
            btnSearch.setForeground(Color.BLACK); 
            searchBar.add(new JLabel("Enter Vehicle No: ")); searchBar.add(txtSearchPlate); searchBar.add(btnSearch);
            topPanel.add(btnHome, BorderLayout.WEST); topPanel.add(lblTitle, BorderLayout.NORTH); topPanel.add(searchBar, BorderLayout.CENTER);
            add(topPanel, BorderLayout.NORTH);

            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            splitPane.setResizeWeight(0.4); 
            JPanel leftPanel = new JPanel(new BorderLayout());
            leftPanel.setBorder(new TitledBorder("Outstanding Fines (Select to Pay)"));
            String[] cols = {"Pay?", "Fine Type", "Date", "Amount (RM)", "ID"};
            finesModel = new DefaultTableModel(cols, 0) {
                @Override public Class<?> getColumnClass(int columnIndex) { return columnIndex == 0 ? Boolean.class : String.class; }
                @Override public boolean isCellEditable(int row, int column) { return column == 0; }
            };
            tableFines = new JTable(finesModel);
            tableFines.setRowHeight(25);
            tableFines.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            tableFines.getColumnModel().getColumn(4).setMinWidth(0); tableFines.getColumnModel().getColumn(4).setMaxWidth(0); tableFines.getColumnModel().getColumn(4).setWidth(0);
            finesModel.addTableModelListener(new javax.swing.event.TableModelListener() { @Override public void tableChanged(javax.swing.event.TableModelEvent e) { updateBillPreview(); } });
            leftPanel.add(new JScrollPane(tableFines), BorderLayout.CENTER);
            
            JPanel rightPanel = new JPanel(new BorderLayout());
            rightPanel.setBorder(new TitledBorder("Bill Preview"));
            txtReceipt = new JTextArea();
            txtReceipt.setFont(new Font("Monospaced", Font.BOLD, 12)); 
            txtReceipt.setEditable(false);
            JScrollPane scrollReceipt = new JScrollPane(txtReceipt);
            scrollReceipt.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            rightPanel.add(scrollReceipt, BorderLayout.CENTER);
            splitPane.setLeftComponent(leftPanel); splitPane.setRightComponent(rightPanel);
            add(splitPane, BorderLayout.CENTER);

            btnPay = new JButton("CONFIRM PAYMENT");
            btnPay.setFont(new Font("Segoe UI", Font.BOLD, 20));
            btnPay.setBackground(new Color(60, 179, 113));
            btnPay.setForeground(Color.BLACK); 
            btnPay.setEnabled(false);
            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            bottomPanel.add(btnPay);
            add(bottomPanel, BorderLayout.SOUTH);

            btnSearch.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { loadCarDetails(); } });
            btnPay.addActionListener(new ActionListener() { @Override public void actionPerformed(ActionEvent e) { performPayment(); } });
        }

        private void loadCarDetails() {
            String plate = txtSearchPlate.getText().trim();
            if (plate.isEmpty()) return;
            currentTicket = system.getActiveTicket(plate);
            if (currentTicket == null) { JOptionPane.showMessageDialog(this, "Vehicle Not Found!", "Error", JOptionPane.ERROR_MESSAGE); resetForm(); return; }
            currentPlate = plate;
            exitTime = LocalDateTime.now();
            currentFee = system.calculateFee(currentTicket, exitTime);
            currentOverstayFine = system.calculateFine(currentTicket, exitTime);
            finesModel.setRowCount(0);
            if (currentOverstayFine > 0) { finesModel.addRow(new Object[]{ true, "Current Overstay Fine", "Today", String.format("%.2f", currentOverstayFine), "NEW" }); }
            List<String[]> fines = system.getFinesForPlateWithId(plate);
            for (String[] f : fines) { finesModel.addRow(new Object[]{ true, f[2], f[3], f[1], f[0] }); }
            updateBillPreview();
            btnPay.setEnabled(true);
        }

        // 在 ExitPanel 类中替换 updateBillPreview 方法
        private void updateBillPreview() {
            if (currentTicket == null) return;
            
            selectedHistoricalFineTotal = 0.0;
            List<String[]> selectedFinesDetails = new ArrayList<>();
            
            // 计算勾选的罚款
            for (int i = 0; i < finesModel.getRowCount(); i++) {
                Boolean isChecked = (Boolean) finesModel.getValueAt(i, 0);
                if (isChecked) {
                    String type = (String) finesModel.getValueAt(i, 1);
                    String date = (String) finesModel.getValueAt(i, 2);
                    String amtStr = (String) finesModel.getValueAt(i, 3);
                    selectedHistoricalFineTotal += Double.parseDouble(amtStr);
                    selectedFinesDetails.add(new String[]{date, type, amtStr});
                }
            }
            
            totalToPay = currentFee + selectedHistoricalFineTotal;
            
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            Duration d = Duration.between(currentTicket.getEntryTime(), exitTime);
            String durationStr = formatDuration(d);
            ParkingSpot spot = system.getSpotById(currentTicket.getSpotId());
            
            StringBuilder sb = new StringBuilder();
            sb.append("=========================================================\n");
            sb.append("                      BILL PREVIEW                       \n");
            sb.append("               UNIVERSITY PARKING SYSTEM                 \n");
            sb.append("=========================================================\n\n");
            
            // 🌟 1. 添加 Ticket ID 在 Plate Number 上面
            sb.append(String.format("%-25s : %s\n", "Ticket ID", currentTicket.getTicketId())); 
            sb.append(String.format("%-25s : %s\n", "Plate Number", currentPlate));
            
            sb.append(String.format("%-25s : %s\n", "Parking Spot Type", (spot != null ? spot.getType() : "-")));
            
            // 🌟 2. 添加 Vehicle Type 在 Spot Type 下面
            sb.append(String.format("%-25s : %s\n", "Vehicle Type", currentTicket.getVehicleType()));

            sb.append(String.format("%-25s : %s\n", "Entry Date & Time", currentTicket.getEntryTime().format(fmt)));
            sb.append(String.format("%-25s : %s\n", "Payment Date & Time", exitTime.format(fmt)));

            // 🌟 3. 添加 Parking Rate 在 Duration 上面
            sb.append(String.format("%-25s : RM %.2f /hour\n", "Parking Rate", currentTicket.getParkingRate()));
            sb.append(String.format("%-25s : %s\n\n", "Parking Duration", durationStr));
            
            // 🌟 4. 对齐 Parking Fees (使用 %25s 确保靠右对齐)
            sb.append(String.format("%-25s : %25s\n", "Parking Fees", "RM " + String.format("%6.2f", currentFee)));
            
            if (!selectedFinesDetails.isEmpty()) {
                sb.append("\n- - - - - OUTSTANDING FINES - - - - -\n");
                for (String[] f : selectedFinesDetails) {
                    sb.append("[" + f[0] + "]\n");
                    String remarkLabel = f[1];
                    if(remarkLabel.length() > 25) remarkLabel = remarkLabel.substring(0, 24) + "...";
                    // 对齐 Fine Amount
                    sb.append(String.format("%-25s : %5s\n", remarkLabel, "RM " + String.format("%6.2f", Double.parseDouble(f[2]))));
                }
                sb.append("---------------------------------------------------------\n");
                sb.append(String.format("%-25s : %25s\n", "Total Fines/Arrears", "RM " + String.format("%6.2f", selectedHistoricalFineTotal)));
            }
            
            sb.append("---------------------------------------------------------\n");
            // 🌟 4. 对齐 Total To Pay (和上面的 Parking Fees 保持一致)
            sb.append(String.format("%-25s : %25s\n", "TOTAL TO PAY", "RM " + String.format("%6.2f", totalToPay)));
            sb.append("=========================================================\n");
            sb.append("Please contact admin if there is any enquiries.\n");
            sb.append("                        Thank You\n");
            sb.append("---------------------------------------------------------");
            
            txtReceipt.setText(sb.toString());
            txtReceipt.setCaretPosition(0); 
        }

        private void performPayment() {
    String[] methods = {"Cash", "Credit Card", "E-Wallet"};
    int choice = JOptionPane.showOptionDialog(this, 
        "Total to Pay: RM " + String.format("%.2f", totalToPay) + "\nSelect Payment Method:", 
        "Payment", 0, JOptionPane.PLAIN_MESSAGE, null, methods, methods[0]);

    if (choice != -1) {
        String method = methods[choice].toUpperCase();
        double customerPaid = totalToPay; // 默认支付金额
        double balance = 0.0;            // 默认找零

        // 🌟 核心修改：如果是现金支付，执行找零逻辑
        if (method.equals("CASH")) {
            boolean valid = false;
            while (!valid) {
                String input = JOptionPane.showInputDialog(this, 
                    "Total Amount: RM " + String.format("%.2f", totalToPay) + "\nEnter Cash Received (RM):", 
                    "Cash Payment", JOptionPane.QUESTION_MESSAGE);
                
                if (input == null) return; // 用户取消

                try {
                    customerPaid = Double.parseDouble(input);
                    if (customerPaid < totalToPay) {
                        JOptionPane.showMessageDialog(this, "Insufficient cash! Minimum RM " + String.format("%.2f", totalToPay), "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        balance = customerPaid - totalToPay;
                        valid = true;
                        // 提示找零
                        JOptionPane.showMessageDialog(this, "Payment Received: RM " + String.format("%.2f", customerPaid) + "\nChange to Return: RM " + String.format("%.2f", balance));
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid number format!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        // 准备数据库所需数据
        List<String[]> paidFinesDetails = new ArrayList<>();
        List<Integer> paidFineIds = new ArrayList<>();
        boolean isOverstayPaid = false;

        for (int i = 0; i < finesModel.getRowCount(); i++) {
            if ((Boolean) finesModel.getValueAt(i, 0)) {
                String type = (String) finesModel.getValueAt(i, 1);
                String date = (String) finesModel.getValueAt(i, 2);
                String amt = (String) finesModel.getValueAt(i, 3);
                String idStr = (String) finesModel.getValueAt(i, 4);
                paidFinesDetails.add(new String[]{date, type, amt});
                if (idStr.equals("NEW")) { isOverstayPaid = true; } 
                else { paidFineIds.add(Integer.parseInt(idStr)); }
            }
        }

        ParkingSpot spot = system.getSpotById(currentTicket.getSpotId());
        String invoiceId = "REC-" + currentPlate + "-" + System.currentTimeMillis()/1000;
        String durationStr = formatDuration(Duration.between(currentTicket.getEntryTime(), exitTime));
        String entryTimeStr = currentTicket.getEntryTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // 🌟 存入数据库：请确保你的 system.processExit 方法接收最后两个参数 (customerPaid, balance)
        system.processExit(currentPlate, totalToPay, method, invoiceId, entryTimeStr, durationStr, 
                           currentFee, currentOverstayFine, spot.getType().toString(), 
                           paidFineIds, isOverstayPaid, customerPaid, balance); 

        JOptionPane.showMessageDialog(this, "Payment Successful! Gate Opening...", "Success", JOptionPane.INFORMATION_MESSAGE);
        
        // 🌟 传递找零数据给收据方法
        printFinalReceipt(invoiceId, method, durationStr, paidFinesDetails, customerPaid, balance);
        resetForm();
    }
}
        
        // 🌟 更新参数签名，加入 paid 和 bal
        // 在 ExitPanel 类中替换 printFinalReceipt 方法
        private void printFinalReceipt(String invId, String method, String dur, List<String[]> paidDetails, double paid, double bal) {
            StringBuilder sb = new StringBuilder();
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            ParkingSpot spot = system.getSpotById(currentTicket.getSpotId());

            sb.append("=========================================================\n");
            sb.append("                         INVOICE                         \n");
            sb.append("                UNIVERSITY PARKING SYSTEM                \n");
            sb.append("=========================================================\n");
            sb.append("Invoice Date: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).append("\n\n");
            
            // 🌟 1. Invoice ID 换成 Ticket ID
            sb.append(String.format("%-25s : %s\n", "TICKET ID", currentTicket.getTicketId()));
            
            // 🌟 2. Ticket ID 下面添加 Parking Spot
            sb.append(String.format("%-25s : %s\n", "Parking Spot", currentTicket.getSpotId()));
            
            sb.append(String.format("%-25s : %s\n", "Plate Number", currentPlate));
            sb.append(String.format("%-25s : %s\n", "Parking Spot Type", (spot != null ? spot.getType() : "-")));
            
            // 🌟 3. Parking Spot Type 下面添加 Vehicle Type
            sb.append(String.format("%-25s : %s\n", "Vehicle Type", currentTicket.getVehicleType()));

            sb.append(String.format("%-25s : %s\n", "Entry Date & Time", currentTicket.getEntryTime().format(fmt)));
            sb.append(String.format("%-25s : %s\n", "Payment Date & Time", exitTime.format(fmt)));
            
            // 🌟 4. Parking Duration 上面添加 Parking Rate
            sb.append(String.format("%-25s : RM %.2f /hour\n", "Parking Rate", currentTicket.getParkingRate()));
            sb.append(String.format("%-25s : %s\n\n", "Parking Duration", dur));

            // 费用部分 (保持对齐)
            sb.append(String.format("%-25s : %25s\n", "Parking Fees", "RM " + String.format("%6.2f", currentFee)));
            
            double totalFines = 0.0;
            if (!paidDetails.isEmpty()) {
                sb.append("\n- - - - - PAID ARREARS DETAILS - - - - -\n");
                for (String[] f : paidDetails) {
                    sb.append("[" + f[0] + "]\n");
                    String remarkLabel = f[1];
                    if(remarkLabel.length() > 25) remarkLabel = remarkLabel.substring(0, 24) + "...";
                    // 对齐 Fine Amount
                    sb.append(String.format("%-25s : %5s\n", remarkLabel, "RM " + String.format("%6.2f", Double.parseDouble(f[2]))));
                    totalFines += Double.parseDouble(f[2]);
                }
                sb.append("---------------------------------------------------------\n");
                sb.append(String.format("%-25s : %25s\n", "Total Fines Paid", "RM " + String.format("%6.2f", totalFines)));
            }
            
            sb.append("---------------------------------------------------------\n");
            sb.append(String.format("%-25s : %25s\n", "TOTAL TO PAY", "RM " + String.format("%6.2f", totalToPay)));
            sb.append(String.format("%-25s : %25s\n", "Payment Type", method));
            sb.append("---------------------------------------------------------\n");
            sb.append(String.format("%-25s : %25s\n", "CUSTOMER PAID", "RM " + String.format("%6.2f", paid)));
            sb.append(String.format("%-25s : %25s\n", "CHANGE RETURNED", "RM " + String.format("%6.2f", bal)));
            sb.append("=========================================================\n");
            sb.append("          Thank you & Have a safe trip!           \n");
            // 如果你还需要保留 Transaction Ref (Invoice ID) 作为参考，可以放在这里，不需要则删除下面这行
            sb.append(String.format("Ref: %s\n", invId)); 
            sb.append("=========================================================\n");

            JTextArea r = new JTextArea(sb.toString());
            r.setFont(new Font("Monospaced", Font.BOLD, 12));
            r.setEditable(false);
            JScrollPane scroll = new JScrollPane(r);
            scroll.setPreferredSize(new Dimension(450, 600));
            JOptionPane.showMessageDialog(this, scroll, "Receipt", JOptionPane.PLAIN_MESSAGE);
        }
        private void resetForm() {
            txtSearchPlate.setText("");
            finesModel.setRowCount(0);
            txtReceipt.setText("");
            btnPay.setEnabled(false);
            currentTicket = null;
        }
        
        private String formatDuration(Duration d) {
            long days = d.toDays();
            long hours = d.toHours() % 24;
            long minutes = d.toMinutes() % 60;
            if (days > 0) return String.format("%dd %02dh %02dm", days, hours, minutes);
            return String.format("%02dh %02dm", hours, minutes);
        }
    }

    // 🌟🌟🌟 请复制此代码替换原本的 class AdminPanel 🌟🌟🌟
// 🌟🌟🌟 新的 AdminPanel (侧边栏 + 可视化地图) 🌟🌟🌟
// 🌟🌟🌟 NEW ADMIN PANEL (System Settings UI Updated) 🌟🌟🌟
    // 🌟🌟🌟 修正后的 AdminPanel (解决了方法嵌套错误) 🌟🌟🌟
    class AdminPanel extends JPanel {
        private JPanel contentArea;
        private CardLayout contentLayout;
        private SpotMonitorPanel spotMonitorPanel;
        private DefaultTableModel fineModel; 
        private Runnable finesRefresher; 
        

        // 🌟 将此方法复制到 AdminPanel.java 中
        // 🌟 复制到 SpotMonitorPanel 类内部 🌟
        private JPanel createStatsPanel() {
            JPanel panel = new JPanel(new GridLayout(1, 2, 20, 0)); 
            panel.setBorder(new javax.swing.border.EmptyBorder(10, 20, 10, 20));
            panel.setBackground(new Color(245, 245, 245));

            // --- 1. Occupancy Rate ---
            JPanel pnlOcc = new JPanel(new BorderLayout());
            pnlOcc.setBackground(Color.WHITE);
            pnlOcc.setBorder(BorderFactory.createCompoundBorder(
                new javax.swing.border.LineBorder(new Color(200, 200, 200), 1),
                new javax.swing.border.EmptyBorder(10, 15, 10, 15)
            ));
            
            // 计算占用率 (修复红线版)
            java.util.List<ParkingSpot> allSpots = ParkingLotSystem.getInstance().getAllSpots();
            int totalSpots = allSpots.size();
            long occupiedCount = allSpots.stream().filter(s -> s.getStatus() == SpotStatus.OCCUPIED).count(); // ✅ 使用 Status 判断
            int percentage = totalSpots > 0 ? (int)((occupiedCount * 100) / totalSpots) : 0;

            JLabel lblOccTitle = new JLabel("OCCUPANCY RATE");
            lblOccTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lblOccTitle.setForeground(Color.GRAY);
            
            JLabel lblOccValue = new JLabel(percentage + "% (" + occupiedCount + "/" + totalSpots + ")");
            lblOccValue.setFont(new Font("Segoe UI", Font.BOLD, 24));
            lblOccValue.setForeground(percentage > 85 ? Color.RED : new Color(34, 139, 34)); 
            
            JProgressBar progOcc = new JProgressBar(0, 100);
            progOcc.setValue(percentage);
            progOcc.setPreferredSize(new Dimension(100, 5));
            progOcc.setForeground(percentage > 85 ? Color.RED : new Color(34, 139, 34));
            progOcc.setBorderPainted(false);

            pnlOcc.add(lblOccTitle, BorderLayout.NORTH);
            pnlOcc.add(lblOccValue, BorderLayout.CENTER);
            pnlOcc.add(progOcc, BorderLayout.SOUTH);

            // --- 2. Revenue ---
            JPanel pnlRev = new JPanel(new BorderLayout());
            pnlRev.setBackground(Color.WHITE);
            pnlRev.setBorder(BorderFactory.createCompoundBorder(
                new javax.swing.border.LineBorder(new Color(200, 200, 200), 1),
                new javax.swing.border.EmptyBorder(10, 15, 10, 15)
            ));

            // 获取收入
            double totalRev = 0.0;
            try { totalRev = com.parking.db.DatabaseHelper.getInstance().getTotalRevenue(); } catch(Exception e){}

            JLabel lblRevTitle = new JLabel("TOTAL REVENUE");
            lblRevTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lblRevTitle.setForeground(Color.GRAY);

            JLabel lblRevValue = new JLabel("RM " + String.format("%,.2f", totalRev));
            lblRevValue.setFont(new Font("Segoe UI", Font.BOLD, 24));
            lblRevValue.setForeground(new Color(0, 102, 204)); 

            pnlRev.add(lblRevTitle, BorderLayout.NORTH);
            pnlRev.add(lblRevValue, BorderLayout.CENTER);

            panel.add(pnlOcc);
            panel.add(pnlRev);
            return panel;
        }
        public AdminPanel() {
            setLayout(new BorderLayout());

            // 1. 左侧侧边栏 (Sidebar)
            add(createSidebar(), BorderLayout.WEST);

            // 2. 右侧内容区 (Content Area)
            contentLayout = new CardLayout();
            contentArea = new JPanel(contentLayout);
            contentArea.setBackground(Color.WHITE);

            // 3. 初始化各个页面
            spotMonitorPanel = new SpotMonitorPanel();
            
            contentArea.add(spotMonitorPanel, "DASHBOARD");
            contentArea.add(createUnpaidFinesPanel(), "FINES");
            contentArea.add(createTransTablePanel(), "HISTORY"); 
            contentArea.add(createSettingsPanel(), "SETTINGS");
            contentArea.add(new ReportsPanel(), "REPORTS");

            add(contentArea, BorderLayout.CENTER);
        }
        
        public void refreshData() {
            if (spotMonitorPanel != null) spotMonitorPanel.refreshAllData();
            if (finesRefresher != null) finesRefresher.run();
        }

        // --- 侧边栏 (Sidebar) ---
        private JPanel createSidebar() {
            JPanel p = new JPanel(); 
            p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
            p.setBackground(new Color(40, 44, 52)); 
            p.setPreferredSize(new Dimension(220, 0));
            p.setBorder(new EmptyBorder(20, 10, 20, 10));

            JLabel title = new JLabel("Admin Panel"); 
            title.setForeground(Color.WHITE); 
            title.setFont(new Font("Segoe UI", Font.BOLD, 24));
            title.setAlignmentX(Component.LEFT_ALIGNMENT);
            p.add(title); 
            p.add(Box.createVerticalStrut(30));

            p.add(createMenuBtn("Spot Monitor", "DASHBOARD"));
            p.add(Box.createVerticalStrut(10));
            p.add(createMenuBtn("Unpaid Fines", "FINES"));
            p.add(createMenuBtn("Receipt History", "HISTORY"));
            p.add(Box.createVerticalStrut(10));
            p.add(createMenuBtn("Settings & Rules", "SETTINGS"));
            p.add(createMenuBtn("Reports Center", "REPORTS"));
            
            p.add(Box.createVerticalGlue());
            JButton logout = new JButton("Logout"); 
            logout.setBackground(new Color(231, 76, 60)); 
            logout.setForeground(Color.BLACK);
            logout.setMaximumSize(new Dimension(200, 40));
            logout.setAlignmentX(Component.LEFT_ALIGNMENT);
            logout.addActionListener(e -> {
                ParkingLotSystem.getInstance().logout();
                CardLayout cl = (CardLayout) getParent().getLayout();
                cl.show(getParent(), "HOME");
            });
            p.add(logout);
            return p;
        }

        private JButton createMenuBtn(String text, String key) {
            JButton b = new JButton(text); 
            b.setMaximumSize(new Dimension(200, 40));
            b.setAlignmentX(Component.LEFT_ALIGNMENT); 
            b.setFocusPainted(false);
            b.setBackground(new Color(40, 44, 52)); 
            b.setForeground(Color.WHITE);
            b.setBorderPainted(false); 
            b.setHorizontalAlignment(SwingConstants.LEFT);
            b.setFont(new Font("Segoe UI", Font.PLAIN, 16)); 
            b.setCursor(new Cursor(Cursor.HAND_CURSOR));
            b.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { b.setBackground(new Color(60, 64, 72)); }
                public void mouseExited(MouseEvent e) { b.setBackground(new Color(40, 44, 52)); }
            });
            b.addActionListener(e -> contentLayout.show(contentArea, key));
            return b;
        }

        // --- NEW SETTINGS PANEL ---
        private JPanel createSettingsPanel() {
            JPanel panel = new JPanel(new BorderLayout(20, 0)); 
            panel.setBorder(new EmptyBorder(30, 40, 30, 40));
            panel.setBackground(Color.WHITE);

            JPanel leftPanel = new JPanel(new GridLayout(6, 1, 0, 15)); 
            leftPanel.setBackground(Color.WHITE);
            leftPanel.setPreferredSize(new Dimension(300, 0));
            leftPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY), 
                "Select Fine Calculation...", 
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, 
                new Font("Segoe UI", Font.PLAIN, 14)
            ));

            JRadioButton r1 = new JRadioButton("Option A: Fixed Fine");
            JRadioButton r2 = new JRadioButton("Option B: Progressive Fine");
            JRadioButton r3 = new JRadioButton("Option C: Hourly Fine");
            
            Font radioFont = new Font("Segoe UI", Font.PLAIN, 14);
            r1.setFont(radioFont); r2.setFont(radioFont); r3.setFont(radioFont);
            r1.setBackground(Color.WHITE); r2.setBackground(Color.WHITE); r3.setBackground(Color.WHITE);
            
            ButtonGroup bg = new ButtonGroup(); 
            bg.add(r1); bg.add(r2); bg.add(r3);

            JButton btnSave = new JButton("Save Configuration");
            btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnSave.setBackground(new Color(255, 255, 240)); 
            btnSave.setForeground(Color.BLACK);
            btnSave.setFocusPainted(false);
            btnSave.setBorder(BorderFactory.createLineBorder(new Color(218, 165, 32), 2)); 
            btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));

            JButton btnHistory = new JButton("View History");
            btnHistory.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            btnHistory.setBackground(Color.WHITE);
            btnHistory.setForeground(Color.BLACK);
            btnHistory.setFocusPainted(false);
            btnHistory.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
            btnHistory.setCursor(new Cursor(Cursor.HAND_CURSOR));

            leftPanel.add(r1); leftPanel.add(r2); leftPanel.add(r3);
            leftPanel.add(Box.createVerticalStrut(10)); 
            leftPanel.add(btnSave); leftPanel.add(btnHistory);

            JTextArea txtDescription = new JTextArea();
            txtDescription.setEditable(false);
            txtDescription.setLineWrap(true);
            txtDescription.setWrapStyleWord(true);
            txtDescription.setFont(new Font("Segoe UI", Font.ITALIC, 14));
            txtDescription.setForeground(new Color(80, 80, 80));
            txtDescription.setBackground(Color.WHITE);
            txtDescription.setBorder(new EmptyBorder(15, 15, 15, 15)); 

            JPanel rightContainer = new JPanel(new BorderLayout());
            rightContainer.setBackground(Color.WHITE);
            rightContainer.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY), 
                "Scheme Description (How it works)", 
                TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, 
                new Font("Segoe UI", Font.PLAIN, 14)
            ));
            rightContainer.add(txtDescription, BorderLayout.CENTER);

            ActionListener updateText = e -> {
                if (r1.isSelected()) txtDescription.setText("📌 OPTION A (Simple):\n\nIf a vehicle overstays (> 24 hours), they are charged a FLAT penalty of RM 50.00.\nIt doesn't matter if they overstay by 1 hour or 10 days, the fine is the same.");
                else if (r2.isSelected()) txtDescription.setText("📈 OPTION B (Step-up):\n\nThe fine increases the longer they stay:\n• First 24h overstay: RM 50\n• 24h - 48h: Add extra RM 100\n• 48h - 72h: Add extra RM 150\n• > 72h: Add extra RM 200\n\nGood for discouraging long-term abandonment.");
                else if (r3.isSelected()) txtDescription.setText("⏱️ OPTION C (Time-based):\n\nFor every hour passed after the 24-hour limit, charge RM 20.00 per hour.\n\nExample: Overstaying 2 hours = RM 40 fine.");
            };

            r1.addActionListener(updateText); r2.addActionListener(updateText); r3.addActionListener(updateText);

            FineScheme fs = ParkingLotSystem.getInstance().getFineScheme();
            if (fs == FineScheme.FIXED) { r1.setSelected(true); updateText.actionPerformed(null); }
            else if (fs == FineScheme.PROGRESSIVE) { r2.setSelected(true); updateText.actionPerformed(null); }
            else { r3.setSelected(true); updateText.actionPerformed(null); }

            btnSave.addActionListener(e -> {
                if(r1.isSelected()) ParkingLotSystem.getInstance().setFineScheme(FineScheme.FIXED);
                else if(r2.isSelected()) ParkingLotSystem.getInstance().setFineScheme(FineScheme.PROGRESSIVE);
                else ParkingLotSystem.getInstance().setFineScheme(FineScheme.HOURLY);
                JOptionPane.showMessageDialog(panel, "Configuration Saved Successfully!");
            });

            btnHistory.addActionListener(e -> showHistoryDialog());

            panel.add(leftPanel, BorderLayout.WEST);
            panel.add(rightContainer, BorderLayout.CENTER);

            return panel;
        }
    // --- SUB-PANEL: Spot Monitor Map ---
        // --- INTERNAL: SPOT MONITOR (支持 Map/Table 切换) ---
        // 🌟🌟🌟 完整修复版：保留精美地图 + 统计卡片 🌟🌟🌟
// 🌟🌟🌟 全能修复版：包含 统计、精美地图、实时表格 和 点击交互 🌟🌟🌟
    // 🌟🌟🌟 全能修复版：包含统计、精美地图、带搜索功能的实时表格、行颜色高亮和编辑限制 🌟🌟🌟
    class SpotMonitorPanel extends JPanel {
        private CardLayout cardLayout;
        private JPanel centerPanel;
        private JTabbedPane mapTabs; 
        private JPanel statsPanel; 
        private DefaultTableModel tableModel;
        private TableRowSorter<DefaultTableModel> sorter; // 🌟 搜索排序器

        public SpotMonitorPanel() {
            setLayout(new BorderLayout());

            // 1. 顶部大容器 (Title + Stats)
            JPanel topContainer = new JPanel(new BorderLayout());
            topContainer.setBackground(Color.WHITE);

            // --- A. Header (Title + Switcher + Refresh) ---
            JLabel title = new JLabel("Parking Monitor");
            title.setFont(new Font("Segoe UI", Font.BOLD, 24));
            title.setBorder(new EmptyBorder(10, 20, 10, 10));

            String[] views = {"🗺️ Map View", "📋 List View (Table)"};
            JComboBox<String> comboView = new JComboBox<>(views);
            comboView.addActionListener(e -> {
                if (cardLayout != null) {
                    cardLayout.show(centerPanel, comboView.getSelectedIndex() == 0 ? "MAP" : "TABLE");
                }
            });

            JButton btnRefresh = new JButton("Refresh Data");
            btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnRefresh.addActionListener(e -> refreshAllData());

            JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            controlPanel.setBackground(Color.WHITE);
            controlPanel.add(new JLabel("View Mode: "));
            controlPanel.add(comboView);
            controlPanel.add(btnRefresh);

            JPanel header = new JPanel(new BorderLayout());
            header.setBackground(Color.WHITE);
            header.add(title, BorderLayout.WEST);
            header.add(controlPanel, BorderLayout.EAST);
            topContainer.add(header, BorderLayout.NORTH);

            // --- B. 统计面板 (Dashboard) ---
            statsPanel = createStatsPanel();
            topContainer.add(statsPanel, BorderLayout.CENTER);
            add(topContainer, BorderLayout.NORTH);

            // 2. 中间内容区 (Map & Table)
            cardLayout = new CardLayout();
            centerPanel = new JPanel(cardLayout);
            
            centerPanel.add(createMapPanel(), "MAP");
            centerPanel.add(createTablePanel(), "TABLE"); 

            add(centerPanel, BorderLayout.CENTER);
            refreshAllData(); // 初始加载数据
        }

        // --- 🌟 核心修改：带搜索栏和颜色逻辑的表格面板 ---
        private JPanel createTablePanel() {
            JPanel container = new JPanel(new BorderLayout());
            container.setBackground(Color.WHITE);

            // 1. 搜索栏 (位于 Table 上方，Stats 下方)
            JPanel searchBarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
            searchBarPanel.setBackground(Color.WHITE);
            JLabel lblSearch = new JLabel("Search Spot / Plate:");
            lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));
            JTextField txtSearch = new JTextField(25);
            txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            searchBarPanel.add(lblSearch);
            searchBarPanel.add(txtSearch);
            container.add(searchBarPanel, BorderLayout.NORTH);

            // 2. 表格定义
            String[] cols = {"Spot ID", "Floor", "Type", "Status", "Occupied By"};
            tableModel = new DefaultTableModel(cols, 0) {
                @Override 
                public boolean isCellEditable(int r, int c) { 
                    // 🌟 规则 1：只有 Status 列 (索引 3) 可能可以编辑
                    if (c != 3) return false; 
                    // 🌟 规则 2：如果状态是 Occupied，不可修改
                    Object statusObj = getValueAt(r, 3);
                    String status = (statusObj != null) ? statusObj.toString() : "";
                    return !status.equalsIgnoreCase("Occupied");
                }
            };

            JTable table = new JTable(tableModel) {
                // 🌟 规则 3：设置行颜色
                @Override
                public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                    Component c = super.prepareRenderer(renderer, row, column);
                    if (!isRowSelected(row)) {
                        String status = getModel().getValueAt(convertRowIndexToModel(row), 3).toString();
                        if (status.equalsIgnoreCase("Occupied")) {
                            c.setBackground(new Color(255, 200, 200)); // 粉色
                        } else if (status.equalsIgnoreCase("Maintenance")) {
                            c.setBackground(new Color(255, 240, 200)); // 橙黄色
                        } else {
                            c.setBackground(Color.WHITE);
                        }
                    }
                    return c;
                }
            };

            table.setRowHeight(35);
            table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

            // 设置 Status 列的下拉菜单编辑器
            JComboBox<SpotStatus> comboStatus = new JComboBox<>(new SpotStatus[]{
                SpotStatus.AVAILABLE, SpotStatus.MAINTENANCE, SpotStatus.INCORRECT
            });
            table.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(comboStatus));

            // 监听 Status 手动修改
            tableModel.addTableModelListener(e -> {
                if (e.getType() == javax.swing.event.TableModelEvent.UPDATE && e.getColumn() == 3) {
                    int row = e.getFirstRow();
                    String id = tableModel.getValueAt(row, 0).toString();
                    SpotStatus newStatus = (SpotStatus) tableModel.getValueAt(row, 3);
                    ParkingLotSystem.getInstance().updateSpotStatus(id, newStatus);
                    // 刷新一下以更新统计数据
                    SwingUtilities.invokeLater(() -> refreshAllData());
                }
            });

            // 3. 搜索逻辑
            sorter = new TableRowSorter<>(tableModel);
            table.setRowSorter(sorter);
            txtSearch.getDocument().addDocumentListener(new DocumentListener() {
                public void insertUpdate(DocumentEvent e) { filter(); }
                public void removeUpdate(DocumentEvent e) { filter(); }
                public void changedUpdate(DocumentEvent e) { filter(); }
                private void filter() {
                    String text = txtSearch.getText();
                    if (text.trim().length() == 0) sorter.setRowFilter(null);
                    else sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            });

            container.add(new JScrollPane(table), BorderLayout.CENTER);
            return container;
        }

        private JPanel createMapPanel() {
            JPanel p = new JPanel(new BorderLayout());
            mapTabs = new JTabbedPane();
            mapTabs.setFont(new Font("Segoe UI", Font.BOLD, 14));
            p.add(mapTabs, BorderLayout.CENTER);
            return p;
        }

        public void refreshAllData() {
            // 1. 刷新统计卡片
            if (statsPanel != null) {
                java.awt.Container parent = statsPanel.getParent();
                if (parent != null) {
                    parent.remove(statsPanel);
                    statsPanel = createStatsPanel(); 
                    parent.add(statsPanel, BorderLayout.CENTER);
                    parent.revalidate(); parent.repaint();
                }
            }

            // 2. 刷新地图
            if (mapTabs != null) {
                mapTabs.removeAll(); 
                List<ParkingSpot> all = ParkingLotSystem.getInstance().getAllSpots();
                for (int f = 1; f <= 5; f++) {
                    mapTabs.addTab("  Floor " + f + "  ", createFloorMap(f, all));
                }
            }

            // 3. 刷新表格数据
            if (tableModel != null) {
                tableModel.setRowCount(0);
                List<ParkingSpot> all = ParkingLotSystem.getInstance().getAllSpots();
                for (ParkingSpot s : all) {
                    String plate = (s.getCurrentVehicle() != null) ? s.getCurrentVehicle().getPlateNumber() : "-";
                    tableModel.addRow(new Object[]{s.getId(), "Floor " + s.getFloor(), s.getType(), s.getStatus(), plate});
                }
            }
        }

        // --- 交互逻辑 (地图模式点击) ---
        private void showAdminSpotAction(ParkingSpot spot) {
            if (spot.getStatus() == SpotStatus.OCCUPIED) {
                Vehicle v = spot.getCurrentVehicle();
                String plate = (v != null) ? v.getPlateNumber() : "Unknown";
                Ticket t = ParkingLotSystem.getInstance().getActiveTicket(plate);
                String ticketId = (t != null) ? t.getTicketId() : "N/A";
                String entryTime = (t != null) ? t.getEntryTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) : "N/A";

                String msg = "<html><body style='width: 250px; font-family: Segoe UI;'>" +
                             "<h2 style='color: #B22222;'>🚗 Occupied Spot Info</h2><hr>" +
                             "<p><b>Ticket ID:</b> " + ticketId + "</p>" +
                             "<p><b>Spot ID:</b> " + spot.getId() + "</p>" +
                             "<p><b>Plate Number:</b> <span style='color: red; font-weight: bold;'>" + plate + "</span></p>" +
                             "<p><b>Entry Time:</b> " + entryTime + "</p>" +
                             "</body></html>";
                JOptionPane.showMessageDialog(this, msg, "Spot Details", JOptionPane.INFORMATION_MESSAGE);
            } else {
                showAdjustStatusDialog(spot);
            }
        }

        private void showAdjustStatusDialog(ParkingSpot spot) {
            JPanel p = new JPanel(new GridLayout(2, 1, 5, 5));
            p.add(new JLabel("Adjust status for " + spot.getId() + ":"));
            SpotStatus[] options = {SpotStatus.AVAILABLE, SpotStatus.MAINTENANCE, SpotStatus.INCORRECT};
            JComboBox<SpotStatus> comboStatus = new JComboBox<>(options);
            comboStatus.setSelectedItem(spot.getStatus());
            p.add(comboStatus);

            if (JOptionPane.showConfirmDialog(this, p, "Adjust Status", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                spot.setStatus((SpotStatus) comboStatus.getSelectedItem());
                DatabaseHelper.getInstance().updateSpot(spot);
                refreshAllData();
            }
        }

        // --- 地图绘图辅助方法 ---
        private JPanel createFloorMap(int f, List<ParkingSpot> all) {
            JPanel container = new JPanel(new GridBagLayout());
            container.setBackground(new Color(240, 242, 245));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 1.0;
            int y = 0;
            if (f == 1) { gbc.gridy = y++; container.add(createMapHeader(), gbc); }
            int[][] rows = {{1, 2}, {3, 4}, {5, 6}};
            for (int[] rowPair : rows) {
                gbc.gridy = y++; container.add(createHorizontalRoad(), gbc);
                gbc.gridy = y++; gbc.weighty = 1.0; gbc.fill = GridBagConstraints.BOTH;
                container.add(createTwoRowBlock(f, rowPair[0], rowPair[1], all), gbc);
                gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weighty = 0;
            }
            gbc.gridy = y++; container.add(createHorizontalRoad(), gbc);
            if (f == 1) { gbc.gridy = y++; container.add(createMapFooter(), gbc); }
            return container;
        }

        private JPanel createMapHeader() {
            JPanel h = new JPanel(new BorderLayout()); h.setOpaque(false);
            JLabel e = new JLabel("  ⬇ ENTRY  "); e.setOpaque(true); e.setBackground(new Color(40,160,60)); e.setForeground(Color.WHITE);
            JLabel x = new JLabel("  EXIT ➡  "); x.setOpaque(true); x.setBackground(new Color(200,50,50)); x.setForeground(Color.WHITE);
            h.add(e, BorderLayout.WEST); h.add(x, BorderLayout.EAST);
            return h;
        }

        private JPanel createMapFooter() {
            JLabel f = new JLabel("   ⬇ TO FACULTY ENTRANCE ⬇   ", SwingConstants.CENTER);
            f.setOpaque(true); f.setBackground(new Color(0,100,200)); f.setForeground(Color.WHITE);
            JPanel p = new JPanel(); p.setOpaque(false); p.add(f); return p;
        }

        private JPanel createHorizontalRoad() {
            return new JPanel() {
                @Override protected void paintComponent(Graphics g) {
                    super.paintComponent(g); Graphics2D g2 = (Graphics2D) g;
                    g2.setColor(new Color(220, 220, 220)); g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.setColor(Color.WHITE); g2.setStroke(new BasicStroke(3, 1, 1, 0, new float[]{15}, 0));
                    g2.drawLine(20, getHeight()/2, getWidth()-20, getHeight()/2);
                }
            };
        }

        private JPanel createTwoRowBlock(int floor, int r1, int r2, List<ParkingSpot> all) {
            JPanel block = new JPanel(new GridBagLayout()); block.setOpaque(false);
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.BOTH; gbc.weighty = 1.0;
            for (int rIndex = 0; rIndex < 2; rIndex++) {
                int row = (rIndex == 0) ? r1 : r2;
                if (rIndex == 0) { gbc.gridx = 0; gbc.gridy = 0; gbc.gridheight = 2; gbc.weightx = 0.05; block.add(createVerticalRoad(), gbc); }
                for (int s = 1; s <= 8; s++) {
                    int col = (s <= 4) ? s : s + 1;
                    gbc.gridx = col; gbc.gridy = rIndex; gbc.gridheight = 1; gbc.weightx = 1.0; gbc.insets = new Insets(2,2,2,2);
                    String id = "F" + floor + "-R" + row + "-S" + s;
                    ParkingSpot spot = all.stream().filter(sp -> sp.getId().equals(id)).findFirst().orElse(null);
                    if (spot != null) {
                        ModernSpotButton btn = new ModernSpotButton(spot);
                        btn.addActionListener(ev -> showAdminSpotAction(spot)); 
                        block.add(btn, gbc);
                    } else { block.add(new JLabel(""), gbc); }
                    if (s == 4 && rIndex == 0) { gbc.gridx = 5; gbc.gridy = 0; gbc.gridheight = 2; gbc.weightx = 0.05; block.add(createVerticalRoad(), gbc); }
                }
                if (rIndex == 0) { gbc.gridx = 10; gbc.gridy = 0; gbc.gridheight = 2; gbc.weightx = 0.05; block.add(createVerticalRoad(), gbc); }
            }
            return block;
        }

        private JPanel createVerticalRoad() {
            return new JPanel() {
                @Override protected void paintComponent(Graphics g) {
                    super.paintComponent(g); Graphics2D g2 = (Graphics2D) g;
                    g2.setColor(new Color(220, 220, 220)); g2.fillRect(0, 0, getWidth(), getHeight());
                    g2.setColor(Color.WHITE); g2.setStroke(new BasicStroke(2, 1, 1, 0, new float[]{10}, 0));
                    g2.drawLine(getWidth()/2, 5, getWidth()/2, getHeight()-5);
                }
            };
        }
    }
            private JPanel createUnpaidFinesPanel() {
            JPanel p = new JPanel(new BorderLayout());
            p.setBorder(new EmptyBorder(10, 20, 10, 20));
            
            // 1. 顶部容器 (包含标题、按钮栏、搜索栏)
            JPanel headerContainer = new JPanel();
            headerContainer.setLayout(new BoxLayout(headerContainer, BoxLayout.Y_AXIS));
            headerContainer.setBackground(Color.WHITE);

            // 1.1 标题
            JLabel lblTitle = new JLabel("Unpaid Fines Management");
            lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
            lblTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
            
            // 1.2 按钮栏
            JPanel btnBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            btnBar.setBackground(Color.WHITE);
            btnBar.setBorder(new EmptyBorder(0, 0, 10, 0)); 

            JButton btnManual = new JButton("Add Manual Fine / Debt");
            btnManual.setBackground(new Color(255, 192, 203));
            btnManual.setForeground(Color.BLACK);
            btnManual.setFocusPainted(false);
            btnManual.addActionListener(e -> showAddManualFineDialog());

            JButton btnType = new JButton("Add Fine Type");
            btnType.setBackground(new Color(135, 206, 250));
            btnType.setForeground(Color.BLACK);
            btnType.setFocusPainted(false);
            btnType.addActionListener(e -> showAddFineTypeDialog());
            
            JButton btnViewAll = new JButton("View All History (P/U/V)");
            btnViewAll.setBackground(new Color(240, 230, 140)); 
            btnViewAll.setForeground(Color.BLACK);
            btnViewAll.setFocusPainted(false);
            btnViewAll.addActionListener(e -> showAllFinesDialog());

            btnBar.add(btnManual); btnBar.add(btnType); btnBar.add(btnViewAll);

            // 1.3 🌟 搜索栏 (新增) 🌟
            JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
            searchPanel.setBackground(Color.WHITE);
            searchPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
            
            JLabel lblSearch = new JLabel("Search:");
            lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));
            JTextField txtSearch = new JTextField(20);
            txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            
            searchPanel.add(lblSearch);
            searchPanel.add(txtSearch);

            // 将所有头部组件加入容器
            // 使用 JPanel 包装一下 label 以防 BoxLayout 拉伸问题
            JPanel titleWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT));
            titleWrapper.setBackground(Color.WHITE);
            titleWrapper.add(lblTitle);
            
            headerContainer.add(titleWrapper);
            headerContainer.add(btnBar);
            headerContainer.add(searchPanel);

            // --- 表格部分 ---
            String[] cols = {"ID", "Plate", "Amount", "Remarks", "Date", "Action"};
            fineModel = new DefaultTableModel(cols, 0) {
                @Override public boolean isCellEditable(int r, int c) { return c == 5; }
            };
            JTable table = new JTable(fineModel);
            table.setRowHeight(30); 
            table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            table.getColumnModel().getColumn(5).setCellRenderer(new VoidButtonRenderer());
            table.getColumnModel().getColumn(5).setCellEditor(new VoidButtonEditor(new JCheckBox(), this)); 
            
            // 🌟 设置搜索排序器 🌟
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(fineModel);
            table.setRowSorter(sorter);
            
            // 🌟 监听输入框，实时过滤 🌟
            txtSearch.getDocument().addDocumentListener(new DocumentListener() {
                public void insertUpdate(DocumentEvent e) { filter(); }
                public void removeUpdate(DocumentEvent e) { filter(); }
                public void changedUpdate(DocumentEvent e) { filter(); }
                private void filter() {
                    String text = txtSearch.getText();
                    if (text.trim().length() == 0) sorter.setRowFilter(null);
                    else sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text)); // (?i) 表示忽略大小写
                }
            });

            JScrollPane scroll = new JScrollPane(table);
            
            p.add(headerContainer, BorderLayout.NORTH);
            p.add(scroll, BorderLayout.CENTER);
            
            // --- 底部刷新逻辑 ---
            finesRefresher = () -> {
                fineModel.setRowCount(0);
                List<String[]> fines = ParkingLotSystem.getInstance().getAllOutstandingFines();
                for (String[] f : fines) fineModel.addRow(new Object[]{f[0], f[1], "RM " + f[2], f[3], f[4], "Void"});
                txtSearch.setText(""); // 刷新时清空搜索
            };
            
            JButton btnRefresh = new JButton("Refresh Data");
            btnRefresh.addActionListener(e -> finesRefresher.run());
            p.add(btnRefresh, BorderLayout.SOUTH);
            
            finesRefresher.run(); 
            return p;
        }

        // --- 辅助方法：现在是 AdminPanel 的成员方法 ---
        private void showAddManualFineDialog() {
            JPanel p = new JPanel(new GridLayout(3, 2, 10, 10));
            JTextField txtPlate = new JTextField();
            JTextField txtAmount = new JTextField();
            JComboBox<String> comboReason = new JComboBox<>();
            comboReason.setEditable(true);
            
            Map<String, Double> fineTypes = ParkingLotSystem.getInstance().getFineTypes();
            comboReason.addItem("-- Select or Type --");
            for (String type : fineTypes.keySet()) {
                comboReason.addItem(type);
            }
            
            // 🌟 修改这里：使用 String.format("%.2f") 强制显示两位小数
            comboReason.addActionListener(e -> {
                String selected = (String) comboReason.getSelectedItem();
                if (fineTypes.containsKey(selected)) {
                    double val = fineTypes.get(selected);
                    txtAmount.setText(String.format("%.2f", val)); // 🌟 例如：50.00
                }
            });
            
            p.add(new JLabel("Vehicle Plate Number:")); p.add(txtPlate);
            p.add(new JLabel("Remarks / Reason:")); p.add(comboReason);
            p.add(new JLabel("Fine Amount (RM):")); p.add(txtAmount);
            
            int result = JOptionPane.showOptionDialog(this, p, "Add Manual Fine", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, new Object[]{"Fine", "Cancel"}, "Fine");
            
            if (result == 0) { 
                String plate = txtPlate.getText().toUpperCase().trim();
                String amountStr = txtAmount.getText().trim();
                Object reasonObj = comboReason.getSelectedItem();
                String remarks = (reasonObj != null) ? reasonObj.toString().trim() : "";
                
                if (plate.isEmpty() || amountStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Please fill in Plate and Amount!", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                try {
                    double amount = Double.parseDouble(amountStr);
                    ParkingLotSystem.getInstance().addFine(plate, amount, remarks);
                    // 🌟 成功提示也顺便格式化一下
                    JOptionPane.showMessageDialog(this, "Fine of RM " + String.format("%.2f", amount) + " added successfully for " + plate);
                    refreshData(); 
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid Amount!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        private void showAddFineTypeDialog() {
            JPanel p = new JPanel(new GridLayout(2, 2, 10, 10));
            JTextField txtReason = new JTextField();
            JTextField txtAmount = new JTextField();
            
            p.add(new JLabel("Fine Reason / Type:")); p.add(txtReason);
            p.add(new JLabel("Default Amount (RM):")); p.add(txtAmount);
            
            int res = JOptionPane.showConfirmDialog(this, p, "Configure New Fine Type", JOptionPane.OK_CANCEL_OPTION);
            
            if (res == JOptionPane.OK_OPTION) {
                try {
                    String reason = txtReason.getText().trim();
                    double amount = Double.parseDouble(txtAmount.getText().trim());
                    
                    if(!reason.isEmpty()) {
                        ParkingLotSystem.getInstance().createFineType(reason, amount);
                        JOptionPane.showMessageDialog(this, "Fine Type Added Successfully!");
                    }
                } catch(Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid Input!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        // 🌟🌟🌟 历史记录弹窗 (已添加搜索功能) 🌟🌟🌟
        private void showAllFinesDialog() {
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "All Fines History (Paid / Unpaid / Void)", true);
            dialog.setSize(900, 600);
            dialog.setLocationRelativeTo(this);
            dialog.setLayout(new BorderLayout());
            
            // 🌟 顶部搜索面板 🌟
            JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            searchPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
            JLabel lblSearch = new JLabel("Filter History:");
            lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));
            JTextField txtSearch = new JTextField(20);
            txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            searchPanel.add(lblSearch);
            searchPanel.add(txtSearch);
            
            dialog.add(searchPanel, BorderLayout.NORTH);

            // 表格
            String[] cols = {"ID", "Plate", "Amount", "Reason", "Status", "Date"};
            DefaultTableModel m = new DefaultTableModel(cols, 0) {
                 @Override public boolean isCellEditable(int r, int c) { return false; }
            };
            JTable t = new JTable(m);
            styleTable(t);
            
            // 🌟 设置搜索排序器 🌟
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(m);
            t.setRowSorter(sorter);
            
            // 🌟 监听搜索输入 🌟
            txtSearch.getDocument().addDocumentListener(new DocumentListener() {
                public void insertUpdate(DocumentEvent e) { filter(); }
                public void removeUpdate(DocumentEvent e) { filter(); }
                public void changedUpdate(DocumentEvent e) { filter(); }
                private void filter() {
                    String text = txtSearch.getText();
                    if (text.trim().length() == 0) sorter.setRowFilter(null);
                    else sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            });
            
            // 获取数据
            List<String[]> allFines = ParkingLotSystem.getInstance().getAllFines(); 
            for(String[] row : allFines) {
                String statusRaw = row[4];
                String statusDisplay = statusRaw;
                if("P".equals(statusRaw)) statusDisplay = "✅ PAID";
                else if("U".equals(statusRaw)) statusDisplay = "❌ UNPAID";
                else if("V".equals(statusRaw)) statusDisplay = "⚪ VOID";
                
                m.addRow(new Object[]{row[0], row[1], "RM " + row[2], row[3], statusDisplay, row[5]});
            }
            
            dialog.add(new JScrollPane(t), BorderLayout.CENTER);
            
            JPanel btnPanel = new JPanel();
            JButton btnClose = new JButton("Close");
            btnClose.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnClose.addActionListener(e -> dialog.dispose());
            btnPanel.add(btnClose);
            dialog.add(btnPanel, BorderLayout.SOUTH);
            
            dialog.setVisible(true);
        }

        // 1. 替换 AdminPanel 类中第 1066 行的 createTransTablePanel
        private JPanel createTransTablePanel() {
            JPanel p = new JPanel(new BorderLayout()); 
            p.setBorder(new EmptyBorder(10, 20, 10, 20));
            p.setBackground(Color.WHITE); // 统一背景色
            
            // --- 1. 顶部区域 (标题 + 搜索栏) ---
            JPanel topContainer = new JPanel(new BorderLayout());
            topContainer.setBackground(Color.WHITE);
            
            // 标题
            JLabel lblTitle = new JLabel("<html><h1>Transaction History</h1><p style='color:blue;'><i>Double-click row to view full receipt</i></p></html>");
            lblTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
            
            // 搜索栏
            JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
            searchPanel.setBackground(Color.WHITE);
            
            JLabel lblSearch = new JLabel("Search (Invoice / Plate / Method):  ");
            lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 14));
            
            JTextField txtSearch = new JTextField(25);
            txtSearch.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            
            searchPanel.add(lblSearch);
            searchPanel.add(txtSearch);
            
            topContainer.add(lblTitle, BorderLayout.NORTH);
            topContainer.add(searchPanel, BorderLayout.SOUTH);
            
            p.add(topContainer, BorderLayout.NORTH);
            
            // --- 2. 表格区域 ---
            String[] cols = {"Invoice", "Plate", "Total", "Method", "Time"};
            
            DefaultTableModel model = new DefaultTableModel(cols, 0) {
                @Override public boolean isCellEditable(int r, int c) { return false; }
            };
            
            JTable table = new JTable(model); 
            table.setRowHeight(30); 
            table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

            // 🌟 核心：添加排序和过滤功能
            TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
            table.setRowSorter(sorter);
            
            // 🌟 核心：监听搜索框输入
            txtSearch.getDocument().addDocumentListener(new DocumentListener() {
                public void insertUpdate(DocumentEvent e) { filter(); }
                public void removeUpdate(DocumentEvent e) { filter(); }
                public void changedUpdate(DocumentEvent e) { filter(); }
                private void filter() {
                    String text = txtSearch.getText();
                    if (text.trim().length() == 0) sorter.setRowFilter(null);
                    else sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text)); // (?i) 忽略大小写
                }
            });

            // 双击查看收据
            table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2 && table.getSelectedRow() != -1) {
                        // 注意：过滤后行号会变，必须转换 convertRowIndexToModel
                        int viewRow = table.getSelectedRow();
                        int modelRow = table.convertRowIndexToModel(viewRow);
                        String invId = model.getValueAt(modelRow, 0).toString();
                        
                        String[] data = com.parking.db.DatabaseHelper.getInstance().getTransactionByInvoiceId(invId);
                        if (data != null) {
                            showReceiptDialog(data);
                        }
                    }
                }
            });

            p.add(new JScrollPane(table), BorderLayout.CENTER);
            
            // --- 3. 底部刷新按钮 ---
            JButton btnRef = new JButton("Refresh History");
            btnRef.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnRef.addActionListener(e -> { 
                model.setRowCount(0); 
                List<String[]> hist = ParkingLotSystem.getInstance().getTransactionHistory(); 
                for (String[] h : hist) model.addRow(new Object[]{h[0], h[1], "RM " + h[2], h[3], h[4]}); 
                txtSearch.setText(""); // 刷新时清空搜索框
            });
            
            JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            bottomPanel.setBackground(Color.WHITE);
            bottomPanel.add(btnRef);
            p.add(bottomPanel, BorderLayout.SOUTH);
            
            btnRef.doClick(); // 初始加载
            return p;
        }
        
// 2. 在 AdminPanel 类中添加这个方法，逻辑与 ExitPanel.printFinalReceipt 完全一致
private void showReceiptDialog(String[] data) {
    String invId = data[0], plate = data[1], total = data[2], paid = data[3], bal = data[4];
    String method = data[5], payTime = data[6], entry = data[7], dur = data[8], parkFee = data[9], spotType = data[11], spotId = data[12], vType = data[13];

    List<String[]> paidDetails = com.parking.db.DatabaseHelper.getInstance().getPaidFinesByInvoiceId(invId);

    StringBuilder sb = new StringBuilder();
    sb.append("=========================================================\n");
    sb.append("                         INVOICE                         \n");
    sb.append("                UNIVERSITY PARKING SYSTEM                \n");
    sb.append("=========================================================\n");
    sb.append("Invoice Date: ").append(payTime.substring(0, 10)).append("\n\n");
    
    sb.append(String.format("%-25s : %s\n", "TICKET ID", invId));
    sb.append(String.format("%-25s : %s\n", "Parking Spot", (spotId != null ? spotId : "-")));
    sb.append(String.format("%-25s : %s\n", "Plate Number", plate));
    sb.append(String.format("%-25s : %s\n", "Parking Spot Type", spotType));
    sb.append(String.format("%-25s : %s\n", "Vehicle Type", (vType != null ? vType : "-")));
    sb.append(String.format("%-25s : %s\n", "Entry Date & Time", entry));
    sb.append(String.format("%-25s : %s\n", "Payment Date & Time", payTime));
    sb.append(String.format("%-25s : %s\n\n", "Parking Duration", dur));

    sb.append(String.format("%-25s : %25s\n", "Parking Fees", "RM " + String.format("%6s", parkFee)));
    
    if (!paidDetails.isEmpty()) {
        sb.append("\n- - - - - PAID ARREARS DETAILS - - - - -\n");
        for (String[] f : paidDetails) {
            sb.append("[" + f[0] + "]\n"); 
            sb.append(String.format("%-20s : %6s\n", "" + f[1] + "", "RM " + String.format("%6s", f[2]))); 
        }
        sb.append("---------------------------------------------------------\n");
    }
    
    sb.append(String.format("%-25s : %25s\n", "TOTAL PAID AMT", "RM " + String.format("%6s", total)));
    sb.append(String.format("%-25s : %25s\n", "Payment Type", method));
    sb.append("---------------------------------------------------------\n");
    sb.append(String.format("%-25s : %25s\n", "CUSTOMER PAID", "RM " + String.format("%6s", paid)));
    sb.append(String.format("%-25s : %25s\n", "CHANGE RETURNED", "RM " + String.format("%6s", bal)));
    sb.append("=========================================================\n");
    sb.append("          Thank you & Have a safe trip!           \n");
    sb.append(String.format("Ref: %s\n", invId)); 
    sb.append("=========================================================\n");

    JTextArea r = new JTextArea(sb.toString());
    r.setFont(new Font("Monospaced", Font.BOLD, 12));
    r.setEditable(false);
    JOptionPane.showMessageDialog(this, new JScrollPane(r), "View Historical Receipt", JOptionPane.PLAIN_MESSAGE);
}

        private void showHistoryDialog() {
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Fine Option Change History", true);
            dialog.setSize(500, 400); dialog.setLocationRelativeTo(this);
            String[] columns = {"Change Time", "Fine Option Detail"};
            DefaultTableModel historyModel = new DefaultTableModel(columns, 0);
            JTable historyTable = new JTable(historyModel);
            historyTable.setFont(new Font("Segoe UI", Font.PLAIN, 14)); historyTable.setRowHeight(25);
            historyTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14)); historyTable.getTableHeader().setBackground(new Color(230, 230, 230));
            historyTable.getColumnModel().getColumn(0).setPreferredWidth(150); historyTable.getColumnModel().getColumn(1).setPreferredWidth(300);
            List<String[]> logs = ParkingLotSystem.getInstance().getFineChangeHistory();
            for (String[] log : logs) { historyModel.addRow(log); }
            dialog.add(new JScrollPane(historyTable)); dialog.setVisible(true);
        }

        private void styleTable(JTable table) {
            table.setFont(new Font("Segoe UI", Font.PLAIN, 14)); table.setRowHeight(30);
            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14)); table.getTableHeader().setBackground(new Color(230, 230, 230));
        }
    }
    
    // 🌟🌟🌟 Reports Panel 🌟🌟🌟
    // 🌟🌟🌟 Reports Panel (已合并导出按钮：PDF/CSV/TXT) 🌟🌟🌟
// 🌟 完整重写的 ReportsPanel 类
    // 🌟 移除 TXT 后的最终版 ReportsPanel
    // 🌟 集成了自定义日历选择器的 ReportsPanel
// 🌟🌟🌟 最终完美版：ReportsPanel (修复 Landscape + 所有表格列 + 日期筛选) 🌟🌟🌟
// 🌟🌟🌟 最终完整版：ReportsPanel (强制横向打印 + 完整表格列 + 智能日期筛选) 🌟🌟🌟
class ReportsPanel extends JPanel {
    private JEditorPane txtPreview;
    private JComboBox<String> comboReportType;
    private JButton btnDateRange; 
    private JCheckBox chkEnableFilter;
    
    // 引用自定义日历组件
    private DateRangePicker datePicker;
    
    public ReportsPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new javax.swing.border.EmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);
        
        // 初始化日历组件
        datePicker = new DateRangePicker();
        
        // --- 1. 顶部工具栏 ---
        JPanel topContainer = new JPanel(new GridLayout(2, 1, 0, 5));
        topContainer.setBackground(Color.WHITE);
        
        // 1.1 第一行：报表选择 + 导出按钮
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        row1.setBackground(new Color(240, 240, 240));
        row1.setBorder(new javax.swing.border.LineBorder(Color.LIGHT_GRAY));
        
        JLabel lblSelect = new JLabel("Select Report Type:");
        lblSelect.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        String[] reports = {
            "1. Vehicle List (Current in Slot)",
            "2. Revenue Report",
            "3. Occupancy Report",
            "4. Fine Report"
        };
        comboReportType = new JComboBox<>(reports);
        comboReportType.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboReportType.addActionListener(e -> generatePreview());
        
        JButton btnExportFile = new JButton("Export / Print Report");
        btnExportFile.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnExportFile.setBackground(new Color(0, 120, 215)); 
        btnExportFile.setForeground(Color.BLACK);
        btnExportFile.setFocusPainted(false);
        btnExportFile.addActionListener(e -> showExportDialog());
        
        row1.add(lblSelect);
        row1.add(comboReportType);
        row1.add(btnExportFile);
        
        // 1.2 第二行：日期筛选栏
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        row2.setBackground(Color.WHITE);
        row2.setBorder(BorderFactory.createTitledBorder("Filter by Date Range"));
        
        chkEnableFilter = new JCheckBox("Filter Data");
        chkEnableFilter.setFont(new Font("Segoe UI", Font.BOLD, 12));
        chkEnableFilter.setBackground(Color.WHITE);
        chkEnableFilter.addActionListener(e -> {
            btnDateRange.setEnabled(chkEnableFilter.isSelected());
            generatePreview();
        });
        
        // 触发日历的按钮
        btnDateRange = new JButton("📅 Select Date Range");
        btnDateRange.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnDateRange.setBackground(new Color(245, 245, 245));
        btnDateRange.setEnabled(false); 
        btnDateRange.addActionListener(e -> showCalendarPopup());
        
        // 当日历选择变动时，更新按钮文字并刷新报表
        datePicker.setOnSelectionChanged(() -> {
            btnDateRange.setText("📅 " + datePicker.getRangeText());
            generatePreview();
        });

        row2.add(chkEnableFilter);
        row2.add(btnDateRange);

        topContainer.add(row1);
        topContainer.add(row2);
        
        add(topContainer, BorderLayout.NORTH);
        
        // --- 2. 报告预览区 ---
        txtPreview = new JEditorPane();
        txtPreview.setContentType("text/html");
        txtPreview.setEditable(false);
        txtPreview.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        
        add(new JScrollPane(txtPreview), BorderLayout.CENTER);
        generatePreview(); 
    }
    
    private void showCalendarPopup() {
        JDialog d = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Select Date Range", true);
        d.setLayout(new BorderLayout());
        d.add(datePicker, BorderLayout.CENTER);
        
        JButton btnDone = new JButton("Confirm Range");
        btnDone.setBackground(new Color(232, 243, 241)); 
        btnDone.setForeground(Color.BLACK); // 黑色字体
        btnDone.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDone.setFocusPainted(false);
        btnDone.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        btnDone.addActionListener(e -> d.dispose());
        
        d.add(btnDone, BorderLayout.SOUTH);
        d.pack(); 
        d.setLocationRelativeTo(btnDateRange);
        d.setVisible(true);
    }
    
    // 🌟 统一筛选逻辑 (智能识别长短日期格式)
    private boolean isWithinRange(String dateStr) {
        if (!chkEnableFilter.isSelected()) return true;
        if (dateStr == null || dateStr.equals("-") || dateStr.equals("N/A")) return false;

        try {
            java.util.Date start = datePicker.getStartDate();
            java.util.Date end = datePicker.getEndDate();
            if (start == null) return true;

            // 格式 1: 带秒 (Revenue / Fine / DB Standard) - e.g., 14/02/2026 13:19:48
            java.time.format.DateTimeFormatter fmtSec = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            // 格式 2: 不带秒 (Vehicle List) - e.g., 14/02/2026 09:08
            java.time.format.DateTimeFormatter fmtNoSec = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            // 格式 3: 数据库原生 (yyyy-MM-dd)
            java.time.format.DateTimeFormatter fmtDB = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            
            java.time.LocalDateTime ldt = null;

            try { ldt = java.time.LocalDateTime.parse(dateStr, fmtSec); } 
            catch(Exception e1) {
                try { ldt = java.time.LocalDateTime.parse(dateStr, fmtNoSec); } 
                catch(Exception e2) {
                    try { ldt = java.time.LocalDateTime.parse(dateStr, fmtDB); } catch(Exception e3) { return true; }
                }
            }
            
            java.util.Date target = java.sql.Timestamp.valueOf(ldt);
            return !target.before(start) && !target.after(end);

        } catch (Exception e) { return true; }
    }

    // 🌟 生成预览 (已恢复所有 Column，完全对应您的截图)
    private void generatePreview() {
        int index = comboReportType.getSelectedIndex();
        StringBuilder html = new StringBuilder();
        
        ParkingLotSystem sys = ParkingLotSystem.getInstance();
        List<ParkingSpot> allSpots = sys.getAllSpots();
        int totalSpots = allSpots.size();
        long occupiedCount = allSpots.stream().filter(ParkingSpot::isOccupied).count();
        double percentage = (totalSpots > 0) ? ((double) occupiedCount / totalSpots * 100) : 0;
        
        html.append("<html><body style='font-family:Segoe UI; margin:0; padding:0; width:100%;'>");
        
        // Report Header
        html.append("<div style='text-align:center; margin-top:10px; width:100%;'>");
        html.append("<h1 style='color:#2c3e50; margin:0; padding:0; font-size:24pt;'>UNIVERSITY PARKING SYSTEM REPORT</h1>");
        html.append("<hr style='border: 1px solid #000; margin: 5px 0;'>");
        html.append("</div>");
        
        // Info Section
        String genDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        html.append("<div style='font-size:11pt; margin-bottom:5px; padding-left:5px;'>"); 
        html.append("<b>Generated on:</b> ").append(genDate).append("<br>");
        html.append("<b>Generated By:</b> Admin").append("<br>");
        html.append("<b>Total Occupied Status:</b> ").append(String.format("%d / %d (%.2f%%)", occupiedCount, totalSpots, percentage));
        
        if (chkEnableFilter.isSelected()) {
            html.append("<br><b>Filter Range:</b> ").append(datePicker.getRangeText());
        }
        html.append("</div>");
        
        // Styles
        String tableStyle = "width:100%; border-collapse:collapse; font-size:10pt; border:1px solid #000; table-layout: fixed;";
        // Header 样式：浅灰色背景，加粗
        String thStyle = "border:1px solid #000; padding:4px; background-color:#ecf0f1; text-align:left; white-space: nowrap; overflow: hidden; font-weight:bold; color:black;";
        String tdStyle = "border:1px solid #000; padding:4px; white-space: nowrap; overflow: hidden;";
        
        if (index == 0) { // 🚙 Vehicle List (对应截图：7 Columns)
            html.append("<h3 style='margin:5px 0;'>🚙 Current Vehicles in Slots</h3>");
            html.append("<table style='" + tableStyle + "'>");
            // 🌟 100% 还原 image_2efb44.png
            html.append("<tr>")
                .append("<th style='" + thStyle + " width:15%;'>Ticket No</th>") 
                .append("<th style='" + thStyle + " width:10%;'>Spot ID</th>")
                .append("<th style='" + thStyle + " width:10%;'>Type</th>")
                .append("<th style='" + thStyle + " width:10%;'>Vehicle</th>")
                .append("<th style='" + thStyle + " width:10%;'>Plate</th>")
                .append("<th style='" + thStyle + " width:15%;'>Entry Time</th>")
                .append("<th style='" + thStyle + " width:30%;'>Fine Rules</th>")
                .append("</tr>");
            
            for(ParkingSpot s : allSpots) { 
                if (s.isOccupied() && s.getCurrentVehicle() != null) {
                    String plate = s.getCurrentVehicle().getPlateNumber();
                    Ticket t = sys.getActiveTicket(plate);
                    String ticketNo = (t != null) ? t.getTicketId() : "N/A";
                    // 格式保持 dd/MM/yyyy HH:mm 以匹配截图
                    String entryTime = (t != null) ? t.getEntryTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) : "-";
                    String fineRules = (t != null) ? ("Option " + (t.getLockedScheme() == FineScheme.FIXED ? "A" : t.getLockedScheme() == FineScheme.PROGRESSIVE ? "B" : "C") + " (Fixed)") : "Manual";

                    if (!isWithinRange(entryTime)) continue;

                    html.append("<tr>");
                    html.append("<td style='" + tdStyle + "'>").append(ticketNo).append("</td>");
                    html.append("<td style='" + tdStyle + "'>").append(s.getId()).append("</td>");
                    html.append("<td style='" + tdStyle + "'>").append(s.getType()).append("</td>");
                    html.append("<td style='" + tdStyle + "'>").append(s.getCurrentVehicle().getType()).append("</td>");
                    html.append("<td style='" + tdStyle + "'><b>").append(plate).append("</b></td>");
                    html.append("<td style='" + tdStyle + "'>").append(entryTime).append("</td>");
                    html.append("<td style='" + tdStyle + "'>").append(fineRules).append("</td>");
                    html.append("</tr>");
                }
            }
            html.append("</table>");
            
        } else if (index == 1) { // 💰 Revenue Report (对应截图：11 Columns)
             html.append("<h3 style='margin:5px 0;'>💰 Revenue Report</h3>");
             List<String[]> trans = sys.getTransactionHistory();
             double totalFilteredRev = 0;
             
             html.append("<table style='" + tableStyle + "'>");
             // 🌟 100% 还原 image_2ef764.png
             html.append("<tr>")
                 .append("<th style='" + thStyle + " width:4%;'>No</th>")
                 .append("<th style='" + thStyle + " width:10%;'>Plate</th>")
                 .append("<th style='" + thStyle + " width:15%;'>Invoice ID</th>") 
                 .append("<th style='" + thStyle + " width:10%;'>Method</th>")
                 .append("<th style='" + thStyle + " width:12%;'>Pay Time</th>")
                 .append("<th style='" + thStyle + " width:12%;'>Entry Time</th>")
                 .append("<th style='" + thStyle + " width:8%;'>Duration</th>")
                 .append("<th style='" + thStyle + " width:12%;'>Exit Time</th>")
                 .append("<th style='" + thStyle + " width:8%;'>Park Amt</th>")
                 .append("<th style='" + thStyle + " width:8%;'>Fine Amt</th>")
                 .append("<th style='" + thStyle + " width:10%;'>Total (RM)</th>")
                 .append("</tr>");
             
             int count = 1;
             for(String[] t : trans) {
                 String payTime = t[4];
                 // 尝试格式化以统一筛选标准
                 try {
                     LocalDateTime pt = LocalDateTime.parse(payTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                     payTime = pt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
                 } catch(Exception e) {} 

                 if (!isWithinRange(payTime)) continue;

                 String total = (t.length > 2) ? t[2] : "0.00";
                 html.append("<tr>");
                 html.append("<td style='" + tdStyle + "'>").append(count++).append("</td>");
                 html.append("<td style='" + tdStyle + "'><b>").append(t[1]).append("</b></td>"); 
                 html.append("<td style='" + tdStyle + "'>").append(t[0]).append("</td>");      
                 html.append("<td style='" + tdStyle + "'>").append(t[3]).append("</td>");      
                 html.append("<td style='" + tdStyle + "'>").append(payTime).append("</td>");      
                 html.append("<td style='" + tdStyle + "'>").append(t[5]).append("</td>");      
                 html.append("<td style='" + tdStyle + "'>").append(t[6]).append("</td>");      
                 html.append("<td style='" + tdStyle + "'>").append(t[4]).append("</td>");      
                 html.append("<td style='" + tdStyle + "'>RM ").append(t[7]).append("</td>");   
                 html.append("<td style='" + tdStyle + "'>RM ").append(t[8]).append("</td>");   
                 html.append("<td style='" + tdStyle + "'><b>RM ").append(total).append("</b></td>");
                 html.append("</tr>");
                 try { totalFilteredRev += Double.parseDouble(total); } catch(Exception e){}
             }
             html.append("</table>");
             html.append("<h3 style='text-align:right; margin-top:5px;'>Total Collected: RM ").append(String.format("%,.2f", totalFilteredRev)).append("</h3>");

        } else if (index == 2) { // 🅿 Occupancy
             html.append("<h3 style='margin:5px 0;'>🅿 Occupancy Summary</h3>");
             html.append("<ul><li><b>Total Spots:</b> ").append(totalSpots).append("</li>")
                 .append("<li><b>Occupied:</b> ").append(occupiedCount).append("</li>")
                 .append("<li><b>Available:</b> ").append(totalSpots - occupiedCount).append("</li>")
                 .append("<li><b>Occupancy Rate:</b> ").append(String.format("%.2f", percentage)).append("%</li></ul>");
                 
        } else if (index == 3) { // ⚠️ Fine Report (对应截图：5 Columns)
             List<String[]> allFines = sys.getAllFines();
             List<String[]> unpaidList = new ArrayList<>();
             List<String[]> paidList = new ArrayList<>();
             double totalUnpaidAmt = 0.0;
             double totalPaidAmt = 0.0;

             for (String[] f : allFines) {
                 String fineDate = f[5];
                 if (!isWithinRange(fineDate)) continue;

                 String status = f[4]; 
                 double amount = 0.0;
                 try { amount = Double.parseDouble(f[2]); } catch(Exception e){}

                 if ("U".equalsIgnoreCase(status)) {
                     unpaidList.add(f);
                     totalUnpaidAmt += amount;
                 } else if ("P".equalsIgnoreCase(status)) {
                     paidList.add(f);
                     totalPaidAmt += amount;
                 }
             }

             html.append("<h2 style='margin:5px 0; color:#c0392b;'>⚠️ Fine Report System</h2>");
             
             html.append("<div style='background-color:#f9f9f9; border:1px solid #ccc; padding:10px; margin-bottom:15px;'>");
             html.append("<b>📊 Fine Summary:</b><br>");
             html.append("Total Unpaid Fines (Arrears): <b>RM ").append(String.format("%,.2f", totalUnpaidAmt)).append("</b><br>");
             html.append("Total Paid Fines (Collected): <b>RM ").append(String.format("%,.2f", totalPaidAmt)).append("</b>");
             html.append("</div>");

             // Unpaid Table
             html.append("<h3 style='color:red;'>❌ Unpaid Fines (Outstanding)</h3>");
             if (unpaidList.isEmpty()) {
                 html.append("<p><i>No unpaid fines found in range.</i></p>");
             } else {
                 html.append("<table style='" + tableStyle + "'>");
                 // 🌟 100% 还原 image_1f02a8.png
                 html.append("<tr style='background-color:#ffebee;'>")
                     .append("<th style='" + thStyle + " width:10%;'>ID</th>")
                     .append("<th style='" + thStyle + " width:15%;'>Plate</th>")
                     .append("<th style='" + thStyle + " width:15%;'>Amount</th>")
                     .append("<th style='" + thStyle + " width:35%;'>Remarks</th>")
                     .append("<th style='" + thStyle + " width:25%;'>Date Generated</th>")
                     .append("</tr>");
                 
                 for(String[] f : unpaidList) {
                     html.append("<tr>");
                     html.append("<td style='" + tdStyle + "'>").append(f[0]).append("</td>");
                     html.append("<td style='" + tdStyle + "'><b>").append(f[1]).append("</b></td>");
                     html.append("<td style='" + tdStyle + " color:red;'>RM ").append(f[2]).append("</td>");
                     html.append("<td style='" + tdStyle + "'>").append(f[3]).append("</td>");
                     html.append("<td style='" + tdStyle + "'>").append(f[5]).append("</td>");
                     html.append("</tr>");
                 }
                 html.append("</table>");
             }

             html.append("<br><hr><br>");

             // Paid Table
             html.append("<h3 style='color:green;'>✅ Paid Fines (History)</h3>");
             if (paidList.isEmpty()) {
                 html.append("<p><i>No paid fines history found in range.</i></p>");
             } else {
                 html.append("<table style='" + tableStyle + "'>");
                 // 🌟 100% 还原 image_1f02a8.png
                 html.append("<tr style='background-color:#e8f5e9;'>") 
                     .append("<th style='" + thStyle + " width:10%;'>ID</th>")
                     .append("<th style='" + thStyle + " width:15%;'>Plate</th>")
                     .append("<th style='" + thStyle + " width:15%;'>Amount</th>")
                     .append("<th style='" + thStyle + " width:35%;'>Remarks</th>")
                     .append("<th style='" + thStyle + " width:25%;'>Date Generated</th>")
                     .append("</tr>");
                 
                 for(String[] f : paidList) {
                     html.append("<tr>");
                     html.append("<td style='" + tdStyle + "'>").append(f[0]).append("</td>");
                     html.append("<td style='" + tdStyle + "'><b>").append(f[1]).append("</b></td>");
                     html.append("<td style='" + tdStyle + " color:green;'>RM ").append(f[2]).append("</td>");
                     html.append("<td style='" + tdStyle + "'>").append(f[3]).append("</td>");
                     html.append("<td style='" + tdStyle + "'>").append(f[5]).append("</td>");
                     html.append("</tr>");
                 }
                 html.append("</table>");
             }
        }
        
        html.append("<p style='text-align:center; font-size:9pt; margin-top:5px; color:#666;'>--- End of Report ---</p>");
        html.append("</body></html>");
        
        txtPreview.setText(html.toString());
        txtPreview.setCaretPosition(0);
    }

    private void showExportDialog() {
        String[] options = {"PDF (Landscape Print)", "CSV (Excel)"};
        int choice = JOptionPane.showOptionDialog(this, 
            "Please select the export format:", "Export File", 
            0, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            
        if (choice == 0) exportPdf();
        else if (choice == 1) exportCsv();
    }

    // 🌟🌟🌟 核心修复：使用 PrintRequestAttributeSet 强制 Landscape 🌟🌟🌟
    private void exportPdf() {
        try {
            // 1. 创建打印属性集合
            PrintRequestAttributeSet attr = new HashPrintRequestAttributeSet();
            
            // 2. 强制设置为横向 (Landscape)
            attr.add(OrientationRequested.LANDSCAPE);
            
            // 3. 强制设置为 A4 纸
            attr.add(MediaSizeName.ISO_A4);
            
            // 4. 设置边距 (10mm 边距, 宽度 210mm, 高度 297mm - A4)
            attr.add(new MediaPrintableArea(10, 10, 210 - 20, 297 - 20, MediaPrintableArea.MM));

            // 5. 设置页眉和页脚
            java.text.MessageFormat header = new java.text.MessageFormat("");
            java.text.MessageFormat footer = new java.text.MessageFormat("- Page {0} -");

            // 6. 🌟 关键：使用 JEditorPane 自带的 print 方法，它会完美尊重 attr 设置
            // 注意：这里会弹出打印窗口，但默认选项已经被我们锁定为“横向”
            boolean complete = txtPreview.print(header, footer, true, null, attr, true);
            
            if (!complete) {
                // User Cancelled
            }
            
        } catch (java.awt.print.PrinterException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Print Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exportCsv() {
        int index = comboReportType.getSelectedIndex();
        if (index != 0 && index != 1) {
            JOptionPane.showMessageDialog(this, "CSV Export available for List and Revenue only.");
            return;
        }
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV Files", "csv"));
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getAbsolutePath();
            if(!path.toLowerCase().endsWith(".csv")) path += ".csv";
            try (java.io.OutputStreamWriter writer = new java.io.OutputStreamWriter(new java.io.FileOutputStream(path), java.nio.charset.StandardCharsets.UTF_8)) {
                writer.write("\uFEFF"); 
                ParkingLotSystem sys = ParkingLotSystem.getInstance();
                if (index == 0) { 
                    writer.write("Ticket No,Spot ID,Type,Vehicle,Plate,Entry Time,Fine Rules\n");
                    for(ParkingSpot s : sys.getAllSpots()) {
                        if (s.isOccupied() && s.getCurrentVehicle() != null) {
                            String plate = s.getCurrentVehicle().getPlateNumber();
                            Ticket t = sys.getActiveTicket(plate);
                            String entryTime = (t != null) ? t.getEntryTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) : "-";
                            writer.write(String.format("%s,%s,%s,%s,%s,%s,%s\n", 
                                (t!=null?t.getTicketId():"N/A"), s.getId(), s.getType(), s.getCurrentVehicle().getType(), plate, entryTime, "Option A (Fixed)"));
                        }
                    }
                } else { 
                    writer.write("No,Plate,Invoice ID,Method,Pay Time,Entry Time,Duration,Exit Time,Park Amt,Fine Amt,Total (RM)\n");
                    List<String[]> trans = sys.getTransactionHistory();
                    int count = 1;
                    for(String[] t : trans) {
                        writer.write(String.format("%d,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n", 
                            count++, t[1], t[0], t[3], t[4], t[5], t[6], t[4], t[7], t[8], t[2]));
                    }
                }
                JOptionPane.showMessageDialog(this, "CSV Exported Successfully!");
            } catch (Exception ex) { ex.printStackTrace(); }
        }
    }
}

class VoidButtonRenderer extends JButton implements TableCellRenderer {
        public VoidButtonRenderer() {
            setOpaque(true);
            setText("Void");
        }
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }

    class VoidButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        private JTable table;
        private AdminPanel panel; 

        public VoidButtonEditor(JCheckBox checkBox, AdminPanel panel) {
            super(checkBox);
            this.panel = panel; 
            button = new JButton();
            button.setOpaque(true);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    fireEditingStopped();
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.table = table;
            label = (value == null) ? "Void" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                int modelRow = table.convertRowIndexToModel(table.getEditingRow());
                String fineIdStr = (String) table.getModel().getValueAt(modelRow, 0); 
                
                JTextArea txtReason = new JTextArea(5, 30);
                txtReason.setLineWrap(true);
                txtReason.setWrapStyleWord(true);
                JScrollPane scrollPane = new JScrollPane(txtReason);
                
                int result = JOptionPane.showConfirmDialog(null, scrollPane, "Enter Void Reason", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
                
                if (result == JOptionPane.OK_OPTION) {
                    String voidReason = txtReason.getText().trim();
                    if (!voidReason.isEmpty()) {
                        ParkingLotSystem.getInstance().voidFine(Integer.parseInt(fineIdStr), voidReason);
                        JOptionPane.showMessageDialog(null, "Fine Voided Successfully!"); 
                        
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() { panel.refreshData(); }
                        });
                    }
                }
            }
            isPushed = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }

    
    // 🌟🌟🌟 NEW CLASS: Beautiful Custom Parking Spot Button 🌟🌟🌟
    // (Place this at the end of Main.java, outside other classes)
    class ModernSpotButton extends JButton {
        private ParkingSpot spot;
        private boolean isSuitable;

        public ModernSpotButton(ParkingSpot spot) {
            this.spot = spot;
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            // Removed fixed size to allow auto-resizing in Grid
        }

        // 🌟🌟🌟 Reports Panel (请粘贴到文件末尾) 🌟🌟🌟
    // 🌟🌟🌟 最终完整版：ReportsPanel (修复所有 Column + 横向打印 + 日历筛选) 🌟🌟🌟
class ReportsPanel extends JPanel {
    private JEditorPane txtPreview;
    private JComboBox<String> comboReportType;
    private JButton btnDateRange; 
    private JCheckBox chkEnableFilter;
    
    // 引用自定义日历组件
    private DateRangePicker datePicker;
    
    public ReportsPanel() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new javax.swing.border.EmptyBorder(10, 10, 10, 10));
        setBackground(Color.WHITE);
        
        // 初始化日历组件
        datePicker = new DateRangePicker();
        
        // --- 1. 顶部工具栏 ---
        JPanel topContainer = new JPanel(new GridLayout(2, 1, 0, 5));
        topContainer.setBackground(Color.WHITE);
        
        // 1.1 第一行：报表选择 + 导出按钮
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        row1.setBackground(new Color(240, 240, 240));
        row1.setBorder(new javax.swing.border.LineBorder(Color.LIGHT_GRAY));
        
        JLabel lblSelect = new JLabel("Select Report Type:");
        lblSelect.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        String[] reports = {
            "1. Vehicle List (Current in Slot)",
            "2. Revenue Report",
            "3. Occupancy Report",
            "4. Fine Report"
        };
        comboReportType = new JComboBox<>(reports);
        comboReportType.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        comboReportType.addActionListener(e -> generatePreview());
        
        JButton btnExportFile = new JButton("Export / Print Report");
        btnExportFile.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnExportFile.setBackground(new Color(0, 120, 215)); 
        btnExportFile.setForeground(Color.BLACK);
        btnExportFile.setFocusPainted(false);
        btnExportFile.addActionListener(e -> showExportDialog());
        
        row1.add(lblSelect);
        row1.add(comboReportType);
        row1.add(btnExportFile);
        
        // 1.2 第二行：日期筛选栏
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        row2.setBackground(Color.WHITE);
        row2.setBorder(BorderFactory.createTitledBorder("Filter by Date Range"));
        
        chkEnableFilter = new JCheckBox("Filter Data");
        chkEnableFilter.setFont(new Font("Segoe UI", Font.BOLD, 12));
        chkEnableFilter.setBackground(Color.WHITE);
        chkEnableFilter.addActionListener(e -> {
            btnDateRange.setEnabled(chkEnableFilter.isSelected());
            generatePreview();
        });
        
        // 触发日历的按钮
        btnDateRange = new JButton("📅 Select Date Range");
        btnDateRange.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnDateRange.setBackground(new Color(245, 245, 245));
        btnDateRange.setEnabled(false); 
        btnDateRange.addActionListener(e -> showCalendarPopup());
        
        // 当日历选择变动时，更新按钮文字并刷新报表
        datePicker.setOnSelectionChanged(() -> {
            btnDateRange.setText("📅 " + datePicker.getRangeText());
            generatePreview();
        });

        row2.add(chkEnableFilter);
        row2.add(btnDateRange);

        topContainer.add(row1);
        topContainer.add(row2);
        
        add(topContainer, BorderLayout.NORTH);
        
        // --- 2. 报告预览区 ---
        txtPreview = new JEditorPane();
        txtPreview.setContentType("text/html");
        txtPreview.setEditable(false);
        txtPreview.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
        
        add(new JScrollPane(txtPreview), BorderLayout.CENTER);
        generatePreview(); 
    }
    
    private void showCalendarPopup() {
        JDialog d = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), "Select Date Range", true);
        d.setLayout(new BorderLayout());
        d.add(datePicker, BorderLayout.CENTER);
        
        JButton btnDone = new JButton("Confirm Range");
        btnDone.setBackground(new Color(232, 243, 241)); 
        btnDone.setForeground(Color.BLACK); // 黑色字体
        btnDone.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDone.setFocusPainted(false);
        btnDone.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        btnDone.addActionListener(e -> d.dispose());
        
        d.add(btnDone, BorderLayout.SOUTH);
        d.pack(); 
        d.setLocationRelativeTo(btnDateRange);
        d.setVisible(true);
    }
    
    // 🌟 统一筛选逻辑
    private boolean isWithinRange(String dateStr) {
        if (!chkEnableFilter.isSelected()) return true;
        if (dateStr == null || dateStr.equals("-") || dateStr.equals("N/A")) return false;

        try {
            java.util.Date start = datePicker.getStartDate();
            java.util.Date end = datePicker.getEndDate();
            if (start == null) return true;

            // 统一尝试解析为标准格式
            java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            // 备用格式 (数据库原生格式)
            java.time.format.DateTimeFormatter fmtDB = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            
            java.time.LocalDateTime ldt = null;

            try { ldt = java.time.LocalDateTime.parse(dateStr, fmt); } 
            catch(Exception e1) {
                try { ldt = java.time.LocalDateTime.parse(dateStr, fmtDB); } catch(Exception e2) { return true; }
            }
            
            java.util.Date target = java.sql.Timestamp.valueOf(ldt);
            return !target.before(start) && !target.after(end);

        } catch (Exception e) { return true; }
    }

    // 🌟 生成预览 (已恢复所有 Column)
    private void generatePreview() {
        int index = comboReportType.getSelectedIndex();
        StringBuilder html = new StringBuilder();
        
        ParkingLotSystem sys = ParkingLotSystem.getInstance();
        List<ParkingSpot> allSpots = sys.getAllSpots();
        int totalSpots = allSpots.size();
        long occupiedCount = allSpots.stream().filter(ParkingSpot::isOccupied).count();
        double percentage = (totalSpots > 0) ? ((double) occupiedCount / totalSpots * 100) : 0;
        
        html.append("<html><body style='font-family:Segoe UI; margin:0; padding:0; width:100%;'>");
        
        // Report Header
        html.append("<div style='text-align:center; margin-top:10px; width:100%;'>");
        html.append("<h1 style='color:#2c3e50; margin:0; padding:0; font-size:24pt;'>UNIVERSITY PARKING SYSTEM REPORT</h1>");
        html.append("<hr style='border: 1px solid #000; margin: 5px 0;'>");
        html.append("</div>");
        
        // Info Section
        String genDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        html.append("<div style='font-size:11pt; margin-bottom:5px; padding-left:5px;'>"); 
        html.append("<b>Generated on:</b> ").append(genDate).append("<br>");
        html.append("<b>Generated By:</b> Admin").append("<br>");
        html.append("<b>Total Occupied Status:</b> ").append(String.format("%d / %d (%.2f%%)", occupiedCount, totalSpots, percentage));
        
        if (chkEnableFilter.isSelected()) {
            html.append("<br><b>Filter Range:</b> ").append(datePicker.getRangeText());
        }
        html.append("</div>");
        
        // Styles
        String tableStyle = "width:100%; border-collapse:collapse; font-size:10pt; border:1px solid #000; table-layout: fixed;";
        String thStyle = "border:1px solid #000; padding:4px; background-color:#ecf0f1; text-align:left; white-space: nowrap; overflow: hidden; font-weight:bold; color:black;";
        String tdStyle = "border:1px solid #000; padding:4px; white-space: nowrap; overflow: hidden;";
        
        if (index == 0) { // 🚙 Vehicle List (7 Columns)
            html.append("<h3 style='margin:5px 0;'>🚙 Current Vehicles in Slots</h3>");
            html.append("<table style='" + tableStyle + "'>");
            html.append("<tr>")
                .append("<th style='" + thStyle + " width:15%;'>Ticket No</th>") 
                .append("<th style='" + thStyle + " width:10%;'>Spot ID</th>")
                .append("<th style='" + thStyle + " width:10%;'>Type</th>")
                .append("<th style='" + thStyle + " width:10%;'>Vehicle</th>")
                .append("<th style='" + thStyle + " width:10%;'>Plate</th>")
                .append("<th style='" + thStyle + " width:15%;'>Entry Time</th>")
                .append("<th style='" + thStyle + " width:30%;'>Fine Rules</th>")
                .append("</tr>");
            
            for(ParkingSpot s : allSpots) { 
                if (s.isOccupied() && s.getCurrentVehicle() != null) {
                    String plate = s.getCurrentVehicle().getPlateNumber();
                    Ticket t = sys.getActiveTicket(plate);
                    String ticketNo = (t != null) ? t.getTicketId() : "N/A";
                    // 🌟 统一格式为 dd/MM/yyyy HH:mm:ss
                    String entryTime = (t != null) ? t.getEntryTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) : "-";
                    String fineRules = (t != null) ? ("Option " + (t.getLockedScheme() == FineScheme.FIXED ? "A" : t.getLockedScheme() == FineScheme.PROGRESSIVE ? "B" : "C") + " (Fixed)") : "Manual";

                    if (!isWithinRange(entryTime)) continue;

                    html.append("<tr>");
                    html.append("<td style='" + tdStyle + "'>").append(ticketNo).append("</td>");
                    html.append("<td style='" + tdStyle + "'>").append(s.getId()).append("</td>");
                    html.append("<td style='" + tdStyle + "'>").append(s.getType()).append("</td>");
                    html.append("<td style='" + tdStyle + "'>").append(s.getCurrentVehicle().getType()).append("</td>");
                    html.append("<td style='" + tdStyle + "'><b>").append(plate).append("</b></td>");
                    html.append("<td style='" + tdStyle + "'>").append(entryTime).append("</td>");
                    html.append("<td style='" + tdStyle + "'>").append(fineRules).append("</td>");
                    html.append("</tr>");
                }
            }
            html.append("</table>");
            
        } else if (index == 1) { // 💰 Revenue Report (11 Columns)
             html.append("<h3 style='margin:5px 0;'>💰 Revenue Report</h3>");
             List<String[]> trans = sys.getTransactionHistory();
             double totalFilteredRev = 0;
             
             html.append("<table style='" + tableStyle + "'>");
             html.append("<tr>")
                 .append("<th style='" + thStyle + " width:4%;'>No</th>")
                 .append("<th style='" + thStyle + " width:10%;'>Plate</th>")
                 .append("<th style='" + thStyle + " width:15%;'>Invoice ID</th>") 
                 .append("<th style='" + thStyle + " width:10%;'>Method</th>")
                 .append("<th style='" + thStyle + " width:12%;'>Pay Time</th>")
                 .append("<th style='" + thStyle + " width:12%;'>Entry Time</th>")
                 .append("<th style='" + thStyle + " width:8%;'>Duration</th>")
                 .append("<th style='" + thStyle + " width:12%;'>Exit Time</th>")
                 .append("<th style='" + thStyle + " width:8%;'>Park Amt</th>")
                 .append("<th style='" + thStyle + " width:8%;'>Fine Amt</th>")
                 .append("<th style='" + thStyle + " width:10%;'>Total (RM)</th>")
                 .append("</tr>");
             
             int count = 1;
             for(String[] t : trans) {
                 String payTime = t[4];
                 // 尝试格式化 Pay Time (如果数据库存的是 yyyy-MM-dd，这里转为 dd/MM/yyyy)
                 try {
                     LocalDateTime pt = LocalDateTime.parse(payTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                     payTime = pt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
                 } catch(Exception e) {} // 保持原样

                 if (!isWithinRange(payTime)) continue;

                 String total = (t.length > 2) ? t[2] : "0.00";
                 html.append("<tr>");
                 html.append("<td style='" + tdStyle + "'>").append(count++).append("</td>");
                 html.append("<td style='" + tdStyle + "'><b>").append(t[1]).append("</b></td>"); 
                 html.append("<td style='" + tdStyle + "'>").append(t[0]).append("</td>");      
                 html.append("<td style='" + tdStyle + "'>").append(t[3]).append("</td>");      
                 html.append("<td style='" + tdStyle + "'>").append(payTime).append("</td>");      
                 html.append("<td style='" + tdStyle + "'>").append(t[5]).append("</td>");      
                 html.append("<td style='" + tdStyle + "'>").append(t[6]).append("</td>");      
                 html.append("<td style='" + tdStyle + "'>").append(payTime).append("</td>");      
                 html.append("<td style='" + tdStyle + "'>RM ").append(t[7]).append("</td>");   
                 html.append("<td style='" + tdStyle + "'>RM ").append(t[8]).append("</td>");   
                 html.append("<td style='" + tdStyle + "'><b>RM ").append(total).append("</b></td>");
                 html.append("</tr>");
                 try { totalFilteredRev += Double.parseDouble(total); } catch(Exception e){}
             }
             html.append("</table>");
             html.append("<h3 style='text-align:right; margin-top:5px;'>Total Collected: RM ").append(String.format("%,.2f", totalFilteredRev)).append("</h3>");

        } else if (index == 2) { // 🅿 Occupancy
             html.append("<h3 style='margin:5px 0;'>🅿 Occupancy Summary</h3>");
             html.append("<ul><li><b>Total Spots:</b> ").append(totalSpots).append("</li>")
                 .append("<li><b>Occupied:</b> ").append(occupiedCount).append("</li>")
                 .append("<li><b>Available:</b> ").append(totalSpots - occupiedCount).append("</li>")
                 .append("<li><b>Occupancy Rate:</b> ").append(String.format("%.2f", percentage)).append("%</li></ul>");
                 
        } else if (index == 3) { // ⚠️ Fine Report (5 Columns)
             List<String[]> allFines = sys.getAllFines();
             List<String[]> unpaidList = new ArrayList<>();
             List<String[]> paidList = new ArrayList<>();
             double totalUnpaidAmt = 0.0;
             double totalPaidAmt = 0.0;

             for (String[] f : allFines) {
                 String fineDate = f[5];
                 if (!isWithinRange(fineDate)) continue;

                 String status = f[4]; 
                 double amount = 0.0;
                 try { amount = Double.parseDouble(f[2]); } catch(Exception e){}

                 if ("U".equalsIgnoreCase(status)) {
                     unpaidList.add(f);
                     totalUnpaidAmt += amount;
                 } else if ("P".equalsIgnoreCase(status)) {
                     paidList.add(f);
                     totalPaidAmt += amount;
                 }
             }

             html.append("<h2 style='margin:5px 0; color:#c0392b;'>⚠️ Fine Report System</h2>");
             
             html.append("<div style='background-color:#f9f9f9; border:1px solid #ccc; padding:10px; margin-bottom:15px;'>");
             html.append("<b>📊 Fine Summary:</b><br>");
             html.append("Total Unpaid Fines (Arrears): <b>RM ").append(String.format("%,.2f", totalUnpaidAmt)).append("</b><br>");
             html.append("Total Paid Fines (Collected): <b>RM ").append(String.format("%,.2f", totalPaidAmt)).append("</b>");
             html.append("</div>");

             // Unpaid Table
             html.append("<h3 style='color:red;'>❌ Unpaid Fines (Outstanding)</h3>");
             if (unpaidList.isEmpty()) {
                 html.append("<p><i>No unpaid fines found in range.</i></p>");
             } else {
                 html.append("<table style='" + tableStyle + "'>");
                 html.append("<tr style='background-color:#ffebee;'>")
                     .append("<th style='" + thStyle + " width:10%;'>ID</th>")
                     .append("<th style='" + thStyle + " width:15%;'>Plate</th>")
                     .append("<th style='" + thStyle + " width:15%;'>Amount</th>")
                     .append("<th style='" + thStyle + " width:35%;'>Remarks</th>")
                     .append("<th style='" + thStyle + " width:25%;'>Date Generated</th>")
                     .append("</tr>");
                 
                 for(String[] f : unpaidList) {
                     html.append("<tr>");
                     html.append("<td style='" + tdStyle + "'>").append(f[0]).append("</td>");
                     html.append("<td style='" + tdStyle + "'><b>").append(f[1]).append("</b></td>");
                     html.append("<td style='" + tdStyle + " color:red;'>RM ").append(f[2]).append("</td>");
                     html.append("<td style='" + tdStyle + "'>").append(f[3]).append("</td>");
                     html.append("<td style='" + tdStyle + "'>").append(f[5]).append("</td>");
                     html.append("</tr>");
                 }
                 html.append("</table>");
             }

             html.append("<br><hr><br>");

             // Paid Table
             html.append("<h3 style='color:green;'>✅ Paid Fines (History)</h3>");
             if (paidList.isEmpty()) {
                 html.append("<p><i>No paid fines history found in range.</i></p>");
             } else {
                 html.append("<table style='" + tableStyle + "'>");
                 html.append("<tr style='background-color:#e8f5e9;'>") 
                     .append("<th style='" + thStyle + " width:10%;'>ID</th>")
                     .append("<th style='" + thStyle + " width:15%;'>Plate</th>")
                     .append("<th style='" + thStyle + " width:15%;'>Amount</th>")
                     .append("<th style='" + thStyle + " width:35%;'>Remarks</th>")
                     .append("<th style='" + thStyle + " width:25%;'>Date Generated</th>")
                     .append("</tr>");
                 
                 for(String[] f : paidList) {
                     html.append("<tr>");
                     html.append("<td style='" + tdStyle + "'>").append(f[0]).append("</td>");
                     html.append("<td style='" + tdStyle + "'><b>").append(f[1]).append("</b></td>");
                     html.append("<td style='" + tdStyle + " color:green;'>RM ").append(f[2]).append("</td>");
                     html.append("<td style='" + tdStyle + "'>").append(f[3]).append("</td>");
                     html.append("<td style='" + tdStyle + "'>").append(f[5]).append("</td>");
                     html.append("</tr>");
                 }
                 html.append("</table>");
             }
        }
        
        html.append("<p style='text-align:center; font-size:9pt; margin-top:5px; color:#666;'>--- End of Report ---</p>");
        html.append("</body></html>");
        
        txtPreview.setText(html.toString());
        txtPreview.setCaretPosition(0);
    }

    private void showExportDialog() {
        String[] options = {"PDF (Landscape Print)", "CSV (Excel)"};
        int choice = JOptionPane.showOptionDialog(this, 
            "Please select the export format:", "Export File", 
            0, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            
        if (choice == 0) exportPdf();
        else if (choice == 1) exportCsv();
    }

    // 🌟🌟🌟 重点：恢复了 Landscape 设置 🌟🌟🌟
    private void exportPdf() {
        java.awt.print.PrinterJob job = java.awt.print.PrinterJob.getPrinterJob();
        java.awt.print.PageFormat pf = job.defaultPage();
        
        // 强制横向打印
        pf.setOrientation(java.awt.print.PageFormat.LANDSCAPE); 
        
        java.awt.print.Paper paper = new java.awt.print.Paper();
        // A4 尺寸 (Point)
        double w = 595, h = 842; 
        paper.setSize(w, h);
        double margin = 10; 
        paper.setImageableArea(margin, margin, w - 2 * margin, h - 2 * margin);
        pf.setPaper(paper);
        
        job.setPrintable(txtPreview.getPrintable(null, new java.text.MessageFormat("- Page {0} -")), job.validatePage(pf));
        
        if (job.printDialog()) {
            try { job.print(); } catch (Exception e) { e.printStackTrace(); }
        }
    }

    private void exportCsv() {
        int index = comboReportType.getSelectedIndex();
        if (index != 0 && index != 1) {
            JOptionPane.showMessageDialog(this, "CSV Export available for List and Revenue only.");
            return;
        }
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV Files", "csv"));
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getAbsolutePath();
            if(!path.toLowerCase().endsWith(".csv")) path += ".csv";
            try (java.io.OutputStreamWriter writer = new java.io.OutputStreamWriter(new java.io.FileOutputStream(path), java.nio.charset.StandardCharsets.UTF_8)) {
                writer.write("\uFEFF"); 
                ParkingLotSystem sys = ParkingLotSystem.getInstance();
                if (index == 0) { 
                    writer.write("Ticket No,Spot ID,Type,Vehicle,Plate,Entry Time,Fine Rules\n");
                    for(ParkingSpot s : sys.getAllSpots()) {
                        if (s.isOccupied() && s.getCurrentVehicle() != null) {
                            String plate = s.getCurrentVehicle().getPlateNumber();
                            Ticket t = sys.getActiveTicket(plate);
                            String entryTime = (t != null) ? t.getEntryTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")) : "-";
                            writer.write(String.format("%s,%s,%s,%s,%s,%s,%s\n", 
                                (t!=null?t.getTicketId():"N/A"), s.getId(), s.getType(), s.getCurrentVehicle().getType(), plate, entryTime, "Option A (Fixed)"));
                        }
                    }
                } else { 
                    writer.write("No,Plate,Invoice ID,Method,Pay Time,Entry Time,Duration,Exit Time,Park Amt,Fine Amt,Total (RM)\n");
                    List<String[]> trans = sys.getTransactionHistory();
                    int count = 1;
                    for(String[] t : trans) {
                        writer.write(String.format("%d,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n", 
                            count++, t[1], t[0], t[3], t[4], t[5], t[6], t[4], t[7], t[8], t[2]));
                    }
                }
                JOptionPane.showMessageDialog(this, "CSV Exported Successfully!");
            } catch (Exception ex) { ex.printStackTrace(); }
        }
    }
}
        public void updateState(VehicleType userType) {
            // Re-use logic to determine if suitable
            this.isSuitable = checkSuitability(userType);
            repaint();
        }
        
        private boolean checkSuitability(VehicleType userType) {
            if (spot.getStatus() != SpotStatus.AVAILABLE) return false;
            if (userType == VehicleType.HANDICAPPED) return true;
            if (userType == VehicleType.MOTORCYCLE && spot.getType() == SpotType.COMPACT) return true;
            if (userType == VehicleType.CAR && (spot.getType() == SpotType.COMPACT || spot.getType() == SpotType.REGULAR)) return true;
            if (userType == VehicleType.SUV_TRUCK && spot.getType() == SpotType.REGULAR) return true;
            if (spot.getType() == SpotType.RESERVED) return true;
            return false;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            // 1. Determine Floor Color based on Type
            Color floorColor = new Color(250, 250, 250); // Default White/Concrete
            Color lineColor = new Color(200, 200, 200);
            
            if (spot.getType() == SpotType.HANDICAPPED) { 
                floorColor = new Color(225, 245, 255); // Light Blue
                lineColor = new Color(100, 180, 255);
            } else if (spot.getType() == SpotType.RESERVED) {
                floorColor = new Color(255, 250, 220); // Light Yellow
                lineColor = new Color(240, 200, 100);
            }

            // 2. Draw Floor
            g2.setColor(floorColor);
            g2.fillRoundRect(2, 2, w-4, h-4, 10, 10);
            
            // 3. Draw Border Lines
            g2.setColor(lineColor);
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(2, 2, w-4, h-4, 10, 10);

            // 4. Draw Suitability Highlight (Green Glow)
            if (isSuitable && spot.getStatus() == SpotStatus.AVAILABLE) {
                g2.setColor(new Color(50, 200, 50, 100)); // Transparent Green
                g2.setStroke(new BasicStroke(3));
                g2.drawRoundRect(4, 4, w-8, h-8, 8, 8);
            }

            // 5. Draw Content based on Status
            if (spot.getStatus() == SpotStatus.OCCUPIED) {
                drawCar(g2, w, h);
                
                // Plate Number Tag
                String plate = (spot.getCurrentVehicle() != null) ? spot.getCurrentVehicle().getPlateNumber() : "BUSY";
                g2.setColor(new Color(0, 0, 0, 180));
                g2.fillRoundRect(5, h/2 - 10, w - 10, 20, 5, 5); // Centered tag
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 10)); // Smaller font for smaller buttons
                drawCenteredString(g2, plate, w, h);
                
            } else if (spot.getStatus() == SpotStatus.MAINTENANCE) {
                g2.setColor(Color.ORANGE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 24));
                drawCenteredString(g2, "🔧", w, h);
                
            } else if (spot.getStatus() == SpotStatus.INCORRECT) {
                g2.setColor(Color.DARK_GRAY);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 24));
                drawCenteredString(g2, "🚫", w, h);
                
            } else {
                // AVAILABLE: Show ID and Icon
                g2.setColor(new Color(150, 150, 150));
                g2.setFont(new Font("Arial", Font.BOLD, 12));
                
                String[] parts = spot.getId().split("-");
                if (parts.length >= 3) {
                     g2.drawString(parts[1] + "-" + parts[2], 5, 15); 
                } else {
                     g2.drawString(spot.getId(), 5, 15);
                }

                // Icons for special spots
                if (spot.getType() == SpotType.HANDICAPPED) {
                    g2.setFont(new Font("Segoe UI", Font.PLAIN, 28));
                    g2.setColor(new Color(100, 180, 255));
                    drawCenteredString(g2, "♿", w, h);
                } else if (spot.getType() == SpotType.RESERVED) {
                    g2.setFont(new Font("Segoe UI", Font.PLAIN, 28));
                    g2.setColor(new Color(240, 200, 0));
                    drawCenteredString(g2, "⭐", w, h);
                }
            }
        }
        
        // 🎨 Helper to draw a top-down car
        private void drawCar(Graphics2D g2, int w, int h) {
            int padding = 10; // Smaller padding
            int carW = w - (padding * 2);
            int carH = h - (padding * 2);
            int x = padding;
            int y = padding;
            
            // Determine Color
            Color carColor = new Color(230, 80, 80); // Red Car
            Vehicle v = spot.getCurrentVehicle();
            if (v != null && v.getType() == VehicleType.MOTORCYCLE) {
                 carColor = new Color(100, 100, 100); // Grey Bike
                 carW = w / 2; // Thinner
                 x = w / 4;
            }

            // Shadow
            g2.setColor(new Color(0,0,0,30));
            g2.fillRoundRect(x+3, y+3, carW, carH, 10, 10);

            // Body
            g2.setColor(carColor);
            g2.fillRoundRect(x, y, carW, carH, 10, 10);

            // Roof / Windshield (Top down view style)
            g2.setColor(new Color(40, 40, 40)); // Dark glass
            int glassMargin = 8;
            g2.fillRoundRect(x + 4, y + 10, carW - 8, 12, 4, 4); // Front Windshield
            g2.fillRoundRect(x + 4, y + carH - 18, carW - 8, 8, 4, 4); // Back Windshield
            
            // Roof Color
            g2.setColor(carColor.brighter());
            g2.fillRoundRect(x + 4, y + 24, carW - 8, carH - 44, 4, 4);
        }
        
        private void drawCenteredString(Graphics2D g, String text, int w, int h) {
            FontMetrics fm = g.getFontMetrics();
            int x = (w - fm.stringWidth(text)) / 2;
            int y = ((h - fm.getHeight()) / 2) + fm.getAscent();
            g.drawString(text, x, y);
        }
    }
    // 🌟🌟🌟 新增：自定义日期范围选择器 (仿现代 Web 风格) 🌟🌟🌟
// 🌟🌟🌟 修复版：自定义日期范围选择器 (完美复刻 UI) 🌟🌟🌟
// 🌟🌟🌟 最终修复版：自定义日期范围选择器 (手动绘制文字，杜绝省略号) 🌟🌟🌟
// 🌟🌟🌟 最终增强版：带输入框的双向绑定日历 🌟🌟🌟
// 🌟🌟🌟 最终完美版：双向绑定的日期选择器 🌟🌟🌟
// 🌟🌟🌟 最终修复版：解决点击冲突，完美支持 Start-End 选择 🌟🌟🌟
class DateRangePicker extends JPanel {
    private java.time.LocalDate currentMonth;
    private java.time.LocalDate selectedStart;
    private java.time.LocalDate selectedEnd;
    
    private JPanel daysPanel;
    private JLabel lblMonthYear;
    private Runnable onSelectionChanged; 
    
    private JTextField txtStartInput;
    private JTextField txtEndInput;
    private java.time.format.DateTimeFormatter inputFmt = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // 🎨 颜色定义
    private final Color COLOR_BG = Color.WHITE;
    private final Color COLOR_SELECTED_BG = new Color(15, 76, 76);   // 深绿色
    private final Color COLOR_RANGE_BG = new Color(232, 243, 241);   // 浅薄荷色
    private final Color COLOR_TEXT_SELECTED = Color.WHITE;
    private final Color COLOR_TEXT_NORMAL = Color.BLACK;

    public DateRangePicker() {
        this.currentMonth = java.time.LocalDate.now().withDayOfMonth(1);
        // 默认不选中任何日期，让用户从头开始选，或者您可以取消注释下面两行来默认选中今天
        this.selectedStart = java.time.LocalDate.now();
        this.selectedEnd = java.time.LocalDate.now();
        
        setLayout(new BorderLayout(0, 0));
        setBackground(COLOR_BG);
        setPreferredSize(new Dimension(360, 430)); 
        
        // --- 1. 顶部容器 ---
        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BoxLayout(topContainer, BoxLayout.Y_AXIS));
        topContainer.setBackground(COLOR_BG);
        
        // 1.1 输入框区域
        JPanel inputPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        inputPanel.setBackground(COLOR_BG);
        inputPanel.setBorder(new javax.swing.border.EmptyBorder(15, 15, 5, 15));
        
        txtStartInput = createDateTextField();
        txtEndInput = createDateTextField();
        
        inputPanel.add(createInputGroup("Start Date:", txtStartInput));
        inputPanel.add(createInputGroup("End Date:", txtEndInput));
        
        topContainer.add(inputPanel);
        
        // 1.2 月份导航
        JPanel navHeader = new JPanel(new BorderLayout());
        navHeader.setBackground(COLOR_BG);
        navHeader.setBorder(new javax.swing.border.EmptyBorder(10, 20, 10, 20));
        
        JButton btnPrev = createNavButton("<");
        JButton btnNext = createNavButton(">");
        lblMonthYear = new JLabel("", SwingConstants.CENTER);
        lblMonthYear.setFont(new Font("Segoe UI", Font.BOLD, 18));
        
        btnPrev.addActionListener(e -> changeMonth(-1));
        btnNext.addActionListener(e -> changeMonth(1));
        
        navHeader.add(btnPrev, BorderLayout.WEST);
        navHeader.add(lblMonthYear, BorderLayout.CENTER);
        navHeader.add(btnNext, BorderLayout.EAST);
        
        topContainer.add(navHeader);
        add(topContainer, BorderLayout.NORTH);
        
        // --- 2. 星期头 ---
        JPanel weekHeader = new JPanel(new GridLayout(1, 7, 0, 0));
        weekHeader.setBackground(COLOR_BG);
        weekHeader.setBorder(new javax.swing.border.EmptyBorder(0, 10, 5, 10));
        String[] weeks = {"Su", "Mo", "Tu", "We", "Th", "Fr", "Sa"};
        for (String w : weeks) {
            JLabel lbl = new JLabel(w, SwingConstants.CENTER);
            lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lbl.setForeground(Color.GRAY);
            weekHeader.add(lbl);
        }
        
        JPanel bodyPanel = new JPanel(new BorderLayout());
        bodyPanel.add(weekHeader, BorderLayout.NORTH);
        
        // --- 3. 日期网格 ---
        daysPanel = new JPanel(new GridLayout(0, 7, 0, 0)); 
        daysPanel.setBackground(COLOR_BG);
        daysPanel.setBorder(new javax.swing.border.EmptyBorder(0, 10, 10, 10));
        
        bodyPanel.add(daysPanel, BorderLayout.CENTER);
        add(bodyPanel, BorderLayout.CENTER);
        
        updateTextFieldsFromSelection(); 
        refreshCalendar();
    }

    // 🌟 修复：移除 FocusListener，避免点击日历时触发冲突
    private JTextField createDateTextField() {
        JTextField txt = new JTextField();
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setHorizontalAlignment(JTextField.CENTER);
        txt.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        // 只有按下 Enter 键时才手动更新，防止自动干扰日历点击
        txt.addActionListener(e -> syncCalendarFromInput());
        return txt;
    }

    private JPanel createInputGroup(String label, JTextField field) {
        JPanel p = new JPanel(new BorderLayout(0, 5));
        p.setBackground(COLOR_BG);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(Color.GRAY);
        p.add(lbl, BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    // 🌟 逻辑：从输入框同步到日历 (Manual Type -> Enter)
    private void syncCalendarFromInput() {
        try {
            String sText = txtStartInput.getText().trim();
            String eText = txtEndInput.getText().trim();
            
            // 只有当两个框都不为空时，才更新日历状态，否则视为无效输入不处理
            if (!sText.isEmpty() && !eText.isEmpty()) {
                selectedStart = java.time.LocalDate.parse(sText, inputFmt);
                selectedEnd = java.time.LocalDate.parse(eText, inputFmt);
                
                // 自动纠正顺序
                if (selectedStart.isAfter(selectedEnd)) {
                    java.time.LocalDate temp = selectedStart;
                    selectedStart = selectedEnd;
                    selectedEnd = temp;
                    updateTextFieldsFromSelection();
                }
                // 跳转月份
                currentMonth = selectedStart.withDayOfMonth(1);
                refreshCalendar();
                if (onSelectionChanged != null) onSelectionChanged.run();
            }
        } catch (Exception ex) {
            // Ignore invalid format
        }
    }

    // 🌟 逻辑：更新输入框文字
    private void updateTextFieldsFromSelection() {
        if (selectedStart != null) txtStartInput.setText(selectedStart.format(inputFmt));
        else txtStartInput.setText("");
        
        if (selectedEnd != null) txtEndInput.setText(selectedEnd.format(inputFmt));
        else txtEndInput.setText("");
    }

    // 🌟🌟🌟 核心修复：点击逻辑 🌟🌟🌟
    private void handleDateClick(java.time.LocalDate date) {
        // 情况 1: 还没选开始，或者已经选了一个完整区间 (Reset) -> 设为新的 Start
        if (selectedStart == null || (selectedStart != null && selectedEnd != null)) {
            selectedStart = date;
            selectedEnd = null; // 清空 End，等待第二次点击
        } 
        // 情况 2: 已经选了 Start，还没选 End -> 设为 End
        else if (selectedStart != null && selectedEnd == null) {
            // 如果点在 Start 之前，纠正为新的 Start
            if (date.isBefore(selectedStart)) {
                selectedStart = date;
            } else {
                // 正常情况：设为 End Date，完成选择
                selectedEnd = date;
            }
        }
        
        // 立即更新 UI
        updateTextFieldsFromSelection();
        refreshCalendar();
        if (onSelectionChanged != null) onSelectionChanged.run();
    }

    public void setOnSelectionChanged(Runnable callback) { this.onSelectionChanged = callback; }
    
    public java.util.Date getStartDate() {
        return (selectedStart == null) ? null : java.sql.Date.valueOf(selectedStart);
    }
    
    public java.util.Date getEndDate() {
        if (selectedEnd == null) return null;
        java.time.LocalDateTime endOfDay = selectedEnd.atTime(23, 59, 59);
        return java.sql.Timestamp.valueOf(endOfDay);
    }
    
    public String getRangeText() {
        if (selectedStart == null) return "Select Range";
        java.time.format.DateTimeFormatter fmt = java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy");
        String s = selectedStart.format(fmt);
        String e = (selectedEnd != null) ? selectedEnd.format(fmt) : s;
        return s + " - " + e;
    }

    private void changeMonth(int months) {
        currentMonth = currentMonth.plusMonths(months);
        refreshCalendar();
    }
    
    private JButton createNavButton(String text) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        b.setContentAreaFilled(false);
        b.setBorder(null);
        b.setFont(new Font("Segoe UI", Font.BOLD, 18));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void refreshCalendar() {
        daysPanel.removeAll();
        
        java.time.format.DateTimeFormatter fmtMonth = java.time.format.DateTimeFormatter.ofPattern("MMMM yyyy");
        lblMonthYear.setText(currentMonth.format(fmtMonth));
        
        java.time.LocalDate firstDay = currentMonth.withDayOfMonth(1);
        int daysInMonth = currentMonth.lengthOfMonth();
        int dayOfWeek = firstDay.getDayOfWeek().getValue(); 
        int emptySlots = (dayOfWeek == 7) ? 0 : dayOfWeek; 
        
        for (int i = 0; i < emptySlots; i++) {
            daysPanel.add(new JLabel(""));
        }
        
        for (int i = 1; i <= daysInMonth; i++) {
            java.time.LocalDate date = currentMonth.withDayOfMonth(i);
            String dayText = String.valueOf(i);
            
            JButton btn = new JButton(dayText) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    // 状态判断
                    boolean isStart = (selectedStart != null && date.equals(selectedStart));
                    boolean isEnd = (selectedEnd != null && date.equals(selectedEnd));
                    boolean isInRange = (selectedStart != null && selectedEnd != null && date.isAfter(selectedStart) && date.isBefore(selectedEnd));
                    
                    int w = getWidth();
                    int h = getHeight();

                    // 1. 绘制背景色
                    if (isInRange) {
                        g2.setColor(COLOR_RANGE_BG);
                        g2.fillRect(0, 0, w, h);
                    } else if (isStart) {
                        if (selectedEnd != null) {
                            g2.setColor(COLOR_RANGE_BG);
                            g2.fillRect(w/2, 0, w/2, h); // 向右连接
                        }
                        g2.setColor(COLOR_SELECTED_BG);
                        g2.fillRoundRect(2, 2, w-4, h-4, 10, 10);
                    } else if (isEnd) {
                        g2.setColor(COLOR_RANGE_BG);
                        g2.fillRect(0, 0, w/2, h); // 向左连接
                        g2.setColor(COLOR_SELECTED_BG);
                        g2.fillRoundRect(2, 2, w-4, h-4, 10, 10);
                    } else if (isStart && selectedEnd == null) {
                        // 只有一个点选中
                        g2.setColor(COLOR_SELECTED_BG);
                        g2.fillRoundRect(2, 2, w-4, h-4, 10, 10);
                    }

                    // 2. 绘制文字
                    boolean isSelected = isStart || isEnd;
                    g2.setFont(new Font("Segoe UI", isSelected ? Font.BOLD : Font.PLAIN, 14));
                    g2.setColor(isSelected ? COLOR_TEXT_SELECTED : COLOR_TEXT_NORMAL);
                    
                    FontMetrics fm = g2.getFontMetrics();
                    int textW = fm.stringWidth(dayText);
                    int textH = fm.getAscent();
                    g2.drawString(dayText, (w - textW) / 2, (h + textH) / 2 - 2);
                }
            };
            
            btn.setFocusPainted(false);
            btn.setBorderPainted(false);
            btn.setContentAreaFilled(false);
            btn.setMargin(new Insets(0,0,0,0)); 
            btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btn.setPreferredSize(new Dimension(45, 40)); 
            
            // 🌟 绑定点击事件
            btn.addActionListener(e -> handleDateClick(date));
            daysPanel.add(btn);
        }
        
        daysPanel.revalidate();
        daysPanel.repaint();
    }
}
}