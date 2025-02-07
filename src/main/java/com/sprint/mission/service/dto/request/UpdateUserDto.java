package com.sprint.mission.service.dto.request;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UpdateUserDto {


    private String username;
    private String password;
    private String email;

    public UpdateUserDto(String username, String password, String email) {

        this.username = username;
        this.password = password;
        this.email = email;
    }
}
