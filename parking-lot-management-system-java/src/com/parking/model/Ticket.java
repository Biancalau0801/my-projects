package com.parking.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Ticket {
    private String spotId;
    private String plateNumber;
    private LocalDateTime entryTime;
    private FineScheme lockedScheme;
    private VehicleType vehicleType; 
    private boolean isHandicapped;
    
    // 🌟 1. 新增字段：记录费率和生成的 ID
    private double parkingRate;
    private String ticketId;

    // 🌟 2. 修改构造函数：接收 parkingRate 和 typeLetter (C/R/H/V)
    public Ticket(String spotId, String plateNumber, VehicleType vehicleType, 
                  boolean isHandicapped, double parkingRate, String typeLetter) {
        this.spotId = spotId;
        this.plateNumber = plateNumber;
        this.vehicleType = vehicleType;
        this.isHandicapped = isHandicapped;
        this.parkingRate = parkingRate; // 存储费率
        
        this.entryTime = LocalDateTime.now();
        this.lockedScheme = FineScheme.FIXED; 

        // 🌟 核心逻辑：生成自定义 ID (格式：类型简写_车牌_yymmddhhmm)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmm");
        String timeStr = this.entryTime.format(formatter);
        this.ticketId = typeLetter + "_" + plateNumber + "_" + timeStr;
    }

    // 🌟 3. 新增 Getter
    public double getParkingRate() { return parkingRate; }
    public String getTicketId() { return ticketId; }

    public VehicleType getVehicleType() { return vehicleType; }
    public boolean isHandicapped() { return isHandicapped; }
    public FineScheme getLockedScheme() { return lockedScheme; }
    public void setLockedScheme(FineScheme scheme) { this.lockedScheme = scheme; }
    public String getSpotId() { return spotId; }
    public String getPlateNumber() { return plateNumber; }
    public LocalDateTime getEntryTime() { return entryTime; }
    public void setEntryTime(LocalDateTime entryTime) { this.entryTime = entryTime; }

    // 🌟 4. 修改 toString 确保打印出来的是新的 ID
    @Override
    public String toString() {
        return ticketId;
    }
}