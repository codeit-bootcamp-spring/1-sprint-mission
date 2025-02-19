package com.sprint.mission.discodeit.dto.user;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class UserCreateRequestDto {
    private String name;
    private String email;
    private String password;
    private MultipartFile profileImage;

    public UserCreateRequestDto(String name, String email, String password, MultipartFile profileImage) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.profileImage = profileImage;
    }
}
