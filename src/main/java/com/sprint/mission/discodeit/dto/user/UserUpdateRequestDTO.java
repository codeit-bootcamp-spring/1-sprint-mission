package com.sprint.mission.discodeit.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class UserUpdateRequestDTO {
    private UUID userId;
    private String newName;
    private String newEmail;
    private String newPassword;
    private byte[] newProfileImage;
}
