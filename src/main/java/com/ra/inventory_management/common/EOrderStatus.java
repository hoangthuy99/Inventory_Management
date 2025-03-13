package com.ra.inventory_management.common;

public enum EOrderStatus {
    PENDING(1),        // Chờ duyệt
    APPROVED(2),       // Đã duyệt
    REJECTED(3),       // Từ chối
    PICKING(4),        // Đang chọn hàng
    PACKING(5),        // Đang đóng gói
    READY_TO_SHIP(6),  // Đang chờ giao
    SHIPPED(7),        // Đã giao
    COMPLETED(8),      // Đã hoàn thành
    CANCELLED(9);      // Đã hủy

    private final int value;

    EOrderStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static EOrderStatus fromValue(int value) {
        for (EOrderStatus status : values()) {
            if (status.value == value) {
                return status;
            }
        }
        throw new IllegalArgumentException("Trạng thái đơn hàng không hợp lệ: " + value);
    }

    @Override
    public String toString() {
        switch (this) {
            case PENDING: return "Chờ duyệt";
            case APPROVED: return "Đã duyệt";
            case REJECTED: return "Từ chối";
            case PICKING: return "Đang chọn hàng";
            case PACKING: return "Đang đóng gói";
            case READY_TO_SHIP: return "Đang chờ giao";
            case SHIPPED: return "Đã giao";
            case COMPLETED: return "Đã hoàn thành";
            case CANCELLED: return "Đã hủy";
            default: return "Không xác định";
        }
    }
}
