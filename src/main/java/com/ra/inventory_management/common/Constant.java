package com.ra.inventory_management.common;

public class Constant {
    public static final String USER_INFO = "userInfo";
    public static final String MENU_SESSION = "menuSession";

    // Product Unit
    public static final Integer UNIT_PACK = 1;
    public static final Integer UNIT_PIECE = 2;
    public static final Integer UNIT_BIN = 3;
    public static final Integer UNIT_KG = 4;

    // Stock Status
    public static final Integer STOCK_NEW = 1;
    public static final Integer STOCK_REPAIR = 2;

    // Good Receipt Status
    public static final Integer PENDING = 1;
    public static final Integer APPROVED = 2;
    public static final Integer REJECTED = 3;
    public static final Integer CANCELED = 4;
    public static final Integer DONE = 5;

    // Good Issue Status
    public static final Integer ISSUE_PENDING = 1; // Chờ duyệt
    public static final Integer ISSUE_CONFIRMED = 2; // Đã duyệt
    public static final Integer ISSUE_REJECTED = 3; // Từ chối
    public static final Integer ISSUE_PICKING = 4; // Đang chọn hàng
    public static final Integer ISSUE_PACKING = 5; // Đang đóng gói
    public static final Integer ISSUE_WAITING_FOR_SHIPMENT = 6; // Đang chờ giao
    public static final Integer ISSUE_SHIPPED = 7; // Đã giao
    public static final Integer ISSUE_CANCELED = 8; // Đã hủy
    public static final Integer ISSUE_COMPLETED = 9; // Đã hoàn thành

    // Sample excel file import
    public static final String PRODUCT_SAMPLE = "/sample-excel/product.xlsx";
    public static final String CATEGORY_SAMPLE = "/sample-excel/category.xlsx";
    public static final String CUSTOMER_SAMPLE = "/sample-excel/customer.xlsx";
    public static final String BRANCH_SAMPLE = "/sample-excel/branch.xlsx";

    // Good Issue status
    public static final Integer GDI_PENDING = 1;
    public static final Integer GDI_APPROVED = 2;
    public static final Integer GDI_REJECTED = 3;
    public static final Integer GDI_PICKING = 4;
    public static final Integer GDI_PACKING = 5;
    public static final Integer GDI_WAITDELIVERY = 6;
    public static final Integer GDI_HAVEDELIVERY = 7;
    public static final Integer GDI_CANCELED = 8;
    public static final Integer GDI_DONE = 9;

    // Filter status of statistic
    public static final Integer BY_MONTH = 1;
    public static final Integer BY_QUARTER = 2;
    public static final Integer BY_YEAR = 3;
}
