package com.smeshariks.pms.services;

import com.smeshariks.pms.entities.Project;
import com.smeshariks.pms.entities.ProjectStatus;

import java.util.List;

public interface ProjectStatusService {

    ProjectStatus findProjectStatus(Integer id);
    boolean saveProjectStatus(ProjectStatus projectStatus);
    void updateProjectStatus(ProjectStatus projectStatus);
    void deleteProjectStatus(Integer projectStatusId);
    List<ProjectStatus> findProjectStatuses(Project project);
    List<ProjectStatus> findAllProjectStatuses();

}
