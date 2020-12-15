package com.smeshariks.pms.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "materials")
@Data
@NoArgsConstructor
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * Наименование материала
     */
    private String title;

    /**
     * Описание материала
     */
    private String description;

    /**
     * Количество на складе
     */
    private Integer balance;

    /**
     * Дата последней поставки
     */
    @Column(name = "last_delivery")
    private Timestamp lastDelivery;

    /**
     * Является ли инструментом
     */
    @Column(name = "is_equipment")
    private Integer isEquipment;
}
