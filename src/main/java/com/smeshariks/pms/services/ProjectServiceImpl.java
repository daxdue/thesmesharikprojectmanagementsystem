package com.smeshariks.pms.services;

import com.smeshariks.pms.entities.Project;
import com.smeshariks.pms.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {

    private ProjectRepository projectRepository;

    @Autowired
    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project findProject(Integer id) {
        Optional<Project> project = projectRepository.findById(id);
        return project.orElse(new Project());
    }

    public boolean saveProject(Project project) {
        Optional<Project> projectDB = projectRepository.findById(project.getId());

        if(projectDB != null) {
            return false;
        }
        projectRepository.save(project);
        return true;
    }

    public void updateProject(Project project) {

        if(project != null) {
            projectRepository.save(project);
        }
    }

    public void deleteProject(Integer projectId) {
        projectRepository.deleteById(projectId);
    }

    public List<Project> findAllProjects() {
        return projectRepository.findAll();
    }
}
