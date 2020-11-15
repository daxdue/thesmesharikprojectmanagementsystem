package com.smeshariks.pms.dto;

import com.smeshariks.pms.entities.UserRole;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SmesharikDto {

    private Integer id;
    private String name;
    private String username;
    private UserRole userRole;

}
