package com.smeshariks.pms.services;

import com.smeshariks.pms.entities.Project;

import java.util.List;


public interface ProjectService {

    Project findProject(Integer id);
    boolean saveProject(Project project);
    void updateProject(Project project);
    void deleteProject(Integer projectId);
    List<Project> findAllProjects();
}
