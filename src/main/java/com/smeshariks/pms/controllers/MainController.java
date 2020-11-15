package com.smeshariks.pms.controllers;

import com.smeshariks.pms.entities.Project;
import com.smeshariks.pms.entities.ProjectStatus;
import com.smeshariks.pms.entities.Statuses;
import com.smeshariks.pms.entities.User;
import com.smeshariks.pms.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {

    private ProjectService projectService;

    @Autowired
    public MainController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/home")
    public String homePage(Model model) {
        return "home";
    }

    @GetMapping("/main")
    public String mainPage(Model model) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Project> allProjects = projectService.findAllProjects();

        int activeProjects = 0;
        int notApproved = 0;
        int completed = 0;

        if(allProjects != null) {
            for (Project project : allProjects) {
                if(project.getStatuses() != null) {
                    ProjectStatus projectStatus = project.getStatuses().get(project.getStatuses().size() - 1);

                    switch (projectStatus.getStatus()) {
                        case "Не утвержден":
                            notApproved++;
                            break;

                        case "В работе":
                            activeProjects++;
                            break;

                        case "Завершен":
                            completed++;
                            break;
                    }
                }
            }
        }
        model.addAttribute("activeProjects", activeProjects);
        model.addAttribute("notApprovedProjects", notApproved);
        model.addAttribute("completedProjects", completed);
        model.addAttribute("user", user);
        return "main";
    }
}

