package com.spirnt.mission.discodeit.dto.user;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class UserUpdateRequest {
    private String name;
    private String email;
    private String password;
    private MultipartFile profileImage;   // 프로필 사진

    public UserUpdateRequest(String name, String email,
                             String password, MultipartFile profileImage) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.profileImage = profileImage;
    }
}
