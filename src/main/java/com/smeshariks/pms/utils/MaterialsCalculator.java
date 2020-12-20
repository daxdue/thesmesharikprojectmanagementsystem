package com.smeshariks.pms.utils;

import com.smeshariks.pms.entities.Material;
import com.smeshariks.pms.entities.MaterialOrder;
import com.smeshariks.pms.entities.MaterialRequest;
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

    public MaterialsCalculator(List<Material> materialList, List<MaterialRequest> materialRequests) {
        this.materialList = materialList;
        this.materialRequests = materialRequests;
    }

    /**
     * Расчет позиций, которые необходимо заказать
     */
    public void calculate() {
        try {
            needsOrderPositions = new ArrayList<>();

            for(Material material : materialList) {

                //Ищем данный материал в списке запросов
                boolean found = false;
                for(MaterialRequest materialRequest : materialRequests) {
                    if(materialRequest.getMaterial().equals(material)) {
                        //Данный материал есть в списке запросов. Проверяем наличие
                        if(material.getBalance() < (material.getMinimumVolume() + materialRequest.getQuantity())) {
                            //Кол-во на складе меньше неснижаемого остатка с учетом запроса. Добавляем новый заказ
                            MaterialOrder materialOrder = new MaterialOrder();
                            materialOrder.setMaterial(material);
                            materialOrder.setQuantity(material.getMinimumVolume() + materialRequest.getQuantity());
                            materialOrder.setAdded(new Timestamp(new Date().getTime()));
                            needsOrderPositions.add(materialOrder);
                            found = true;
                        }
                    }
                }

                if(!found) {
                    //Материала нет в списке запросов. Проверяем наличие
                    if(material.getBalance() < material.getMinimumVolume()) {
                        //Кол-во на складе меньше неснижаемого остатка. Необходимо дозаказать
                        MaterialOrder materialOrder = new MaterialOrder();
                        materialOrder.setMaterial(material);
                        materialOrder.setQuantity(material.getMinimumVolume() + material.getBalance());
                        materialOrder.setAdded(new Timestamp(new Date().getTime()));
                        needsOrderPositions.add(materialOrder);
                    }
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

}
