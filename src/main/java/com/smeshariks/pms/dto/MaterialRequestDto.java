package com.smeshariks.pms.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
public class MaterialRequestDto {

    private Integer material_id;
    private Integer quantity;
    private String moreinfo;
    private Integer user_id;
    private Integer project_id;
    private String deadline;

}
