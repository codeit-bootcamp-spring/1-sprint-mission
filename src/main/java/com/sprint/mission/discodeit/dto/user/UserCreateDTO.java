package com.sprint.mission.discodeit.dto.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class UserCreateDTO extends UserBaseDTO{
    private String password;
    private MultipartFile profileImage; // 프로필 사진

    public UserCreateDTO(String userName, String userEmail, String password, MultipartFile profileImage) {
        super(userName, userEmail);
        this.password = password;
        this.profileImage = profileImage;
    }
}
