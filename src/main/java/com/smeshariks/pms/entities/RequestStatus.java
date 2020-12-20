package com.smeshariks.pms.entities;

public enum RequestStatus {
    REQUESTED(0),
    IN_PROCESS(1),
    COMPLETED(2);

    private final Integer value;

    RequestStatus(Integer value) {
        this.value = value;
    }

    public Integer getValue() {return value;}
}
