package com.smeshariks.pms.services;

import com.smeshariks.pms.entities.Smesharik;
import com.smeshariks.pms.repositories.SmesharikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmesharikServiceImpl implements SmesharikService {

    @Autowired
    SmesharikRepository smesharikRepository;

    public List<Smesharik> allSmeshariks() {
        return smesharikRepository.findAll();
    }
}
