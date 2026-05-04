package com.parking.logic;

import com.parking.db.DatabaseHelper;
import com.parking.model.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ParkingLotSystem {
    private static ParkingLotSystem instance;
    private List<ParkingSpot> spots;
    private List<Ticket> activeTickets; 
    private DatabaseHelper db; 
    private FineScheme currentFineScheme; 
    private int loggedInAdminId = -1; 

    private ParkingLotSystem() {
        db = DatabaseHelper.getInstance(); 
        String savedScheme = db.getConfig("FINE_SCHEME");
        if (savedScheme != null) {
            try { currentFineScheme = FineScheme.valueOf(savedScheme); } 
            catch (Exception e) { currentFineScheme = FineScheme.FIXED; }
        } else { currentFineScheme = FineScheme.FIXED; }
        loadData(); // 🌟 初始化数据
    }

    public static synchronized ParkingLotSystem getInstance() {
        if (instance == null) instance = new ParkingLotSystem();
        return instance;
    }

    // 🌟 新增：重新从数据库加载数据（解决红线）
    public void loadData() {
        this.spots = db.loadSpots();
        this.activeTickets = db.loadActiveTickets();
        if (spots.isEmpty()) { 
            initializeDefaultStructure(); 
            db.initSpotsIfEmpty(spots); 
        }
    }

    public void processExit(String plateNumber, double totalAmount, String paymentMethod,
                            String invoiceId, String entryTimeStr, String durationStr, 
                            double parkingFee, double overstayFine, String spotType,
                            List<Integer> paidFineIds, boolean isOverstayPaid, 
                            double customerPaid, double balance) {
        
        Ticket t = getActiveTicket(plateNumber);
        if (t != null) {
            ParkingSpot s = getSpotById(t.getSpotId());
            if (s != null) {
                for (int fineId : paidFineIds) { db.markFineAsPaidById(fineId, invoiceId); }
                if (overstayFine > 0 && !isOverstayPaid) { addFine(plateNumber, overstayFine, "Overstay Penalty (Unpaid)"); }

                // 🌟 关键：传入全部 13 个参数，包含 Spot ID 和 Vehicle Type
                db.logTransaction(invoiceId, plateNumber, totalAmount, paymentMethod, 
                                  entryTimeStr, durationStr, parkingFee, overstayFine, 
                                  spotType, customerPaid, balance, 
                                  t.getSpotId(), t.getVehicleType().toString());

                s.removeVehicle();
                activeTickets.remove(t);
                db.updateSpot(s);
                db.deleteTicket(plateNumber);
                db.updateRevenue(totalAmount);
                loadData(); // 🌟 刷新内存
            }
        }
    }

    public double calculateFee(Ticket ticket, LocalDateTime exitTime) {
        ParkingSpot spot = getSpotById(ticket.getSpotId());
        long hours = (long) Math.ceil(Duration.between(ticket.getEntryTime(), exitTime).toMinutes() / 60.0);
        if (hours == 0) hours = 1; 
        double hourlyRate = spot.getHourlyRate(); 
        if (spot.getType() == SpotType.HANDICAPPED) { hourlyRate = 2.0; }
        Vehicle v = spot.getCurrentVehicle();
        boolean hasCard = (v != null) ? v.isHasHandicappedCard() : ticket.isHandicapped();
        if (hasCard) { hourlyRate = hourlyRate - 2.0; }
        if (hourlyRate < 0) { hourlyRate = 0.0; }
        return hours * hourlyRate;
    }

    public double calculateFine(Ticket ticket, LocalDateTime exitTime) {
        double fine = 0.0;
        long hours = (long) Math.ceil(Duration.between(ticket.getEntryTime(), exitTime).getSeconds() / 3600.0);
        ParkingSpot spot = getSpotById(ticket.getSpotId());
        FineScheme schemeToUse = ticket.getLockedScheme();
        if (hours > 24) {
            switch (schemeToUse) {
                case FIXED: fine = 50.0; break;
                case PROGRESSIVE: fine = 50.0; if (hours > 48) fine += 100.0; if (hours > 72) fine += 150.0; if (hours > 96) fine += 200.0; break;
                case HOURLY: fine = (hours - 24) * 20.0; break;
            }
            if (spot.getType() == SpotType.RESERVED) { fine += 100.0; }
        }
        return fine;
    }

    // --- 现有逻辑保持不变 ---
    public boolean login(String username, String password) { int adminId = db.authenticateAdmin(username, password); if (adminId != -1) { this.loggedInAdminId = adminId; db.logAdminAction("LOGIN", "Admin [" + username + "] (ID:" + adminId + ") logged in."); return true; } return false; }
    public void logout() { this.loggedInAdminId = -1; }
    public void updateSpotStatus(String spotId, SpotStatus newStatus) { ParkingSpot spot = getSpotById(spotId); if (spot != null) { SpotStatus old = spot.getStatus(); spot.setStatus(newStatus); if (newStatus == SpotStatus.AVAILABLE) { spot.removeVehicle(); } db.updateSpot(spot); db.logAdminAction("UPDATE_SPOT_STATUS", "Spot " + spotId + " changed from " + old + " to " + newStatus); } }
    public void createFineType(String reason, double amount) { if (loggedInAdminId != -1) { db.addFineType(reason, amount, loggedInAdminId); db.logAdminAction("ADD_FINE_TYPE", "Added fine type: " + reason + " (RM " + amount + ")"); } }
    public Map<String, Double> getFineTypes() { return db.getFineTypesMap(); }
    public void addFine(String plate, double amount, String remarks) { int adminId = (loggedInAdminId != -1) ? loggedInAdminId : 0; db.addFine(plate, amount, remarks, adminId); db.logAdminAction("MANUAL_FINE", "Added RM " + amount + " to " + plate + ". Remark: " + remarks + " (Admin ID: " + adminId + ")"); }
    public void voidFine(int fineId, String voidReason) { if (loggedInAdminId != -1) { db.voidFine(fineId, voidReason, loggedInAdminId); db.logAdminAction("VOID_FINE", "Voided Fine ID " + fineId + ". Reason: " + voidReason + " (Admin ID: " + loggedInAdminId + ")"); } }
    private void initializeDefaultStructure() { spots = new ArrayList<>(); for (int f = 1; f <= 5; f++) { for (int r = 1; r <= 6; r++) { for (int s = 1; s <= 8; s++) { SpotType type = SpotType.REGULAR; if (r == 1) { type = SpotType.COMPACT; } else if (r == 6 && s >= 7) { type = SpotType.HANDICAPPED; } else if (r == 6 && s <= 2) { type = SpotType.RESERVED; } spots.add(new ParkingSpot(f, r, s, type)); } } } }
    public List<ParkingSpot> findAvailableSpots(VehicleType vType) { return spots.stream().filter(spot -> spot.isAvailableForParking() && isSpotSuitable(spot, vType)).collect(Collectors.toList()); }
    private boolean isSpotSuitable(ParkingSpot spot, VehicleType vType) { if (vType == VehicleType.HANDICAPPED) return true; if (vType == VehicleType.MOTORCYCLE && spot.getType() == SpotType.COMPACT) return true; if (vType == VehicleType.CAR && (spot.getType() == SpotType.COMPACT || spot.getType() == SpotType.REGULAR)) return true; if (vType == VehicleType.SUV_TRUCK && spot.getType() == SpotType.REGULAR) return true; if (spot.getType() == SpotType.RESERVED) return true; return false; }
    public Ticket parkVehicle(Vehicle vehicle, String spotId) { ParkingSpot spot = getSpotById(spotId); if (spot != null && spot.isAvailableForParking()) { spot.park(vehicle); String typeLetter = "R"; switch(spot.getType()) { case COMPACT: typeLetter = "C"; break; case REGULAR: typeLetter = "R"; break; case HANDICAPPED: typeLetter = "H"; break; case RESERVED: typeLetter = "V"; break; } Ticket ticket = new Ticket(spotId, vehicle.getPlateNumber(), vehicle.getType(), vehicle.isHasHandicappedCard(), spot.getHourlyRate(), typeLetter); ticket.setLockedScheme(this.currentFineScheme); activeTickets.add(ticket); db.updateSpot(spot); db.saveTicket(ticket); return ticket; } return null; }
    public ParkingSpot getSpotById(String id) { return spots.stream().filter(s -> s.getId().equals(id)).findFirst().orElse(null); }
    public Ticket getActiveTicket(String plate) { return activeTickets.stream().filter(t -> t.getPlateNumber().equalsIgnoreCase(plate)).findFirst().orElse(null); }
    public double getOutstandingFine(String plate) { return 0.0; } 
    public List<String[]> getFinesForPlateWithId(String plate) { return db.getUnpaidFinesForPlateWithId(plate); }
    public List<String[]> getFinesForPlate(String plate) { return db.getUnpaidFinesForPlate(plate); }
    public List<String[]> getAllOutstandingFines() { return db.getAllUnpaidFinesList(); }
    public List<String[]> getPaidFinesByInvoiceId(String invoiceId) { return db.getPaidFinesByInvoiceId(invoiceId); }
    public List<ParkingSpot> getAllSpots() { return spots; }
    public List<Ticket> getActiveTickets() { return activeTickets; }
    public double getTotalRevenue() { return db.getRevenue(); }
    public int getOccupancyCount() { return (int) spots.stream().filter(ParkingSpot::isOccupied).count(); }
    public List<String[]> getTransactionHistory() { return db.getTransactionHistory(); }
    public FineScheme getFineScheme() { return currentFineScheme; }
    public void setFineScheme(FineScheme scheme) { this.currentFineScheme = scheme; db.saveConfig("FINE_SCHEME", scheme.name()); db.logAdminAction("CHANGE_FINE_RULE", "Updated to " + scheme.toString()); }
    public List<String[]> getAllFines() { return db.getAllFines(); }
    public List<String[]> getFineChangeHistory() { return db.getAdminLogs(); }
}