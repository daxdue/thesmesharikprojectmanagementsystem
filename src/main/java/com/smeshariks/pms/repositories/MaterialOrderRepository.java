package com.smeshariks.pms.repositories;

import com.smeshariks.pms.entities.Material;
import com.smeshariks.pms.entities.MaterialOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialOrderRepository extends JpaRepository<MaterialOrder, Integer> {
    MaterialOrder findByMaterial(Material material);
}
