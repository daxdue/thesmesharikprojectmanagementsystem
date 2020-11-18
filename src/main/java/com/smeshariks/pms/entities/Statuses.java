package com.smeshariks.pms.entities;

public enum Statuses {
    NOT_APPROVED(1, "не утвержден"),
    IN_WORK(2, "в работе"),
    COMPLETED(3, "завершен");

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
