package com.smeshariks.pms.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.smeshariks.pms.utils.TimestampConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "requests")
@Data
@NoArgsConstructor
public class MaterialRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "material_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Material material;

    private Integer quantity;

    private String moreinfo;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "user_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "project_id")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Project project;

    /**
     * Добавлено
     */
    private Timestamp added;

    /**
     * Время, которому требуется
     */
    private Timestamp deadline;

    /**
     * Статус выполнения
     */
    private Integer status;

    public String getDead() {
        return TimestampConverter.convert(deadline, false);
    }
}
