package com.smeshariks.pms.dto;

import com.smeshariks.pms.entities.MaterialOrder;
import lombok.*;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdersDto {

    private List<MaterialOrder> materialOrders;
}
