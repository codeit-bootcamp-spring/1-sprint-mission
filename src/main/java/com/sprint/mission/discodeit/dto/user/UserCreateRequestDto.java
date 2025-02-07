package com.sprint.mission.discodeit.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class UserCreateRequestDto {
    private String name;
    private String email;
    private String password;
    private byte[] profileImage;
    private String status;

    public UserCreateRequestDto(String name, String email, String password, byte[] profileImage, String status) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.profileImage = profileImage;
        this.status = status;
    }
}
