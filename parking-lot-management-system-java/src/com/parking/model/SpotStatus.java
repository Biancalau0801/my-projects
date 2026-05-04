package com.parking.model;

public enum SpotStatus {
    AVAILABLE("A", "Available"),
    OCCUPIED("O", "Occupied"),
    MAINTENANCE("M", "Maintenance"),
    INCORRECT("I", "Parking Incorrect Spot");

    private final String code;
    private final String displayName;

    SpotStatus(String code, String displayName) {
        this.code = code;
        this.displayName = displayName;
    }

    public String getCode() { return code; }
    public String getDisplayName() { return displayName; }

    public static SpotStatus fromCode(String code) {
        for (SpotStatus s : values()) {
            if (s.code.equalsIgnoreCase(code)) return s;
        }
        return AVAILABLE; // Default
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}