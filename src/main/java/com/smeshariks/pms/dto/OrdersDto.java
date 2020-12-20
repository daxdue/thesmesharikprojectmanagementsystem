package com.smeshariks.pms.dto;

import lombok.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Component
public class OrdersDto {

    @Getter
    @Setter
    private ArrayList<Integer> id;
    @Getter
    @Setter
    private ArrayList<Integer> quantity;
}
