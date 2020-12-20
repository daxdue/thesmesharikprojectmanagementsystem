package com.smeshariks.pms.repositories;

import com.smeshariks.pms.entities.Material;
import com.smeshariks.pms.entities.MaterialRequest;
import com.smeshariks.pms.entities.Project;
import com.smeshariks.pms.entities.User;
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
public interface MaterialRequestRepository extends JpaRepository<MaterialRequest, Integer> {

    List<MaterialRequest> findMaterialRequestByProject(Project project);
    List<MaterialRequest> findMaterialRequestByUser(User user);
    List<MaterialRequest> findMaterialRequestByMaterial(Material material);
    List<MaterialRequest> findMaterialRequestByStatus(Integer status);
    List<MaterialRequest> findMaterialRequestByStatusAndMaterial(Integer status, Material material);
    @Modifying
    @Query("update MaterialRequest m set m.status = :status where m.id = :id")
    void edit(@Param(value = "status") Integer status, @Param(value = "id") Integer id);
}
