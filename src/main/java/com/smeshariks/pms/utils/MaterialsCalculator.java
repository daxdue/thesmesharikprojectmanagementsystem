package com.smeshariks.pms.utils;

import com.smeshariks.pms.entities.*;
import lombok.Data;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class MaterialsCalculator {

    /**
     * Общий список всех материалов
     */
    private List<Material> materialList;

    /**
     * Список запросов на материалы
     */
    private List<MaterialRequest> materialRequests;

    /**
     * Список позиций, которые необходимо заказать
     */
    private List<MaterialOrder> needsOrderPositions;

    private List<MaterialOrder> orderedMaterial;

    private Order order;

    public MaterialsCalculator(List<Material> materialList, List<MaterialRequest> materialRequests, List<MaterialOrder> materialOrders) {
        this.materialList = materialList;
        this.materialRequests = materialRequests;
        this.orderedMaterial = materialOrders;
    }

    /**
     * Расчет позиций, которые необходимо заказать
     */
    public void calculate() {
        try {
            needsOrderPositions = new ArrayList<>();
            order = new Order();
            int totalCost = 0;
            for(Material material : materialList) {

                //Ищем данный материал в списке запросов
                boolean found = false;

                for(MaterialRequest materialRequest : materialRequests) {
                    if(materialRequest.getMaterial().equals(material)) {
                        //Данный материал есть в списке запросов. Проверяем наличие
                        int orderedQuantity = 0;
                        for(MaterialOrder materialOrder : orderedMaterial) {
                            if(materialOrder.getMaterial().equals(materialRequest.getMaterial())) {
                                orderedQuantity = materialOrder.getQuantity();
                            }
                        }
                        if(material.getBalance() + orderedQuantity < (material.getMinimumVolume() + materialRequest.getQuantity())) {
                            //Кол-во на складе меньше неснижаемого остатка с учетом запроса. Добавляем новый заказ
                            MaterialOrder materialOrder = new MaterialOrder();
                            materialOrder.setMaterial(material);
                            materialOrder.setQuantity(material.getMinimumVolume() + materialRequest.getQuantity());
                            //materialOrder.setAdded(new Timestamp(new Date().getTime()));
                            totalCost += material.getCost() * (material.getMinimumVolume() + materialRequest.getQuantity());
                            needsOrderPositions.add(materialOrder);
                            found = true;
                        }
                    }
                }

                if(!found) {
                    //Материала нет в списке запросов. Проверяем наличие
                    int orderedQuantity = 0;
                    for(MaterialOrder materialOrder : orderedMaterial) {
                        if(materialOrder.getMaterial().equals(material)) {
                            orderedQuantity = materialOrder.getQuantity();
                        }
                    }
                    if(material.getBalance() + orderedQuantity <=  material.getMinimumVolume()) {
                        //Кол-во на складе меньше неснижаемого остатка. Необходимо дозаказать
                        MaterialOrder materialOrder = new MaterialOrder();
                        materialOrder.setMaterial(material);
                        materialOrder.setQuantity(material.getMinimumVolume() + material.getBalance());
                        totalCost += material.getCost() * (material.getMinimumVolume() + material.getBalance());
                        //materialOrder.setAdded(new Timestamp(new Date().getTime()));
                        needsOrderPositions.add(materialOrder);
                    }
                }
                order.setTotal(totalCost);
                order.setMaterialOrders(needsOrderPositions);
                order.setStatus(RequestStatus.REQUESTED.getValue());
                order.setTstamp(new Timestamp(new Date().getTime()));
            }

        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

}
