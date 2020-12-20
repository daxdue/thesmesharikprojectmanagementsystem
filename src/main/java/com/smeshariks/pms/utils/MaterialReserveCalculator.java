package com.smeshariks.pms.utils;

import com.smeshariks.pms.entities.Material;
import com.smeshariks.pms.entities.MaterialRequest;
import com.smeshariks.pms.entities.RequestStatus;
import com.smeshariks.pms.services.MaterialRequestService;
import com.smeshariks.pms.services.MaterialService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class MaterialReserveCalculator {

    @Autowired
    private MaterialRequestService materialRequestService;
    @Autowired
    private MaterialService materialService;

    //private List<Material> materials;
    //private List<MaterialRequest> materialRequests;

    @Autowired
    public MaterialReserveCalculator(MaterialRequestService materialRequestService, MaterialService materialService) {
        this.materialRequestService = materialRequestService;
        this.materialService = materialService;
    }

    public List<Material> calculate() {

        try {
            List<Material> materials = materialService.findAllMaterials();
            for(Material material : materials) {
                 List<MaterialRequest>  materialRequests = materialRequestService.findRequestsByStatusAndMaterial(RequestStatus.REQUESTED, material);
                 material.setReserve(materialRequests.size());
            }
            return materials;

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }
}
