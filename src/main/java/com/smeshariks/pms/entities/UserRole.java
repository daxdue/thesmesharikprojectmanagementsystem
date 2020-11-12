package com.smeshariks.pms.entities;

public enum UserRole {
    ADMIN("Администратор"),
    CUSTOMER("Заказчик"),
    WORKER("Работник"),
    MANAGER("Менеджер"),
    WAREHOUSEMAN("Кладовщик");

    private final String displayValue;

    private UserRole(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
