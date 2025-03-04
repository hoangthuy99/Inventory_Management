package com.ra.inventory_management.common;

public enum EOrderStatus {
    PENDING(0),        // Đơn hàng mới, chưa xác nhận
    CONFIRMED(1),      // Đã xác nhận đơn hàng
    READY_FOR_EXPORT(2), // Đã sẵn sàng để xuất kho
    EXPORTED(3),       // Đã xuất kho
    COMPLETED(4),      // Đã hoàn thành
    CANCELLED(5);      // Đơn hàng bị hủy

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
        throw new IllegalArgumentException("Invalid order status: " + value);
    }

    @Override
    public String toString() {
        switch (this) {
            case PENDING: return "Chờ xác nhận";
            case CONFIRMED: return "Đã xác nhận";
            case READY_FOR_EXPORT: return "Sẵn sàng xuất kho";
            case EXPORTED: return "Đã xuất kho";
            case COMPLETED: return "Hoàn thành";
            case CANCELLED: return "Đã hủy";
            default: return "Không xác định";
        }
    }
}
