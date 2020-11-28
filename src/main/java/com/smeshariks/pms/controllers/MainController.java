package com.smeshariks.pms.controllers;

import com.smeshariks.pms.entities.*;
import com.smeshariks.pms.services.ProjectService;
import com.smeshariks.pms.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

import static com.smeshariks.pms.entities.UserRole.ADMIN;

@Controller
public class MainController {

    private ProjectService projectService;
    private TaskService taskService;

    @Autowired
    public MainController(ProjectService projectService, TaskService taskService) {
        this.projectService = projectService;
        this.taskService = taskService;
    }

    @GetMapping("/home")
    public String homePage(Model model) {
        return "home";
    }

    @GetMapping("/main")
    public String mainPage(Model model) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Project> allProjects = new ArrayList<>();
        List<Task> allTasks = new ArrayList<>();

        if(user.getUserRole() == ADMIN) {
            allProjects = projectService.findAllProjects();
            allTasks = taskService.findAllTasks();

        } else {
            allProjects = projectService.findByUser(user);
            allTasks = taskService.findTasksByExecutor(user);
        }

        int activeProjects = 0;
        int notApproved = 0;
        int completed = 0;
        int rejected = 0;

        if(allProjects != null) {
            for (Project project : allProjects) {

                switch (project.getStatus()) {
                    case NOT_APPROVED:
                        notApproved++;
                        break;

                    case IN_WORK:
                        activeProjects++;
                        break;

                    case COMPLETED:
                        completed++;
                        break;

                    case REJECTED:
                        rejected++;
                        break;
                }
            }
        }
        model.addAttribute("tasks", allTasks);
        model.addAttribute("activeProjects", activeProjects);
        model.addAttribute("notApprovedProjects", notApproved);
        model.addAttribute("completedProjects", completed);
        model.addAttribute("user", user);
        return "main";
    }
}

