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

    // Sample excel file import
    public static final String PRODUCT_SAMPLE = "/sample-excel/product.xlsx";
    public static final String CATEGORY_SAMPLE = "/sample-excel/category.xlsx";
}
