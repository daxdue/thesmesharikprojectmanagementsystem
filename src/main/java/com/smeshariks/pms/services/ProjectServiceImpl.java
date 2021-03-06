package com.smeshariks.pms.services;

import com.smeshariks.pms.entities.Project;
import com.smeshariks.pms.entities.Status;
import com.smeshariks.pms.entities.Statuses;
import com.smeshariks.pms.entities.User;
import com.smeshariks.pms.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public Project findProjectByTitle(String title) {
        Project project = projectRepository.findByTitle(title);
        return project;
    }

    public boolean saveProject(Project project) {
        Project projectDB = projectRepository.findByTitle(project.getTitle());

        if(projectDB != null) {
            return false;
        }
        projectRepository.save(project);
        return true;
    }

    public void updateProject(Project project) {

        if(project != null) {
            projectRepository.edit(project.getDescription(), project.getCost(), project.getStartTime(),
                    project.getDeadTime(), project.getCurrentStatus(), project.getDeliveryAddress(), project.getId());
        }
    }

    public void deleteProject(Integer projectId) {
        projectRepository.deleteById(projectId);
    }

    public List<Project> findAllProjects() {
        return projectRepository.findAll();
    }

    public List<Project> findByStatus(Statuses status) {
        List<Project> projects = new ArrayList<>();

        switch (status) {
            case NOT_APPROVED:
                projects = projectRepository.findByCurrentStatus(Statuses.NOT_APPROVED.getValue());
                break;

            case IN_WORK:
                projects = projectRepository.findByCurrentStatus(Statuses.IN_WORK.getValue());
                break;

            case COMPLETED:
                projects = projectRepository.findByCurrentStatus(Statuses.COMPLETED.getValue());
                break;

            case REJECTED:
                projects = projectRepository.findByCurrentStatus(Statuses.REJECTED.getValue());
                break;

            case IN_DEVIVERY:
                projects = projectRepository.findByCurrentStatus(Statuses.IN_DEVIVERY.getValue());
                break;

            case ARCHIVATED:
                projects = projectRepository.findByCurrentStatus(Statuses.ARCHIVATED.getValue());
                break;
        }
        return projects;
    }

    public List<Project> findByUser(User user) {
        return projectRepository.findAllByOwner(user);
    }

    public List<Project> findByUserAndStatus(User user, Statuses status) {
        return projectRepository.findAllByOwnerAndCurrentStatus(user, status.getValue());
    }
}
