package com.parking.model;

public class Vehicle {
    private String plateNumber;
    private VehicleType type;
    private boolean hasHandicappedCard; // 是否持有残疾人卡

    public Vehicle(String plateNumber, VehicleType type, boolean hasHandicappedCard) {
        this.plateNumber = plateNumber;
        this.type = type;
        this.hasHandicappedCard = hasHandicappedCard;
    }

    // Getter 方法
    public String getPlateNumber() { return plateNumber; }
    public VehicleType getType() { return type; }
    public boolean isHasHandicappedCard() { return hasHandicappedCard; }
}