package com.sprint.mission.discodeit.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;


@Getter
@AllArgsConstructor
public class UserUpdateRequestDto {
    private String newName;
    private String newEmail;
    private String newPassword;
    private MultipartFile NewProfileImage;
}
