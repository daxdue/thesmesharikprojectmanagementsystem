package com.smeshariks.pms.controllers;

import com.smeshariks.pms.dto.SmesharikDto;
import com.smeshariks.pms.entities.*;
import com.smeshariks.pms.services.*;
import com.smeshariks.pms.utils.MaterialsCalculator;
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
    private final MaterialService materialService;
    private final MaterialRequestService materialRequestService;
    private final MaterialOrderService materialOrderService;
    private final OrderService orderService;

    @Autowired
    public MainController(ProjectService projectService, TaskService taskService, UserService userService,
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

    @GetMapping("/home")
    public String homePage(Model model) {
        return "home";
    }

    @GetMapping("/chat")
    public String chatPage(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SmesharikDto smesharikDto = new SmesharikDto();
        smesharikDto.setUsername(user.getUsername());
        smesharikDto.setId(user.getId());
        smesharikDto.setName(user.getName());
        smesharikDto.setUserRole(user.getUserRole());
        model.addAttribute("user", smesharikDto);
        if(smesharikDto.getUsername().equals("pinhead")) {
            model.addAttribute("friend", "barash");
        } else {
            model.addAttribute("friend", "pinhead");
        }
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
            List<User> warehousemans = userService.findAllByRoleName(UserRole.WAREHOUSEMAN.getDatabaseRole());
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

                model.addAttribute("workersAmount", allWorkersAmount.size() + warehousemans.size());
                model.addAttribute("freeWorkers", freeWorkers);
                model.addAttribute("busyWorkers", busyWorkers);
            }

            model.addAttribute("activeProjects", activeProjects);
            model.addAttribute("notApprovedProjects", notApproved);
            model.addAttribute("completedProjects", completed);


        } else if(user.getUserRole() == UserRole.WAREHOUSEMAN){

            //Заявки
            List<MaterialRequest> materialRequestsActive = materialRequestService.findRequestsByStatus(RequestStatus.IN_PROCESS);
            //  List<MaterialRequest> materialRequestsWaits = materialRequestService.findRequestsByStatus(RequestStatus.REQUESTED);

            //Общее число существующих позиций
            long positionQuantity = materialService.count();

            //Запрошенные материалы
            List<MaterialRequest> materialRequestsWaits = materialRequestService.findRequestsByStatus(RequestStatus.REQUESTED);
            //Все материалы
            List<Material> materials = materialService.findAllMaterials();
            //Все заказы материалов
            List<MaterialOrder> materialOrders = new ArrayList<>();
            List<Order> orders = orderService.findOrdersByStatus(RequestStatus.IN_PROCESS);
            List<Project> projects = projectService.findByStatus(Statuses.IN_DEVIVERY);

            int deliveries = 0;
            int samovivoz = 0;

            for(Project project : projects) {
                try {
                    if(project.getDeliveryAddress() == null) {
                        samovivoz++;
                    } else {
                        deliveries++;
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }

            for(Order order : orders) {
                for(MaterialOrder materialOrder : order.getMaterialOrders()) {
                    materialOrders.add(materialOrder);
                }
            }
            MaterialsCalculator materialsCalculator = new MaterialsCalculator(materials, materialRequestsWaits, materialOrders);
            materialsCalculator.calculate();

            model.addAttribute("needOrder", materialsCalculator.getOrder());
            model.addAttribute("orders", orders.size());
            model.addAttribute("positions", positionQuantity);
            model.addAttribute("activeRequests", materialRequestsActive.size());
            model.addAttribute("waitRequests", materialRequestsWaits.size());
            model.addAttribute("deliveries", deliveries);
            model.addAttribute("samovivoz", samovivoz);

            //Остатки по складу


        } else if(user.getUserRole() == UserRole.CUSTOMER) {

            allProjects = projectService.findByUser(user);
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
                model.addAttribute("activeProjects", activeProjects);
                model.addAttribute("notApprovedProjects", notApproved);
                model.addAttribute("completedProjects", completed);
            }


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


    @GetMapping("/team")
    public String getTeam(Model model) {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        SmesharikDto smesharikDto = new SmesharikDto();
        smesharikDto.setId(user.getId());
        smesharikDto.setName(user.getName());
        smesharikDto.setUserRole(user.getUserRole());
        model.addAttribute("user", smesharikDto);

        List<User> workers = userService.findUsersByRole(new Role(3, "ROLE_WORKER"));
        List<User> warehouseman = userService.findUsersByRole(new Role(5, "ROLE_WAREHOUSEMAN"));
        List<User> managers = userService.findUsersByRole(new Role(4, "ROLE_MANAGER"));

        List<User> users = new ArrayList<>();
        users.addAll(workers);
        users.addAll(warehouseman);
        users.addAll(managers);

        List<SmesharikDto> team = new ArrayList<>();

        for(User usr : users) {
            SmesharikDto smesharikDto1 = new SmesharikDto();
            smesharikDto1.setUsername(usr.getUsername());
            smesharikDto1.setId(usr.getId());
            smesharikDto1.setName(usr.getName());
            smesharikDto1.setUserRole(usr.getUserRole());
            team.add(smesharikDto1);
        }

        model.addAttribute("team", team);

        return "team";
    }
}

