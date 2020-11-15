package com.smeshariks.pms.entities;


public enum Statuses {
    NOT_APPROVED(1, "Не утвержден"),
    IN_WORK(2, "В работе"),
    COMPLETED(3, "Завершен");

    private final int value;
    private final String name;

    Statuses(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }
}
