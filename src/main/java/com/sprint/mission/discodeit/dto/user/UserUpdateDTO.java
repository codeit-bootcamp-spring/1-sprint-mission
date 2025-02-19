package com.sprint.mission.discodeit.dto.user;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class UserUpdateDTO extends UserBaseDTO{
    private String password;
    private MultipartFile profileImage;

    public UserUpdateDTO(String userName, String userEmail,String password, MultipartFile profileImage) {
        super(userName, userEmail);
        this.password = password;
        this.profileImage = profileImage;
    }
}
