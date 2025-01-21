package com.sprint.mission.discodeit.dto;

import lombok.Getter;

@Getter
public class UserDto {
    private String loginId;
    private String password;
    private String name;

    private UserDto(String loginId, String password, String name) {
        this.loginId = loginId;
        this.password = password;
        this.name = name;
    }

    public static UserDto of(String loginId, String password, String name) {
        return new UserDto(loginId, password, name);
    }

}
