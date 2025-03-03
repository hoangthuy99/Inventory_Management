package com.ra.inventory_management.common;

public enum EOrderStatus {
    PENDING(0),
    CONFIRMED(1),
    SHIPPED(2),
    COMPLETED(3),
    CANCELED(4);

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
}
