package com.smeshariks.pms.services;

import com.smeshariks.pms.entities.Project;
import com.smeshariks.pms.entities.ProjectStatus;
import com.smeshariks.pms.repositories.ProjectStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProjectStatusServiceImpl implements ProjectStatusService {

    private ProjectStatusRepository projectStatusRepository;

    @Autowired
    public ProjectStatusServiceImpl(ProjectStatusRepository projectStatusRepository) {
        this.projectStatusRepository = projectStatusRepository;
    }

    public ProjectStatus findProjectStatus(Integer id) {
        Optional<ProjectStatus> projectStatus = projectStatusRepository.findById(id);
        return projectStatus.orElse(new ProjectStatus());
    }

    public boolean saveProjectStatus(ProjectStatus projectStatus) {
        /*Optional<ProjectStatus> projectStatusDB = projectStatusRepository.findById(projectStatus.getId());

        if(projectStatusDB != null) {
            return false;
        }
    */
        projectStatusRepository.save(projectStatus);
        return true;
    }

    public void updateProjectStatus(ProjectStatus  projectStatus) {
        if(projectStatus != null) {
            projectStatusRepository.save(projectStatus);
        }
    }

    public void deleteProjectStatus(Integer projectStatusId) {
        projectStatusRepository.deleteById(projectStatusId);
    }

    public List<ProjectStatus> findProjectStatuses(Project project) {
        return projectStatusRepository.findProjectStatusesByProject(project);
    }

    public List<ProjectStatus> findAllProjectStatuses() {
        return projectStatusRepository.findAll();
    }
}
