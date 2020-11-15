package com.smeshariks.pms.services;

import com.smeshariks.pms.entities.Status;

import java.util.List;

public interface StatusService {

    Status findStatus(Integer id);
    boolean saveStatus(Status status);
    void updateStatus(Status status);
    void deleteStatus(Integer statusId);
    List<Status> findAllStatuses();
}
