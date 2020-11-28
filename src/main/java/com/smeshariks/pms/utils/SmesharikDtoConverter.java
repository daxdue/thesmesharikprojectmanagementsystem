package com.smeshariks.pms.utils;

import com.smeshariks.pms.dto.SmesharikDto;
import com.smeshariks.pms.entities.User;

public class SmesharikDtoConverter {

    private User user;
    private SmesharikDto smesharikDto;

    public SmesharikDtoConverter(User user) {
        this.user = user;
        this.smesharikDto = smesharikDto;
    }

    public SmesharikDto convert() {
        SmesharikDto smesharikDto = new SmesharikDto();
        smesharikDto.setId(user.getId());
        smesharikDto.setName(user.getName());
        smesharikDto.setUserRole(user.getUserRole());
        return smesharikDto;
    }
}
