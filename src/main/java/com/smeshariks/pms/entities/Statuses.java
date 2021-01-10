package com.smeshariks.pms.entities;

public enum Statuses {
    UNKNOWN(0,"не известен"),
    NOT_APPROVED(1, "не утвержден"),
    IN_WORK(2, "в работе"),
    COMPLETED(3, "завершен"),
    REJECTED(4, "отклонен"),
    WAIT(5, "ожидает"),
    EXPIRED(6, "просрочена"),
    ARCHIVATED(7, "в архиве"),
    IN_DEVIVERY(8, "в доставке");

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
