package com.smeshariks.pms.services;

import com.smeshariks.pms.entities.Material;
import com.smeshariks.pms.repositories.MaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MaterialServiceImpl implements MaterialService{

    private final MaterialRepository materialRepository;

    @Autowired
    public MaterialServiceImpl(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    public Material findMaterial(Integer id) {
        Optional<Material> material = materialRepository.findById(id);
        return material.orElse(new Material());
    }

    public Material findMaterialByTitle(String title) {
        return materialRepository.findByTitle(title);
    }

    public boolean saveMaterial(Material material) {
        Material materialDB = materialRepository.findByTitle(material.getTitle());

        if (materialDB != null) {
            return false;
        }

        materialRepository.save(material);
        return true;
    }

    public void updateMaterial(Material material) {
        try {
            materialRepository.edit(material.getTitle(), material.getDescription(), material.getBalance(),
                    material.getLastDelivery(), material.getId());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void deleteMaterial(Integer id) {
        materialRepository.deleteById(id);
    }

    public List<Material> findAllMaterials() {
        return (List<Material>) materialRepository.findAll();
    }

    public List<Material> findAllEquipments() {
        return materialRepository.findAllByIsEquipment(1);
    }

    public List<Material> findOnlyMaterials() {
        return materialRepository.findAllByIsEquipment(0);
    }
}
