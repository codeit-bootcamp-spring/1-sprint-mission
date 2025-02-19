package com.sprint.mission.discodeit.dto.user;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@Getter
public class UserUpdateDTO extends UserBaseDTO{
    private String password;
    private MultipartFile profileImage;

    public UserUpdateDTO(UUID id, String userName, String userEmail, String password, MultipartFile profileImage) {
        super(id, userName, userEmail);
        this.password = password;
        this.profileImage = profileImage;
    }
}
