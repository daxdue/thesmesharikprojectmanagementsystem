package com.smeshariks.pms.services;

import com.smeshariks.pms.entities.Timings;

import java.util.List;

public interface TimingsService {

    Timings findTimings(Integer id);
    boolean saveTimings(Timings timings);
    void updateTimings(Timings timings);
    void deleteTimings(Integer id);
    List<Timings> findAllTimings();
}
