package com.smeshariks.pms.services;

import com.smeshariks.pms.entities.Material;
import com.smeshariks.pms.entities.MaterialRequest;
import com.smeshariks.pms.entities.Project;
import com.smeshariks.pms.entities.User;
import com.smeshariks.pms.repositories.MaterialRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MaterialRequestServiceImpl implements MaterialRequestService {

    private final MaterialRequestRepository materialRequestRepository;

    @Autowired
    public MaterialRequestServiceImpl(MaterialRequestRepository materialRequestRepository) {
        this.materialRequestRepository = materialRequestRepository;
    }

    public MaterialRequest findRequest(Integer id) {
        Optional<MaterialRequest> materialRequest = materialRequestRepository.findById(id);
        return materialRequest.orElse(new MaterialRequest());
    }

    public boolean saveMaterialRequest(MaterialRequest materialRequest) {
        materialRequestRepository.save(materialRequest);
        return true;
    }

    public void deleteMaterialRequest(Integer id) {
        materialRequestRepository.deleteById(id);
    }

    public List<MaterialRequest> findAllRequests() {
        return materialRequestRepository.findAll();
    }

    public List<MaterialRequest> findRequestsByProject(Project project) {
        return materialRequestRepository.findMaterialRequestByProject(project);
    }

    public List<MaterialRequest> findRequestsByUser(User user) {
        return materialRequestRepository.findMaterialRequestByUser(user);
    }

    public List<MaterialRequest> findRequestsByMaterial(Material material) {
        return materialRequestRepository.findMaterialRequestByMaterial(material);
    }
}
