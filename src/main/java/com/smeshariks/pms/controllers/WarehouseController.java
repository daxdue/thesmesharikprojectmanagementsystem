package com.smeshariks.pms.controllers;

import com.smeshariks.pms.dto.*;
import com.smeshariks.pms.entities.*;
import com.smeshariks.pms.services.*;
import com.smeshariks.pms.utils.DateValidator;
import com.smeshariks.pms.utils.MaterialReserveCalculator;
import com.smeshariks.pms.utils.MaterialsCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/warehouse")
public class WarehouseController {

    private final UserService userService;
    private final ProjectService projectService;
    private final MaterialService materialService;
    private final MaterialRequestService materialRequestService;
    private final OrderService orderService;
    private final MaterialOrderService materialOrderService;

    @Autowired
    public WarehouseController(MaterialService materialService, MaterialRequestService materialRequestService,
                               UserService userService, ProjectService projectService, OrderService orderService,
                               MaterialOrderService materialOrderService) {
        this.materialService = materialService;
        this.materialRequestService = materialRequestService;
        this.userService = userService;
        this.projectService = projectService;
        this.orderService = orderService;
        this.materialOrderService = materialOrderService;
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
        material.setOrdered(0);
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

    @GetMapping("/requests")
    public String materialRequests(Model model) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SmesharikDto smesharikDto = new SmesharikDto();
        smesharikDto.setId(user.getId());
        smesharikDto.setName(user.getName());
        smesharikDto.setUserRole(user.getUserRole());

        List<MaterialRequest> materialRequestsActive = materialRequestService.findRequestsByStatus(RequestStatus.IN_PROCESS);
        List<MaterialRequest> materialRequestsWaits = materialRequestService.findRequestsByStatus(RequestStatus.REQUESTED);

        model.addAttribute("user", smesharikDto);
        model.addAttribute("materialsRequestsActive", materialRequestsActive);
        model.addAttribute("materialsRequestsWaits", materialRequestsWaits);

        return "requests";
    }

    @GetMapping("/requests/{id}/{action}")
    public String updateRequest(@PathVariable Integer id, @PathVariable String action, Model model) {
        MaterialRequest materialRequest = materialRequestService.findRequest(id);
        if(materialRequest != null) {
            switch (action) {
                case "start":
                    materialRequest.setStatus(RequestStatus.IN_PROCESS.getValue());
                    break;

                case "complete":
                    Material material = materialService.findMaterial(materialRequest.getMaterial().getId());
                    if(material != null) {
                        int balance =material.getBalance();
                        material.setBalance(balance - materialRequest.getQuantity());
                        materialService.updateMaterial(material);
                    }
                    materialRequest.setStatus(RequestStatus.COMPLETED.getValue());
                    break;
            }

            materialRequestService.updateMaterialRequest(materialRequest);
        }

        return "redirect:/warehouse/requests";
    }

    @GetMapping("/orders/{id}/{action}")
    public String updateOrder(@PathVariable Integer id, @PathVariable String action, Model model) {
        Order order = orderService.findOrderById(id);
        if(order != null) {
            switch (action) {
                case "start":
                    order.setStatus(RequestStatus.IN_PROCESS.getValue());
                    break;

                case "complete":
                    order.setStatus(RequestStatus.COMPLETED.getValue());
                    break;
            }

            for(MaterialOrder materialOrder : order.getMaterialOrders()) {
                Material material = materialService.findMaterial(materialOrder.getMaterial().getId());
                if(material != null) {
                    //int actualCount = material.getBalance();
                    material.setBalance(material.getBalance() + materialOrder.getQuantity());
                    materialService.updateMaterial(material);
                }
            }
            orderService.updateOrder(order);
        }

        return "redirect:/warehouse";
    }

    @GetMapping("/shipments")
    public String materialOrders(Model model) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SmesharikDto smesharikDto = new SmesharikDto();
        smesharikDto.setId(user.getId());
        smesharikDto.setName(user.getName());
        smesharikDto.setUserRole(user.getUserRole());

        List<Order> ordersActive = orderService.findOrdersByStatus(RequestStatus.IN_PROCESS);

        //Запрошенные материалы
        List<MaterialRequest> materialRequestsWaits = materialRequestService.findRequestsByStatus(RequestStatus.REQUESTED);
        //Все материалы
        List<Material> materials = materialService.findAllMaterials();
        //Все заказы материалов
        List<MaterialOrder> materialOrders = new ArrayList<>();
        List<Order> orders = orderService.findOrdersByStatus(RequestStatus.IN_PROCESS);
        for(Order order : orders) {
            for(MaterialOrder materialOrder : order.getMaterialOrders()) {
                materialOrders.add(materialOrder);
            }
        }

        MaterialsCalculator materialsCalculator = new MaterialsCalculator(materials, materialRequestsWaits, materialOrders);
        materialsCalculator.calculate();

        model.addAttribute("needOrder", materialsCalculator.getOrder());
        model.addAttribute("orders", new OrdersDto());
        model.addAttribute("user", smesharikDto);
        model.addAttribute("ordersActive", ordersActive);

        return "orders";
    }

    @PostMapping("/shipments")
    public String orderMaterials(@ModelAttribute("needOrder") OrdersDto needOrder) {

        //Запрошенные материалы
        List<MaterialRequest> materialRequestsWaits = materialRequestService.findRequestsByStatus(RequestStatus.REQUESTED);
        //Все материалы
        List<Material> materials = materialService.findAllMaterials();
        //Все заказы материалов
        List<MaterialOrder> materialOrders = new ArrayList<>();
        List<Order> orders = orderService.findOrdersByStatus(RequestStatus.IN_PROCESS);
        for(Order order : orders) {
            for(MaterialOrder materialOrder : order.getMaterialOrders()) {
                materialOrders.add(materialOrder);
            }
        }
        MaterialsCalculator materialsCalculator = new MaterialsCalculator(materials, materialRequestsWaits, materialOrders);
        materialsCalculator.calculate();

        materialOrderService.saveList(materialsCalculator.getOrder().getMaterialOrders());
        Order order = materialsCalculator.getOrder();
        order.setStatus(RequestStatus.IN_PROCESS.getValue());
        orderService.save(order);

        return "redirect:/warehouse/shipments";
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
