package com.smeshariks.pms.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MaterialDto {

    private String title;
    private String description;
    private Integer balance;
    private Integer cost;
    private Integer minimumVolume;
    private Integer isEquipment;
}
