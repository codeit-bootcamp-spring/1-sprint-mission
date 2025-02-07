package com.sprint.mission.service.dto.request;

import lombok.Getter;

@Getter
public class CreateUserDto {

    private String username;
    private String password;
    private String email;
    // 프로필 이미지
    private byte[] profileImg;

    public CreateUserDto(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
