package com.smeshariks.pms.controllers;

import com.smeshariks.pms.dto.MaterialDto;
import com.smeshariks.pms.dto.MaterialEdit;
import com.smeshariks.pms.dto.MaterialRequestDto;
import com.smeshariks.pms.dto.SmesharikDto;
import com.smeshariks.pms.entities.*;
import com.smeshariks.pms.services.MaterialRequestService;
import com.smeshariks.pms.services.MaterialService;
import com.smeshariks.pms.services.ProjectService;
import com.smeshariks.pms.services.UserService;
import com.smeshariks.pms.utils.DateValidator;
import com.smeshariks.pms.utils.MaterialReserveCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

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
        materialRequest.setStatus(RequestStatus.REQUESTED.getValue());

        materialRequestService.saveMaterialRequest(materialRequest);

        return "redirect:/task";
    }

    @PostMapping("/materials")
    public String createMaterial(@ModelAttribute("newMaterial") MaterialDto materialDto) {

        Material material = new Material();
        material.setTitle(materialDto.getTitle());
        material.setDescription(materialDto.getDescription());
        material.setBalance(materialDto.getBalance());
        material.setCost(materialDto.getCost());
        material.setReserve(0);
        material.setLastDelivery(new Timestamp(new Date().getTime()));
        material.setIsEquipment(materialDto.getIsEquipment());
        material.setMinimumVolume(materialDto.getMinimumVolume());

        materialService.saveMaterial(material);

        return "redirect:/warehouse";
    }

    @GetMapping
    public String warehouseMaterials(Model model) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SmesharikDto smesharikDto = new SmesharikDto();
        smesharikDto.setId(user.getId());
        smesharikDto.setName(user.getName());
        smesharikDto.setUserRole(user.getUserRole());

        List<Material> materials = materialService.findAllMaterialsWithReserve();

        model.addAttribute("user", smesharikDto);
        model.addAttribute("materials", materials);
        model.addAttribute("materialEdit", new MaterialEdit());
        model.addAttribute("newMaterial", new MaterialDto());
        return "materials";
    }

    @PostMapping("/materials/change")
    public String changeMaterial(@ModelAttribute("materialEdit") MaterialEdit materialEdit) {

        Material material = materialService.findMaterial(materialEdit.getId());

        if(material != null) {
            if(materialEdit.getType() == 0) {
                //Decrease materials
                material.setBalance(material.getBalance() - materialEdit.getQuantity());
            } else {
                //Increase materials
                material.setBalance(material.getBalance() + materialEdit.getQuantity());
            }
            material.setLastDelivery(new Timestamp(new Date().getTime()));
            materialService.updateMaterial(material);
        }

        return "redirect:/warehouse/";
    }

}
