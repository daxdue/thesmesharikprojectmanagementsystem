package com.smeshariks.pms.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
public class MaterialOrder {

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

    private Timestamp added;
}
