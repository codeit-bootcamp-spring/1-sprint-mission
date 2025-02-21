package com.sprint.mission.discodeit.dto.user;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import static com.sprint.mission.discodeit.constant.UserConstant.*;


public record UserUpdateDto(

    String newUsername,
    String newEmail,
    String newPassword
) {
}

