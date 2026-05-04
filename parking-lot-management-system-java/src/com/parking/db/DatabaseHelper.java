package com.parking.db;

import com.parking.model.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper {
    private static final String URL = "jdbc:sqlite:parking_system.db";
    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance() {
        if (instance == null) {
            instance = new DatabaseHelper();
        }
        return instance;
    }

    public DatabaseHelper() {
        createTables();
        checkAndAddColumns();
    }

    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
        return conn;
    }

    private void createTables() {
        String sqlSpots = "CREATE TABLE IF NOT EXISTS spots (id TEXT PRIMARY KEY, floor INTEGER, row INTEGER, num INTEGER, type TEXT, status TEXT, vehicle_plate TEXT, vehicle_type TEXT, is_handicapped INTEGER)";
        String sqlTickets = "CREATE TABLE IF NOT EXISTS tickets (ticket_id TEXT PRIMARY KEY, spot_id TEXT, plate_number TEXT, entry_time TEXT, fine_scheme TEXT, vehicle_type TEXT, is_handicapped INTEGER, parking_rate REAL, type_letter TEXT)";
        String sqlRevenue = "CREATE TABLE IF NOT EXISTS revenue (total REAL)";
        String sqlFines = "CREATE TABLE IF NOT EXISTS fines (id INTEGER PRIMARY KEY AUTOINCREMENT, plate TEXT, amount REAL, remarks TEXT, fine_type_id INTEGER, created_at TEXT, created_by TEXT, last_updated_date TEXT, last_updated_by TEXT, void_reason TEXT, status TEXT, invoice_id TEXT, payment_time TEXT)";
        String sqlTrans = "CREATE TABLE IF NOT EXISTS transactions (id INTEGER PRIMARY KEY AUTOINCREMENT, invoice_id TEXT, plate TEXT, total_amount REAL, customer_paid REAL, remaining_balance REAL, method TEXT, payment_time TEXT, entry_time TEXT, duration TEXT, parking_fee REAL, fine_amount REAL, spot_type TEXT, spot_id TEXT, vehicle_type TEXT)";
        String sqlConfig = "CREATE TABLE IF NOT EXISTS system_config (key TEXT PRIMARY KEY, value TEXT)";
        String sqlLogs = "CREATE TABLE IF NOT EXISTS admin_logs (id INTEGER PRIMARY KEY AUTOINCREMENT, action_type TEXT, detail TEXT, timestamp TEXT)";
        String sqlAdmins = "CREATE TABLE IF NOT EXISTS admins (admin_id INTEGER PRIMARY KEY AUTOINCREMENT, admin_name TEXT UNIQUE, password TEXT)";
        String sqlFineTypes = "CREATE TABLE IF NOT EXISTS fine_types (fine_id INTEGER PRIMARY KEY AUTOINCREMENT, fine_type TEXT, fine_amount REAL, created_date TEXT, created_by TEXT, last_updated_date TEXT, last_updated_by TEXT)";

        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            stmt.execute(sqlSpots); stmt.execute(sqlTickets); stmt.execute(sqlRevenue);
            stmt.execute(sqlFines); stmt.execute(sqlTrans); stmt.execute(sqlConfig);
            stmt.execute(sqlLogs); stmt.execute(sqlAdmins); stmt.execute(sqlFineTypes);
            ResultSet rs = stmt.executeQuery("SELECT count(*) FROM revenue");
            if (rs.next() && rs.getInt(1) == 0) { stmt.execute("INSERT INTO revenue(total) VALUES(0.0)"); }
            insertDefaultAdmin(conn);
            insertDefaultFineType(conn);
        } catch (SQLException e) { System.out.println("Create tables error: " + e.getMessage()); }
    }

    private void checkAndAddColumns() {
        try (Connection conn = connect(); Statement stmt = conn.createStatement()) {
            try { stmt.execute("ALTER TABLE transactions ADD COLUMN spot_id TEXT"); } catch (SQLException e) {}
            try { stmt.execute("ALTER TABLE transactions ADD COLUMN vehicle_type TEXT"); } catch (SQLException e) {}
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void logTransaction(String invoiceId, String plate, double totalAmount, String method, 
                               String entryTime, String duration, double parkingFee, 
                               double fineAmount, String spotType, double paid, double bal,
                               String spotId, String vehicleType) { 
        String sql = "INSERT INTO transactions(invoice_id, plate, total_amount, customer_paid, remaining_balance, method, payment_time, entry_time, duration, parking_fee, fine_amount, spot_type, spot_id, vehicle_type) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, invoiceId); pstmt.setString(2, plate); pstmt.setDouble(3, totalAmount);
            pstmt.setDouble(4, paid); pstmt.setDouble(5, bal); pstmt.setString(6, method);
            pstmt.setString(7, LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            pstmt.setString(8, entryTime); pstmt.setString(9, duration); pstmt.setDouble(10, parkingFee);
            pstmt.setDouble(11, fineAmount); pstmt.setString(12, spotType);
            pstmt.setString(13, spotId); pstmt.setString(14, vehicleType); 
            pstmt.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public String[] getTransactionByInvoiceId(String invoiceId) {
        String sql = "SELECT * FROM transactions WHERE invoice_id = ?";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, invoiceId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String[] row = new String[14]; 
                row[0] = rs.getString("invoice_id"); row[1] = rs.getString("plate");
                row[2] = String.format("%.2f", rs.getDouble("total_amount"));
                row[3] = String.format("%.2f", rs.getDouble("customer_paid"));
                row[4] = String.format("%.2f", rs.getDouble("remaining_balance"));
                row[5] = rs.getString("method"); row[6] = rs.getString("payment_time");
                row[7] = rs.getString("entry_time"); row[8] = rs.getString("duration");
                row[9] = String.format("%.2f", rs.getDouble("parking_fee"));
                row[10] = String.format("%.2f", rs.getDouble("fine_amount"));
                row[11] = rs.getString("spot_type");
                row[12] = rs.getString("spot_id"); row[13] = rs.getString("vehicle_type"); 
                return row;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    public List<String[]> getPaidFinesByInvoiceId(String invoiceId) {
        List<String[]> list = new ArrayList<>();
        String sql = "SELECT created_at, remarks, amount FROM fines WHERE invoice_id = ? AND status = 'P'";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, invoiceId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new String[]{ rs.getString("created_at"), rs.getString("remarks"), String.format("%.2f", rs.getDouble("amount")) });
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    // --- 现有方法保持不变 ---
    public int authenticateAdmin(String username, String password) { String sql = "SELECT admin_id FROM admins WHERE admin_name = ? AND password = ?"; try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) { pstmt.setString(1, username); pstmt.setString(2, password); ResultSet rs = pstmt.executeQuery(); if (rs.next()) return rs.getInt("admin_id"); } catch (SQLException e) { e.printStackTrace(); } return -1; }
    public void addFineType(String reason, double amount, int adminId) { String sql = "INSERT INTO fine_types (fine_type, fine_amount, created_date, created_by, last_updated_date, last_updated_by) VALUES (?, ?, ?, ?, ?, ?)"; String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")); try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) { pstmt.setString(1, reason); pstmt.setDouble(2, amount); pstmt.setString(3, now); pstmt.setString(4, String.valueOf(adminId)); pstmt.setString(5, now); pstmt.setString(6, String.valueOf(adminId)); pstmt.executeUpdate(); } catch (SQLException e) { e.printStackTrace(); } }
    public Map<String, Double> getFineTypesMap() { Map<String, Double> map = new HashMap<>(); String sql = "SELECT fine_type, fine_amount FROM fine_types"; try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) { while (rs.next()) { map.put(rs.getString("fine_type"), rs.getDouble("fine_amount")); } } catch (SQLException e) { e.printStackTrace(); } return map; }
    public void initSpotsIfEmpty(List<ParkingSpot> spots) { try (Connection conn = connect(); Statement stmt = conn.createStatement()) { ResultSet rs = stmt.executeQuery("SELECT count(*) FROM spots"); if (rs.next() && rs.getInt(1) == 0) { String sql = "INSERT INTO spots(id, floor, row, num, type, status) VALUES(?,?,?,?,?,?)"; try (PreparedStatement pstmt = conn.prepareStatement(sql)) { for (ParkingSpot s : spots) { pstmt.setString(1, s.getId()); pstmt.setInt(2, s.getFloor()); pstmt.setInt(3, s.getRow()); pstmt.setInt(4, s.getNumber()); String typeToSave = s.getType().toString(); if ((s.getRow() == 5 || s.getRow() == 6) && (s.getNumber() >= 1 && s.getNumber() <= 4)) { typeToSave = "RESERVED"; } pstmt.setString(5, typeToSave); pstmt.setString(6, "AVAILABLE"); pstmt.addBatch(); } pstmt.executeBatch(); } } } catch (SQLException e) { e.printStackTrace(); } }
    public List<ParkingSpot> loadSpots() { List<ParkingSpot> list = new ArrayList<>(); String sql = "SELECT * FROM spots"; try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) { while (rs.next()) { ParkingSpot spot = new ParkingSpot(rs.getInt("floor"), rs.getInt("row"), rs.getInt("num"), SpotType.valueOf(rs.getString("type"))); spot.setStatus(SpotStatus.fromCode(rs.getString("status"))); if (spot.getStatus() == SpotStatus.OCCUPIED) { spot.park(new Vehicle(rs.getString("vehicle_plate"), VehicleType.valueOf(rs.getString("vehicle_type")), rs.getInt("is_handicapped") == 1)); } list.add(spot); } } catch (SQLException e) { e.printStackTrace(); } return list; }
    public void updateSpot(ParkingSpot spot) { String sql = "UPDATE spots SET status = ?, vehicle_plate = ?, vehicle_type = ?, is_handicapped = ? WHERE id = ?"; try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) { pstmt.setString(1, spot.getStatus().getCode()); if (spot.getStatus() == SpotStatus.OCCUPIED && spot.getCurrentVehicle() != null) { pstmt.setString(2, spot.getCurrentVehicle().getPlateNumber()); pstmt.setString(3, spot.getCurrentVehicle().getType().toString()); pstmt.setInt(4, spot.getCurrentVehicle().isHasHandicappedCard() ? 1 : 0); } else { pstmt.setString(2, null); pstmt.setString(3, null); pstmt.setInt(4, 0); } pstmt.setString(5, spot.getId()); pstmt.executeUpdate(); } catch (SQLException e) { e.printStackTrace(); } }
    public void saveTicket(Ticket t) { String sql = "INSERT INTO tickets(ticket_id, spot_id, plate_number, entry_time, fine_scheme, vehicle_type, is_handicapped, parking_rate, type_letter) VALUES(?,?,?,?,?,?,?,?,?)"; try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) { pstmt.setString(1, t.getTicketId()); pstmt.setString(2, t.getSpotId()); pstmt.setString(3, t.getPlateNumber()); pstmt.setString(4, t.getEntryTime().toString()); pstmt.setString(5, t.getLockedScheme().name()); pstmt.setString(6, t.getVehicleType().toString()); pstmt.setInt(7, t.isHandicapped() ? 1 : 0); pstmt.setDouble(8, t.getParkingRate()); pstmt.setString(9, t.getTicketId().substring(0, 1)); pstmt.executeUpdate(); } catch (SQLException e) { e.printStackTrace(); } }
    public void deleteTicket(String plateNumber) { String sql = "DELETE FROM tickets WHERE plate_number = ?"; try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) { pstmt.setString(1, plateNumber); pstmt.executeUpdate(); } catch (SQLException e) { e.printStackTrace(); } }
    public List<Ticket> loadActiveTickets() { List<Ticket> list = new ArrayList<>(); String sql = "SELECT * FROM tickets"; try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) { while (rs.next()) { Ticket t = new Ticket(rs.getString("spot_id"), rs.getString("plate_number"), VehicleType.valueOf(rs.getString("vehicle_type")), rs.getInt("is_handicapped") == 1, rs.getDouble("parking_rate"), rs.getString("type_letter")); t.setEntryTime(LocalDateTime.parse(rs.getString("entry_time"))); t.setLockedScheme(FineScheme.valueOf(rs.getString("fine_scheme"))); list.add(t); } } catch (SQLException e) { e.printStackTrace(); } return list; }
    public void updateRevenue(double amountToAdd) { String sql = "UPDATE revenue SET total = total + ?"; try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) { pstmt.setDouble(1, amountToAdd); pstmt.executeUpdate(); } catch (SQLException e) { e.printStackTrace(); } }
    public double getRevenue() { String sql = "SELECT total FROM revenue"; try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) { if (rs.next()) return rs.getDouble("total"); } catch (SQLException e) { e.printStackTrace(); } return 0.0; }
    public void saveConfig(String key, String value) { String sql = "REPLACE INTO system_config(key, value) VALUES(?,?)"; try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) { pstmt.setString(1, key); pstmt.setString(2, value); pstmt.executeUpdate(); } catch (SQLException e) { e.printStackTrace(); } }
    public String getConfig(String key) { String sql = "SELECT value FROM system_config WHERE key = ?"; try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) { pstmt.setString(1, key); ResultSet rs = pstmt.executeQuery(); if (rs.next()) return rs.getString("value"); } catch (SQLException e) { e.printStackTrace(); } return null; }
    public void logAdminAction(String actionType, String detail) { String sql = "INSERT INTO admin_logs(action_type, detail, timestamp) VALUES(?,?,?)"; try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) { pstmt.setString(1, actionType); pstmt.setString(2, detail); String time = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")); pstmt.setString(3, time); pstmt.executeUpdate(); } catch (SQLException e) { e.printStackTrace(); } }
    public List<String[]> getAdminLogs() { List<String[]> list = new ArrayList<>(); String sql = "SELECT * FROM admin_logs ORDER BY id DESC"; try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) { while (rs.next()) { list.add(new String[]{rs.getString("timestamp"), rs.getString("detail")}); } } catch (SQLException e) { e.printStackTrace(); } return list; }
    public List<String[]> getTransactionHistory() { List<String[]> list = new ArrayList<>(); String sql = "SELECT * FROM transactions ORDER BY id DESC"; try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) { while (rs.next()) { String[] row = new String[10]; row[0] = rs.getString("invoice_id"); row[1] = rs.getString("plate"); row[2] = String.format("%.2f", rs.getDouble("total_amount")); row[3] = rs.getString("method"); row[4] = rs.getString("payment_time"); row[5] = rs.getString("entry_time"); row[6] = rs.getString("duration"); row[7] = String.format("%.2f", rs.getDouble("parking_fee")); row[8] = String.format("%.2f", rs.getDouble("fine_amount")); row[9] = rs.getString("spot_type"); list.add(row); } } catch (SQLException e) { e.printStackTrace(); } return list; }
    public void markSpecificFinesAsPaid(List<Integer> fineIds, String invoiceId) { if (fineIds.isEmpty()) return; StringBuilder sql = new StringBuilder("UPDATE fines SET status = 'P', invoice_id = ?, payment_time = ? WHERE id IN ("); for (int i = 0; i < fineIds.size(); i++) { sql.append(i == 0 ? "?" : ", ?"); } sql.append(")"); try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql.toString())) { pstmt.setString(1, invoiceId); pstmt.setString(2, LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))); for (int i = 0; i < fineIds.size(); i++) { pstmt.setInt(i + 3, fineIds.get(i)); } pstmt.executeUpdate(); } catch (SQLException e) { e.printStackTrace(); } }
    public void markFineAsPaidById(int fineId, String invoiceId) { String sql = "UPDATE fines SET status = 'P', invoice_id = ?, payment_time = ? WHERE id = ?"; try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) { pstmt.setString(1, invoiceId); pstmt.setString(2, LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))); pstmt.setInt(3, fineId); pstmt.executeUpdate(); } catch (SQLException e) { e.printStackTrace(); } }
    public List<String[]> getAllFines() { List<String[]> list = new ArrayList<>(); String sql = "SELECT id, plate, amount, remarks, status, created_at FROM fines ORDER BY id DESC"; try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) { while (rs.next()) { list.add(new String[]{String.valueOf(rs.getInt("id")), rs.getString("plate"), String.format("%.2f", rs.getDouble("amount")), rs.getString("remarks"), rs.getString("status"), rs.getString("created_at")}); } } catch (SQLException e) { e.printStackTrace(); } return list; }
    public List<String[]> getUnpaidFinesForPlateWithId(String plate) { List<String[]> list = new ArrayList<>(); String sql = "SELECT id, amount, remarks, created_at FROM fines WHERE plate = ? AND status = 'U'"; try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) { pstmt.setString(1, plate); ResultSet rs = pstmt.executeQuery(); while (rs.next()) { list.add(new String[]{String.valueOf(rs.getInt("id")), String.valueOf(rs.getDouble("amount")), rs.getString("remarks"), rs.getString("created_at")}); } } catch (SQLException e) { e.printStackTrace(); } return list; }
    public List<String[]> getUnpaidFinesForPlate(String plate) { return getUnpaidFinesForPlateWithId(plate); } 
    public void addFine(String plate, double amount, String remarks, int adminId) { String sql = "INSERT INTO fines (plate, amount, remarks, created_at, created_by, status) VALUES (?, ?, ?, ?, ?, 'U')"; try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) { pstmt.setString(1, plate); pstmt.setDouble(2, amount); pstmt.setString(3, remarks); pstmt.setString(4, LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))); pstmt.setString(5, String.valueOf(adminId)); pstmt.executeUpdate(); } catch (SQLException e) { e.printStackTrace(); } }
    public void voidFine(int fineId, String voidReason, int adminId) { String sql = "UPDATE fines SET status = 'V', void_reason = ?, last_updated_by = ?, last_updated_date = ? WHERE id = ?"; String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")); try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) { pstmt.setString(1, voidReason); pstmt.setString(2, String.valueOf(adminId)); pstmt.setString(3, now); pstmt.setInt(4, fineId); pstmt.executeUpdate(); } catch (SQLException e) { e.printStackTrace(); } }
    public double getTotalRevenue() { double total = 0.0; String sql = "SELECT SUM(total_amount) FROM transactions"; try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) { if (rs.next()) total = rs.getDouble(1); } catch (SQLException e) { e.printStackTrace(); } return total; }
    private void insertDefaultAdmin(Connection conn) { try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT count(*) FROM admins")) { if (rs.next() && rs.getInt(1) == 0) { stmt.execute("INSERT INTO admins (admin_name, password) VALUES ('admin', '1234')"); } } catch (SQLException e) { e.printStackTrace(); } }
    private void insertDefaultFineType(Connection conn) { try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT count(*) FROM fine_types")) { if (rs.next() && rs.getInt(1) == 0) { stmt.execute("INSERT INTO fine_types (fine_id, fine_type, fine_amount) VALUES (1, 'Reserved Spot without a Reservation', 100.00)"); stmt.execute("INSERT INTO fine_types (fine_id, fine_type, fine_amount, created_by, last_updated_by) VALUES (2, 'Overstay', NULL, 'System', 'System')"); } } catch (SQLException e) { e.printStackTrace(); } }
    public List<String[]> getAllUnpaidFinesList() { List<String[]> list = new ArrayList<>(); String sql = "SELECT id, plate, amount, remarks, created_at FROM fines WHERE status = 'U'"; try (Connection conn = connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) { while (rs.next()) { list.add(new String[]{ String.valueOf(rs.getInt("id")), rs.getString("plate"), String.valueOf(rs.getDouble("amount")), rs.getString("remarks"), rs.getString("created_at") }); } } catch (SQLException e) { e.printStackTrace(); } return list; }
}