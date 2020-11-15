package com.smeshariks.pms.services;

import com.smeshariks.pms.entities.Timings;
import com.smeshariks.pms.repositories.TimingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TimingsServiceImpl implements TimingsService{

    private TimingsRepository timingsRepository;

    @Autowired
    public TimingsServiceImpl(TimingsRepository timingsRepository) {
        this.timingsRepository = timingsRepository;
    }

    public Timings findTimings(Integer id) {
        Optional<Timings> timings = timingsRepository.findById(id);
        return timings.orElse(new Timings());
    }

    public boolean saveTimings(Timings timings) {
        Optional<Timings> timingsDB = timingsRepository.findById(timings.getId());

        if(timingsDB != null) {
            return false;
        }

        timingsRepository.save(timings);
        return true;
    }

    public void updateTimings(Timings timings) {
        if(timings != null) {
            timingsRepository.save(timings);
        }
    }

    public void deleteTimings(Integer id) {
        timingsRepository.deleteById(id);
    }

    public List<Timings> findAllTimings() {
        return timingsRepository.findAll();
    }
}
