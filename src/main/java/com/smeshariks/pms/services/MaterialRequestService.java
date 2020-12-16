package com.smeshariks.pms.services;

import com.smeshariks.pms.entities.Material;
import com.smeshariks.pms.entities.MaterialRequest;
import com.smeshariks.pms.entities.Project;
import com.smeshariks.pms.entities.User;

import java.util.List;

public interface MaterialRequestService {

    MaterialRequest findRequest(Integer id);
    boolean saveMaterialRequest(MaterialRequest materialRequest);
    //void updateMaterialRequest(MaterialRequest materialRequest);
    void deleteMaterialRequest(Integer id);
    List<MaterialRequest> findAllRequests();
    List<MaterialRequest> findRequestsByProject(Project project);
    List<MaterialRequest> findRequestsByUser(User user);
    List<MaterialRequest> findRequestsByMaterial(Material material);
}
