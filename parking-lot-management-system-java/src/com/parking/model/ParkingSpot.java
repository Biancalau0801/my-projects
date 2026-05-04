package com.parking.model;

public class ParkingSpot {
    private String id;
    private int floor;
    private int row;
    private int number;
    private SpotType type;
    private SpotStatus status; // 🌟 Changed from boolean occupied to Enum
    private Vehicle currentVehicle;

    public ParkingSpot(int floor, int row, int number, SpotType type) {
        this.floor = floor;
        this.row = row;
        this.number = number;
        this.id = "F" + floor + "-R" + row + "-S" + number;
        this.type = type;
        this.status = SpotStatus.AVAILABLE; // Default
    }

    // Getters & Setters
    public String getId() { return id; }
    public int getFloor() { return floor; }
    public int getRow() { return row; }
    public int getNumber() { return number; }
    public SpotType getType() { return type; }
    
    public SpotStatus getStatus() { return status; }
    public void setStatus(SpotStatus status) { this.status = status; }

    // Helper to check availability for logic
    public boolean isAvailableForParking() {
        return this.status == SpotStatus.AVAILABLE;
    }

    public boolean isOccupied() {
        return this.status == SpotStatus.OCCUPIED;
    }

    public Vehicle getCurrentVehicle() { return currentVehicle; }

    public void park(Vehicle v) {
        this.currentVehicle = v;
        this.status = SpotStatus.OCCUPIED; // Auto set to O
    }

    public void removeVehicle() {
        this.currentVehicle = null;
        this.status = SpotStatus.AVAILABLE; // Auto set to A
    }
    
    public double getHourlyRate() {
        switch (type) {
            case COMPACT: return 2.0; // Motorcycle
            case REGULAR: return 5.0; // Car
            case HANDICAPPED: return 5.0; 
            case RESERVED: return 10.0;
            default: return 5.0;
        }
    }
}