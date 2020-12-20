package com.smeshariks.pms.services;

import com.smeshariks.pms.entities.*;

import java.util.List;

public interface MaterialRequestService {

    MaterialRequest findRequest(Integer id);
    boolean saveMaterialRequest(MaterialRequest materialRequest);
    void updateMaterialRequest(MaterialRequest materialRequest);
    void deleteMaterialRequest(Integer id);
    List<MaterialRequest> findAllRequests();
    List<MaterialRequest> findRequestsByProject(Project project);
    List<MaterialRequest> findRequestsByUser(User user);
    List<MaterialRequest> findRequestsByMaterial(Material material);
    List<MaterialRequest> findRequestsByStatus(RequestStatus requestStatus);
    List<MaterialRequest> findRequestsByStatusAndMaterial(RequestStatus requestStatus, Material material);

}
