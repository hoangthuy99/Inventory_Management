package com.ra.inventory_management.common;

public enum EOrderStatus {
    PENDING(1),
    APPROVED(2),
    REJECTED(3),
    PICKING(4),
    PACKING(5),
    WAITDELIVERY(6),
    HAVEDELIVERY(7),
    CANCELED(8),
    DONE(9);
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
