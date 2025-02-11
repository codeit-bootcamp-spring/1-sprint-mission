package com.sprint.mission.discodeit.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserUpdateRequestDto {
    private UUID userId;
    private String newName;
    private String newEmail;
    private String newPassword;
    private MultipartFile newProfileImage;
}
