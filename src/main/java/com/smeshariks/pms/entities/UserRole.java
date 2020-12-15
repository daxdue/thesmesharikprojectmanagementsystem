package com.smeshariks.pms.entities;

public enum UserRole {
    ADMIN("Директор", "ROLE_ADMIN"),
    CUSTOMER("Заказчик", "ROLE_CUSTOMER"),
    WORKER("Работник", "ROLE_WORKER"),
    MANAGER("Менеджер", "ROLE_MANAGER"),
    WAREHOUSEMAN("Кладовщик", "ROLE_WAREHOUSEMAN");

    private final String displayValue;
    private final String databaseRole;

    private UserRole(String displayValue, String databaseRole) {
        this.displayValue = displayValue;
        this.databaseRole = databaseRole;
    }

    public String getDisplayValue() {
        return displayValue;
    }

    public String getDatabaseRole() {return databaseRole;}
}
