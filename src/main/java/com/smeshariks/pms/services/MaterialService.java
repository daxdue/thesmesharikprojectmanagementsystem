package com.smeshariks.pms.services;

import com.smeshariks.pms.entities.Material;

import java.util.List;

public interface MaterialService {

    Material findMaterial(Integer id);
    Material findMaterialByTitle(String title);
    boolean saveMaterial(Material material);
    void updateMaterial(Material material);
    void deleteMaterial(Integer id);
    long count();
    List<Material> findAllMaterials();
    List<Material> findAllMaterialsWithReserve();
    List<Material> findAllEquipments();
    List<Material> findOnlyMaterials();
}
