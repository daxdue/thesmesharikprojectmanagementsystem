package com.smeshariks.pms.controllers;

import com.smeshariks.pms.dto.SmesharikDto;
import com.smeshariks.pms.entities.*;
import com.smeshariks.pms.services.ProjectService;
import com.smeshariks.pms.services.TaskService;
import com.smeshariks.pms.services.TaskStatusService;
import com.smeshariks.pms.services.UserService;
import com.smeshariks.pms.utils.DateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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

    @Autowired
    public TaskController(ProjectService projectService, TaskService taskService,
                          UserService userService, TaskStatusService taskStatusService) {
        this.projectService = projectService;
        this.taskService = taskService;
        this.userService = userService;
        this.taskStatusService = taskStatusService;
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

}
