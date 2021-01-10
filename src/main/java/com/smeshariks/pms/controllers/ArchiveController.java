package com.smeshariks.pms.controllers;

import com.smeshariks.pms.dto.SmesharikDto;
import com.smeshariks.pms.entities.*;
import com.smeshariks.pms.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/archive")
public class ArchiveController {

    private ProjectService projectService;
    private TaskService taskService;
    private UserService userService;
    private final MaterialService materialService;
    private final MaterialRequestService materialRequestService;
    private final MaterialOrderService materialOrderService;
    private final OrderService orderService;

    @Autowired
    public ArchiveController(ProjectService projectService, TaskService taskService, UserService userService,
                          MaterialService materialService, MaterialRequestService materialRequestService,
                          MaterialOrderService materialOrderService, OrderService orderService) {
        this.projectService = projectService;
        this.taskService = taskService;
        this.userService = userService;
        this.materialService = materialService;
        this.materialRequestService = materialRequestService;
        this.materialOrderService = materialOrderService;
        this.orderService = orderService;
    }

    @GetMapping
    public String getArchive(Model model) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SmesharikDto smesharikDto = new SmesharikDto();
        smesharikDto.setId(user.getId());
        smesharikDto.setName(user.getName());
        smesharikDto.setUserRole(user.getUserRole());

        model.addAttribute("user", smesharikDto);
        return "archive";
    }

    @GetMapping("/get")
    public String getArchiveType(@RequestParam(name = "type") String type, Model model) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SmesharikDto smesharikDto = new SmesharikDto();
        smesharikDto.setId(user.getId());
        smesharikDto.setName(user.getName());
        smesharikDto.setUserRole(user.getUserRole());

        if(user.getUserRole() == UserRole.ADMIN) {
            switch (type) {
                case "projects":
                    model.addAttribute("projects", projectService.findByStatus(Statuses.ARCHIVATED));
                    break;

                case "tasks":
                    List<Task> tasks = taskService.findAllTasks();
                    List<Task> completed = new ArrayList<>();
                    for(Task task : tasks) {
                        if(task.getLastStatus().getStatusValue() == Statuses.COMPLETED) {
                            completed.add(task);
                        }
                    }
                    model.addAttribute("tasks", completed);
                    break;

                case "requests":
                    model.addAttribute("requests", materialRequestService.findRequestsByStatus(RequestStatus.COMPLETED));
                    break;

                case "orders":
                    model.addAttribute("orders", orderService.findOrdersByStatus(RequestStatus.COMPLETED));
                    break;
            }
        } else {
            switch (type) {
                case "projects":
                    model.addAttribute("projects", projectService.findByUserAndStatus(user, Statuses.ARCHIVATED));
                    break;

                case "tasks":
                    List<Task> tasks = taskService.findTasksByExecutor(user);
                    List<Task> completed = new ArrayList<>();
                    for(Task task : tasks) {
                        if(task.getLastStatus().getStatusValue() == Statuses.COMPLETED) {
                            completed.add(task);
                        }
                    }
                    model.addAttribute("tasks", completed);
                    break;

                case "requests":
                    model.addAttribute("requests", materialRequestService.findRequestsByStatus(RequestStatus.COMPLETED));
                    break;

                case "orders":
                    model.addAttribute("orders", orderService.findOrdersByStatus(RequestStatus.COMPLETED));
                    break;
            }
        }

        model.addAttribute("type", type);
        model.addAttribute("user", smesharikDto);
        return "archive_items";
    }
}
