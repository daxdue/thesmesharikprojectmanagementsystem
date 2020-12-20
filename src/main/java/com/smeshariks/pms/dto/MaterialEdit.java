package com.smeshariks.pms.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MaterialEdit {

    private Integer id;
    private Integer quantity;
    private Integer type;
}
