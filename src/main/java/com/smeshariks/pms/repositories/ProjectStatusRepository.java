package com.smeshariks.pms.repositories;

import com.smeshariks.pms.entities.Project;
import com.smeshariks.pms.entities.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectStatusRepository extends JpaRepository<ProjectStatus, Integer> {

    List<ProjectStatus> findProjectStatusesByProject(Project project);
}
