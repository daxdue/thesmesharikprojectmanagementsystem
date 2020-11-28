package com.smeshariks.pms.services;

import com.smeshariks.pms.entities.Project;
import com.smeshariks.pms.entities.Status;
import com.smeshariks.pms.entities.Statuses;
import com.smeshariks.pms.entities.User;

import java.util.List;


public interface ProjectService {

    Project findProject(Integer id);
    Project findProjectByTitle(String title);
    boolean saveProject(Project project);
    void updateProject(Project project);
    void deleteProject(Integer projectId);
    List<Project> findAllProjects();
    List<Project> findByStatus(Statuses status);
    List<Project> findByUser(User user);
    List<Project> findByUserAndStatus(User user, Statuses status);
}
