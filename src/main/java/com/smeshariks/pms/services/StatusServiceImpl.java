package com.smeshariks.pms.services;

import com.smeshariks.pms.entities.Status;
import com.smeshariks.pms.repositories.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatusServiceImpl implements StatusService{

    private StatusRepository statusRepository;

    @Autowired
    public StatusServiceImpl(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    public Status findStatus(Integer id) {
        Optional<Status> status = statusRepository.findById(id);
        return status.orElse(new Status());
    }

    public boolean saveStatus(Status status) {
        Optional<Status> statusDB = statusRepository.findById(status.getId());

        if(statusDB != null) {
            return false;
        }

        statusRepository.save(status);
        return true;
    }

    public void updateStatus(Status status) {
        if(status != null) {
            statusRepository.save(status);
        }
    }

    public void deleteStatus(Integer statusId) {
        statusRepository.deleteById(statusId);
    }

    public List<Status> findAllStatuses() {
        return statusRepository.findAll();
    }

}
