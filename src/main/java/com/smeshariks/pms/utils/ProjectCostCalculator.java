package com.smeshariks.pms.utils;

import com.smeshariks.pms.entities.MaterialRequest;
import com.smeshariks.pms.entities.Task;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ProjectCostCalculator {

    /**
     * Список всех заявок на материалы
     */

    private List<MaterialRequest> materialRequests;

    /**
     * Список всех выполненных задач по проекту
     */
    private List<Task> tasks;

    public ProjectCostCalculator(List<MaterialRequest> materialRequests, List<Task> tasks) {
        this.materialRequests = materialRequests;
        this.tasks = tasks;
    }

    public int calculateMaterialsCost() {

        int totalCost = 0;

        try {
            for(MaterialRequest request : materialRequests) {
                if(request.getMaterial().getIsEquipment() == 0) {
                    //Работаем только с материалами
                    totalCost += request.getQuantity() * request.getMaterial().getCost();
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return totalCost;
    }

    public int calculateWorkingHours() {

        int totalCost = 0;

        try {
            for(Task task : tasks) {
                Date startDate = new Date(task.getStartTime().getTime());
                Date endDate = new Date(task.getDeadTime().getTime());

                long diffs = Math.abs(endDate.getTime() - startDate.getTime());
                long daysNum = TimeUnit.DAYS.convert(diffs, TimeUnit.MILLISECONDS);

                totalCost += daysNum * 2; //2 смешкоина - стоимость 8 часового дня смешарика
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return totalCost;
    }
}
