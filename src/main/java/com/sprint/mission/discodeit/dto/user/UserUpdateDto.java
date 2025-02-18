package com.sprint.mission.discodeit.dto.user;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import static com.sprint.mission.discodeit.constant.UserConstant.*;


public record UserUpdateDto(

    @Nullable
    @Size(min = USERNAME_MIN_LENGTH, max = USERNAME_MAX_LENGTH, message = "username 은 2 ~ 10글자 사이여야 합니다.")
    String username,
    @NotBlank
    String inputPassword,
    @Nullable
    String password,
    @Nullable
    @Pattern(regexp = EMAIL_REGEX)
    String email,
    @Nullable
    @Size(
        min = NICKNAME_MIN_LENGTH,
        max = NICKNAME_MAX_LENGTH
    )
    String nickname,
    @Nullable
    @Pattern(regexp = PHONE_REGEX)
    String phoneNumber,
    @Nullable
    String description,
    @Nullable
    MultipartFile profileImage
) {
}

