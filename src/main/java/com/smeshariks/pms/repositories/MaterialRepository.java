package com.smeshariks.pms.repositories;

import com.smeshariks.pms.entities.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.List;

@Repository
@Transactional
public interface MaterialRepository extends JpaRepository<Material, Integer> {
    Material findByTitle(String title);

    @Modifying
    @Query("update Material m set m.title = :title, m.description = :description, m.balance = :balance, " +
            "m.lastDelivery = :lastDelivery where m.id = :id")
    void edit(@Param(value = "title") String title, @Param(value = "description") String description,
              @Param(value = "balance") Integer balance, @Param(value = "lastDelivery") Timestamp lastDelivery,
              @Param(value = "id") Integer id);

    List<Material> findAllByIsEquipment(int type);
}
