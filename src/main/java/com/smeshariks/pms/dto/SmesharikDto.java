package com.smeshariks.pms.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SmesharikDto {

    private Integer id;
    private String name;
    private String username;
}
