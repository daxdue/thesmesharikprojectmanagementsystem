package com.smeshariks.pms.controllers;

import com.smeshariks.pms.dto.MaterialRequestDto;
import com.smeshariks.pms.dto.SmesharikDto;
import com.smeshariks.pms.entities.Material;
import com.smeshariks.pms.entities.MaterialRequest;
import com.smeshariks.pms.entities.Project;
import com.smeshariks.pms.entities.User;
import com.smeshariks.pms.services.MaterialRequestService;
import com.smeshariks.pms.services.MaterialService;
import com.smeshariks.pms.services.ProjectService;
import com.smeshariks.pms.services.UserService;
import com.smeshariks.pms.utils.DateValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.Date;

@Controller
@RequestMapping("/warehouse")
public class WarehouseController {

    private final UserService userService;
    private final ProjectService projectService;
    private final MaterialService materialService;
    private final MaterialRequestService materialRequestService;

    @Autowired
    public WarehouseController(MaterialService materialService, MaterialRequestService materialRequestService,
                               UserService userService, ProjectService projectService) {
        this.materialService = materialService;
        this.materialRequestService = materialRequestService;
        this.userService = userService;
        this.projectService = projectService;
    }

    @PostMapping("/request")
    public String createMaterialRequest(@ModelAttribute("newRequest") @Valid MaterialRequestDto materialRequestDto) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SmesharikDto smesharikDto = new SmesharikDto();
        smesharikDto.setId(user.getId());
        smesharikDto.setName(user.getName());
        smesharikDto.setUserRole(user.getUserRole());

        MaterialRequest materialRequest = new MaterialRequest();

        User customer = userService.findUserById(materialRequestDto.getUser_id());
        if(customer != null) {
            materialRequest.setUser(customer);
        }
        Project project = projectService.findProject(materialRequestDto.getProject_id());
        if(project != null) {
            materialRequest.setProject(project);
        }
        Material material = materialService.findMaterial(materialRequestDto.getMaterial_id());
        if(material != null) {
            materialRequest.setMaterial(material);
        }
        materialRequest.setAdded(new Timestamp(new Date().getTime()));

        DateValidator dateValidator = new DateValidator();
        if(dateValidator.isValidSingle(materialRequestDto.getDeadline())) {
            materialRequest.setDeadline(dateValidator.convertSingle(materialRequestDto.getDeadline()));
        }

        materialRequest.setMoreinfo(materialRequestDto.getMoreinfo());
        materialRequest.setQuantity(materialRequestDto.getQuantity());

        materialRequestService.saveMaterialRequest(materialRequest);

        return "redirect:/task";
    }
}
