package com.smeshariks.pms.controllers;

import com.smeshariks.pms.dto.ProjectDto;
import com.smeshariks.pms.dto.SmesharikDto;
import com.smeshariks.pms.entities.*;
import com.smeshariks.pms.services.*;
import com.smeshariks.pms.utils.CostValidator;
import com.smeshariks.pms.utils.DateValidator;
import com.smeshariks.pms.utils.SmesharikDtoConverter;
import com.smeshariks.pms.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

@Controller
@RequestMapping("/project")
public class ProjectController {

    private static final String ALL = "all";
    private static final String ACTIVE = "active";
    private static final String INACTIVE = "inactive";
    private static final String COMPLETED = "completed";
    private static final String REJECTED = "rejected";

    private ProjectService projectService;
    private TimingsService timingsService;
    private ProjectStatusService projectStatusService;
    private TaskService taskService;
    private TaskStatusService taskStatusService;

    @Autowired
    private UserService userService;

    @Autowired
    public ProjectController(ProjectService projectService, TimingsService timingsService,
                             ProjectStatusService projectStatusService, TaskService taskService,
                             TaskStatusService taskStatusService) {
        this.projectService = projectService;
        this.timingsService = timingsService;
        this.projectStatusService = projectStatusService;
        this.taskService = taskService;
        this.taskStatusService = taskStatusService;
    }

    @GetMapping("/create")
    public String getProjectForm(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SmesharikDto smesharikDto = new SmesharikDto();
        smesharikDto.setId(user.getId());
        smesharikDto.setName(user.getName());
        smesharikDto.setUserRole(user.getUserRole());
        model.addAttribute("user", smesharikDto);
        model.addAttribute("projectForm", new Project());
        return "new_project";
    }

    @PostMapping("/create")
    public String createProject(@ModelAttribute("projectForm") @Valid Project projectForm, BindingResult bindingResult,
                                Model model) {


        Timings timings = new Timings();

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SmesharikDto smesharikDto = new SmesharikDto();
        smesharikDto.setId(user.getId());
        smesharikDto.setName(user.getName());
        smesharikDto.setUserRole(user.getUserRole());


        DateValidator dateValidator = new DateValidator(projectForm.getTsStart(), projectForm.getTsStop());
        Validator costValidator = new CostValidator(projectForm.getCost());

        if(dateValidator.isValid() && costValidator.isValid()) {

            timings.setStartTime(dateValidator.getTimestamps()[0]);
            timings.setDeadTime(dateValidator.getTimestamps()[1]);

            projectForm.setOwner(user);
            projectForm.setStartTime(dateValidator.getTimestamps()[0]);
            projectForm.setDeadTime(dateValidator.getTimestamps()[1]);

            projectForm.setAdded(new Timestamp(new Date().getTime()));
            projectForm.setCurrentStatus(Statuses.NOT_APPROVED.getValue());
            ProjectStatus projectStatus = new ProjectStatus();
            projectStatus.setStatus(Statuses.NOT_APPROVED.getName());
            projectStatus.setTimestamp(new Timestamp(new Date().getTime()));
            projectStatus.setProject(projectForm);

            if(!projectService.saveProject(projectForm)) {
                model.addAttribute("user", smesharikDto);
                model.addAttribute("projectForm", projectForm);
                model.addAttribute("invalidTitle", true);
                return "new_project";
            }

            projectStatusService.saveProjectStatus(projectStatus);

        } else {
            model.addAttribute("user", smesharikDto);
            model.addAttribute("projectForm", projectForm);

            if(!dateValidator.isValid()) {

                model.addAttribute("timingsError", true);
                return "new_project";

            } else if(!costValidator.isValid()) {
                model.addAttribute("costError", true);
                return "new_project";
            }

        }

        return "redirect:/main";
    }


    @GetMapping("/get")
    public String getProjects(@RequestParam(name = "type") String type, Model model) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SmesharikDto smesharikDto = new SmesharikDto();
        smesharikDto.setId(user.getId());
        smesharikDto.setName(user.getName());
        smesharikDto.setUserRole(user.getUserRole());

        List<Project> projects = new ArrayList<>();

        if(user.getUserRole() == UserRole.ADMIN) {
            switch (type) {
                case ALL:
                    projects = projectService.findAllProjects();
                    break;

                case ACTIVE:
                    projects = projectService.findByStatus(Statuses.IN_WORK);
                    break;

                case INACTIVE:
                    projects = projectService.findByStatus(Statuses.NOT_APPROVED);
                    break;

                case COMPLETED:
                    projects = projectService.findByStatus(Statuses.COMPLETED);
                    break;

                case REJECTED:
                    projects = projectService.findByStatus(Statuses.REJECTED);
                    break;
            }

        } else {

            switch (type) {
                case ALL:
                    projects = projectService.findByUser(user);
                    break;

                case ACTIVE:
                    projects = projectService.findByUserAndStatus(user, Statuses.IN_WORK);
                    break;

                case INACTIVE:
                    projects = projectService.findByUserAndStatus(user, Statuses.NOT_APPROVED);
                    break;

                case COMPLETED:
                    projects = projectService.findByUserAndStatus(user, Statuses.COMPLETED);
                    break;

                case REJECTED:
                    projects = projectService.findByUserAndStatus(user, Statuses.REJECTED);
                    break;
            }
        }

        model.addAttribute("user", smesharikDto);
        model.addAttribute("currentType", type);
        model.addAttribute("projects", projects);

        return "projects";
    }

    @GetMapping("/update/{id}")
    public String editProject(@PathVariable Integer id, Model model) {

        SmesharikDto smesharikDto = new SmesharikDtoConverter((User)SecurityContextHolder
                .getContext().getAuthentication().getPrincipal()).convert();

        Project project = projectService.findProject(id);
        project.convertToStringTimestamp();
        model.addAttribute("user", smesharikDto);
        model.addAttribute("projectForm", project);
        model.addAttribute("isEdit", true);

        return "new_project";
    }

    @PostMapping("/update/{id}")
    public String updateProject(@PathVariable Integer id, @ModelAttribute("project") Project project, Model model) {

        Timings timings = new Timings();

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SmesharikDto smesharikDto = new SmesharikDto();
        smesharikDto.setId(user.getId());
        smesharikDto.setName(user.getName());
        smesharikDto.setUserRole(user.getUserRole());

        if((project.getTsStart() != null) && (project.getTsStop() != null)) {
            DateValidator dateValidator = new DateValidator(project.getTsStart(), project.getTsStop());
            Validator costValidator = new CostValidator(project.getCost());

            if(dateValidator.isValid() && costValidator.isValid()) {

                timings.setStartTime(dateValidator.getTimestamps()[0]);
                timings.setDeadTime(dateValidator.getTimestamps()[1]);

                project.setStartTime(dateValidator.getTimestamps()[0]);
                project.setDeadTime(dateValidator.getTimestamps()[1]);


                ProjectStatus projectStatus = new ProjectStatus();
                projectStatus.setStatus(project.getStatus().getName());
                projectStatus.setTimestamp(new Timestamp(new Date().getTime()));
                projectStatus.setProject(project);

                projectService.updateProject(project);
                projectStatusService.saveProjectStatus(projectStatus);

            }
        } else {
            ProjectStatus projectStatus = new ProjectStatus();
            projectStatus.setStatus(project.getStatus().getName());
            projectStatus.setTimestamp(new Timestamp(new Date().getTime()));
            projectStatus.setProject(project);

            projectService.updateProject(project);
            projectStatusService.saveProjectStatus(projectStatus);
        }

        return "redirect:/project/" + project.getId();
    }


    @GetMapping("/{id}")
    public String getProject(@PathVariable Integer id,  Model model) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SmesharikDto smesharikDto = new SmesharikDto();
        smesharikDto.setId(user.getId());
        smesharikDto.setName(user.getName());
        smesharikDto.setUserRole(user.getUserRole());

        Project project = projectService.findProject(id);
        /*
        if(action != null) {
            switch (action) {
                case "confirm":
                    project.setCurrentStatus(Statuses.IN_WORK.getValue());
                    projectService.updateProject(project);

                    ProjectStatus projectStatus = new ProjectStatus();
                    projectStatus.setStatus(Statuses.IN_WORK.getName());
                    projectStatus.setTimestamp(new Timestamp(new Date().getTime()));
                    projectStatus.setProject(project);
                    projectStatusService.saveProjectStatus(projectStatus);
                    break;
            }
        }
        */
        List<ProjectStatus> statuses = new ArrayList<>();
        statuses = projectStatusService.findProjectStatuses(project);

        //Поиск возможных исполнителей для новой задачи
        List<User> executors = userService.findUsersByRole(new Role(3, "ROLE_WORKER"));
        List<Task> tasks = taskService.findTasksByProject(project);

        model.addAttribute("tasks", tasks);
        model.addAttribute("executors", executors);
        model.addAttribute("user", smesharikDto);
        model.addAttribute("project", project);
        model.addAttribute("projectStatuses", statuses);
        model.addAttribute("taskForm", new Task());
        return "project";

    }


    /*
    @PutMapping("/{id}")
    public String updateProject(@PathVariable Integer id, @RequestParam(name = "action") String action, Model model) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SmesharikDto smesharikDto = new SmesharikDto();
        smesharikDto.setId(user.getId());
        smesharikDto.setName(user.getName());
        smesharikDto.setUserRole(user.getUserRole());

        Project project = projectService.findProject(id);
        project.setCurrentStatus(Statuses.IN_WORK.getValue());
        projectService.updateProject(project);

        ProjectStatus projectStatus = new ProjectStatus();
        projectStatus.setStatus(Statuses.IN_WORK.getName());
        projectStatus.setTimestamp(new Timestamp(new Date().getTime()));
        projectStatus.setProject(project);
        projectStatusService.saveProjectStatus(projectStatus);

        return "redirect:/";
    }

     */
}