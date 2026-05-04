package com.parking.model;

public enum FineScheme {
    FIXED,          // Option A: 固定罚款 (Flat RM 50)
    PROGRESSIVE,    // Option B: 阶梯式/递增罚款 (First 24h RM 50, then increases)
    HOURLY          // Option C: 按小时罚款 (RM 20 per hour)
}