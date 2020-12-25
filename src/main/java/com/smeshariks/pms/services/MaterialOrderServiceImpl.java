package com.smeshariks.pms.services;

import com.smeshariks.pms.entities.Material;
import com.smeshariks.pms.entities.MaterialOrder;
import com.smeshariks.pms.entities.Order;
import com.smeshariks.pms.repositories.MaterialOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MaterialOrderServiceImpl implements MaterialOrderService {

    private final MaterialOrderRepository materialOrderRepository;

    @Autowired
    public MaterialOrderServiceImpl(MaterialOrderRepository materialOrderRepository) {
        this.materialOrderRepository = materialOrderRepository;
    }

    public void saveList(List<MaterialOrder> orderList) {
        for (MaterialOrder materialOrder : orderList) {
            materialOrderRepository.save(materialOrder);
        }
    }

    public MaterialOrder findById(Integer id) {
        return materialOrderRepository.findById(id).orElse(new MaterialOrder());
    }
    public MaterialOrder findByMaterial(Material material) {
        return materialOrderRepository.findByMaterial(material);
    }

    public List<MaterialOrder> findAll(){
        return materialOrderRepository.findAll();
    }
}
