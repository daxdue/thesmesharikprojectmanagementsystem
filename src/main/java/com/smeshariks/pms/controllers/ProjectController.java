package com.smeshariks.pms.controllers;

import com.smeshariks.pms.dto.ProjectDto;
import com.smeshariks.pms.dto.SmesharikDto;
import com.smeshariks.pms.entities.*;
import com.smeshariks.pms.services.ProjectService;
import com.smeshariks.pms.services.ProjectStatusService;
import com.smeshariks.pms.services.TimingsService;
import com.smeshariks.pms.utils.CostValidator;
import com.smeshariks.pms.utils.DateValidator;
import com.smeshariks.pms.utils.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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

    private ProjectService projectService;
    private TimingsService timingsService;
    private ProjectStatusService projectStatusService;

    @Autowired
    public ProjectController(ProjectService projectService, TimingsService timingsService,
                             ProjectStatusService projectStatusService) {
        this.projectService = projectService;
        this.timingsService = timingsService;
        this.projectStatusService = projectStatusService;
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
        }

        model.addAttribute("user", smesharikDto);
        model.addAttribute("currentType", type);
        model.addAttribute("projects", projects);

        return "projects";
    }
}
