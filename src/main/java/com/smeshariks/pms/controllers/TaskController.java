package com.smeshariks.pms.controllers;

import com.smeshariks.pms.dto.MaterialRequestDto;
import com.smeshariks.pms.dto.SmesharikDto;
import com.smeshariks.pms.entities.*;
import com.smeshariks.pms.services.*;
import com.smeshariks.pms.utils.DateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/task")
public class TaskController {

    private ProjectService projectService;
    private TaskService taskService;
    private TaskStatusService taskStatusService;
    private UserService userService;
    private final MaterialService materialService;

    @Autowired
    public TaskController(ProjectService projectService, TaskService taskService,
                          UserService userService, TaskStatusService taskStatusService,
                          MaterialService materialService) {
        this.projectService = projectService;
        this.taskService = taskService;
        this.userService = userService;
        this.taskStatusService = taskStatusService;
        this.materialService = materialService;
    }



    @PostMapping("/create")
    public String createTask(@ModelAttribute("taskForm") @Valid Task task, BindingResult bindingResult,
                             Model model) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SmesharikDto smesharikDto = new SmesharikDto();
        smesharikDto.setId(user.getId());
        smesharikDto.setName(user.getName());
        smesharikDto.setUserRole(user.getUserRole());

        User executor = userService.findUserById(task.getExecutorId());

        if(executor != null) {
            task.setExecutor(executor);
        }

        Project project = projectService.findProject(task.getProjectId());

        if(project != null) {
            task.setProject(project);
        }

        DateValidator dateValidator = new DateValidator();
        if(dateValidator.isValidSingle(task.getDeadTimeStr())) {

            task.setStartTime(new Timestamp(new Date().getTime()));
            task.setDeadTime(dateValidator.convertSingle(task.getDeadTimeStr()));

            TaskStatus taskStatus = new TaskStatus();
            taskStatus.setTimestamp(new Timestamp(new Date().getTime()));
            taskStatus.setTask(task);
            taskStatus.setStatus(Statuses.WAIT.getValue());
            taskService.saveTask(task);
            taskStatusService.saveTaskStatus(taskStatus);
        }

        return "redirect:/project/" + project.getId();
    }

    /*
    TODO Настроить права доступа только админу к этому контроллеру
     */
    @GetMapping("/delete/{id}")
    public String deleteTask(@PathVariable Integer id) {

        Task task = taskService.findTask(id);
        if(task != null) {
            taskService.deleteTask(task.getId());
            return "redirect:/project/" + task.getProject().getId();
        }

        return "home";
    }

    @GetMapping("/{id}/{action}")
    public String updateTask(@PathVariable Integer id, @PathVariable String action, Model model) {

        Task task = taskService.findTask(id);
        if(task != null) {

            TaskStatus taskStatus = new TaskStatus();
            taskStatus.setTimestamp(new Timestamp(new Date().getTime()));
            taskStatus.setTask(task);

            switch (action) {

                case "start":
                    taskStatus.setStatus(Statuses.IN_WORK.getValue());
                    break;

                case "complete":
                    taskStatus.setStatus(Statuses.NOT_APPROVED.getValue());
                    break;

                case "approve":
                    taskStatus.setStatus(Statuses.COMPLETED.getValue());
                    break;

                default:
                    taskStatus.setStatus(Statuses.UNKNOWN.getValue());
                    break;
            }

            taskStatusService.saveTaskStatus(taskStatus);
        }
        return "redirect:/task";
    }

    @PostMapping("/update")
    public String updateTask(@ModelAttribute("taskReport") TaskReport taskReport) {

        if(taskReport != null) {
            Task task = taskService.findTask(taskReport.getTask_id());
            if(task != null) {
                TaskStatus taskStatus = new TaskStatus();
                taskStatus.setTimestamp(new Timestamp(new Date().getTime()));
                taskStatus.setTask(task);
                taskStatus.setStatus(Statuses.NOT_APPROVED.getValue());
                taskReport.setTask(task);
                taskStatusService.saveTaskStatus(taskStatus);
                taskStatusService.saveTaskReport(taskReport);
            }
        }

        return "redirect:/task";
    }

    @GetMapping
    public String userTasks(Model model) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SmesharikDto smesharikDto = new SmesharikDto();
        smesharikDto.setId(user.getId());
        smesharikDto.setName(user.getName());
        smesharikDto.setUserRole(user.getUserRole());

        List<Task> inWorkTasks = new ArrayList<>();
        List<Task> waitExecuteTasks = new ArrayList<>();
        List<Task> waitApproveTasks = new ArrayList<>();
        List<Task> allTasks = new ArrayList<>();
        List<Material> materials = materialService.findAllMaterials();

        if(smesharikDto.getUserRole() == UserRole.ADMIN) {

            allTasks = taskService.findAllTasks();

        } else {

            allTasks = taskService.findTasksByExecutor(user);
        }


        if(allTasks != null) {

            for (Task task : allTasks) {

                switch (task.getLastStatus().getStatusValue()) {

                    case WAIT:
                        waitExecuteTasks.add(task);
                        break;

                    case IN_WORK:
                        inWorkTasks.add(task);
                        break;

                    case NOT_APPROVED:
                        waitApproveTasks.add(task);
                        break;
                }
            }
        }

        model.addAttribute("user", smesharikDto);
        model.addAttribute("workflowTasks", inWorkTasks);
        model.addAttribute("waitApproveTasks", waitApproveTasks);
        model.addAttribute("waitExecuteTasks", waitExecuteTasks);
        model.addAttribute("materials", materials);
        model.addAttribute("newRequest", new MaterialRequestDto());
        model.addAttribute("taskReport", new TaskReport());

        return "tasks";
    }

}
