package com.smeshariks.pms.entities;

import com.smeshariks.pms.utils.TimestampConverter;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

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
     * Стоимость единицы
     */
    private Integer cost;

    /**
     * Минимальный неснижаемый остаток
     */
    @Column(name = "min_volume")
    private Integer minimumVolume;

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

    @Transient
    private Integer reserve;

    public String getDelivery() {
        return TimestampConverter.convert(lastDelivery, true);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Material material = (Material) o;
        return id.equals(material.id) &&
                title.equals(material.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }
}
