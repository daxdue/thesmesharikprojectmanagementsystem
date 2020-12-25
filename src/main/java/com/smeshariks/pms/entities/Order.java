package com.smeshariks.pms.entities;

import com.smeshariks.pms.utils.TimestampConverter;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Entity
@Table(name = "orders", schema = "public")
@Data
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Timestamp tstamp;

    private Integer status;

    @Transient
    private Integer total;

    @ManyToMany
    @JoinTable(name = "orders_materials",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "material_order_id"))
    private List<MaterialOrder> materialOrders;


    public String getOrderDate() {
        return TimestampConverter.convert(tstamp, true);
    }
}
