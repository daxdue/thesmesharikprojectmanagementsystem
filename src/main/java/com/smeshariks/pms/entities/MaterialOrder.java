package com.smeshariks.pms.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "materials_orders")
@Data
@NoArgsConstructor
@Component
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


    @ManyToMany
    @JoinTable(name = "orders_materials",
        joinColumns = @JoinColumn(name = "material_order_id"),
        inverseJoinColumns = @JoinColumn(name = "order_id"))
    private List<Order> orders;
}
