package com.smeshariks.pms.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProjectDto {

    private String title;
    private String description;
    private String cost;
    private String startTime;
    private String deadTime;
}
