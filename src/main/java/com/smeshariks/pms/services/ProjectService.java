package com.smeshariks.pms.services;

import com.smeshariks.pms.entities.Project;

import java.util.List;


public interface ProjectService {

    Project findProject(Integer id);
    Project findProjectByTitle(String title);
    boolean saveProject(Project project);
    void updateProject(Project project);
    void deleteProject(Integer projectId);
    List<Project> findAllProjects();
}
