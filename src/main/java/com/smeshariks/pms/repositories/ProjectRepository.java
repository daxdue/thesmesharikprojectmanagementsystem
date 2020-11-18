package com.smeshariks.pms.repositories;

import com.smeshariks.pms.entities.Project;
import com.smeshariks.pms.entities.Status;
import com.smeshariks.pms.entities.Statuses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {

    Project findByTitle(String title);

    @Modifying
    @Query("SELECT p FROM Project p WHERE p.currentStatus = :status")
    List<Project> findByCurrentStatus(@Param(value = "status") Integer status);
}
