package com.smeshariks.pms.controllers;

import com.smeshariks.pms.entities.*;
import com.smeshariks.pms.services.ProjectService;
import com.smeshariks.pms.services.TaskService;
import com.smeshariks.pms.services.UserService;
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
    private UserService userService;

    @Autowired
    public MainController(ProjectService projectService, TaskService taskService, UserService userService) {
        this.projectService = projectService;
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping("/home")
    public String homePage(Model model) {
        return "home";
    }

    @GetMapping("/chat")
    public String chatPage(Model model) {
        return "chat";
    }

    @GetMapping("/main")
    public String mainPage(Model model) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<Project> allProjects = new ArrayList<>();
        List<Task> allTasks = new ArrayList<>();

        int waitingTasks = 0;
        int activaTasks = 0;
        int waitingApprove= 0;

        if(user.getUserRole() == ADMIN) {
            allProjects = projectService.findAllProjects();
            allTasks = taskService.findAllTasks();
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


            if(allTasks != null) {
                for(Task task : allTasks) {
                    switch (task.getLastStatus().getStatusValue()) {
                        case WAIT:
                            waitingTasks++;
                            break;

                        case IN_WORK:
                            activaTasks++;
                            break;

                        case NOT_APPROVED:
                            waitingApprove++;
                            break;
                    }
                }

                model.addAttribute("activeTasks", activaTasks);
                model.addAttribute("waitingTasks", waitingTasks);
                model.addAttribute("waitingApprove", waitingApprove);
            }

            List<User> allWorkersAmount = userService.findAllByRoleName(UserRole.WORKER.getDatabaseRole());

            int freeWorkers = 0;
            int busyWorkers = 0;

            if(allWorkersAmount != null) {
                for (User us : allWorkersAmount) {
                    List<Task> workerTasks = us.getTasks();
                    if(workerTasks != null) {
                        int preValue = busyWorkers;
                        for(Task wTask : workerTasks) {
                            if(wTask.getLastStatus().getStatusValue() == Statuses.IN_WORK) {
                                busyWorkers++;
                                break;
                            }
                        }
                        if(preValue == busyWorkers) {
                            freeWorkers++;
                        }

                    } else {
                        freeWorkers++;
                    }
                }

                model.addAttribute("workersAmount", allWorkersAmount.size());
                model.addAttribute("freeWorkers", freeWorkers);
                model.addAttribute("busyWorkers", busyWorkers);
            }

            model.addAttribute("activeProjects", activeProjects);
            model.addAttribute("notApprovedProjects", notApproved);
            model.addAttribute("completedProjects", completed);


        } else if(user.getUserRole() == UserRole.WAREHOUSEMAN){

            //Заявки

            //Остатки по складу

        } else {
            allProjects = projectService.findByUser(user);
            allTasks = taskService.findTasksByExecutor(user);
            activaTasks = 0;
            waitingTasks = 0;

            if(allTasks != null) {
                for(Task task : allTasks) {

                    switch (task.getLastStatus().getStatusValue()) {
                        case WAIT:
                            waitingTasks++;
                            break;

                        case IN_WORK:
                            activaTasks++;
                            break;

                        case NOT_APPROVED:
                            waitingApprove++;
                            break;
                    }
                }
            }
            model.addAttribute("activeTasks", activaTasks);
            model.addAttribute("waitingTasks", waitingTasks);
            model.addAttribute("waitingApprove", waitingApprove);
            model.addAttribute("tasks", allTasks);
        }

        model.addAttribute("user", user);


        return "main";
    }
}

