package com.smeshariks.pms.services;

import com.smeshariks.pms.entities.Material;
import com.smeshariks.pms.entities.MaterialOrder;
import com.smeshariks.pms.entities.Order;

import java.util.List;

public interface MaterialOrderService {

    void saveList(List<MaterialOrder> orderList);
    MaterialOrder findByMaterial(Material material);
    MaterialOrder findById(Integer id);
    List<MaterialOrder> findAll();

}
