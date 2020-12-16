package com.smeshariks.pms.repositories;

import com.smeshariks.pms.entities.Material;
import com.smeshariks.pms.entities.MaterialRequest;
import com.smeshariks.pms.entities.Project;
import com.smeshariks.pms.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterialRequestRepository extends JpaRepository<MaterialRequest, Integer> {

    List<MaterialRequest> findMaterialRequestByProject(Project project);
    List<MaterialRequest> findMaterialRequestByUser(User user);
    List<MaterialRequest> findMaterialRequestByMaterial(Material material);
}
