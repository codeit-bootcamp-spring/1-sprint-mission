package com.sprint.mission.discodeit.dto.user;

import static com.sprint.mission.discodeit.constant.UserConstant.PASSWORD_MIN_LENGTH;
import static com.sprint.mission.discodeit.constant.UserConstant.USERNAME_MAX_LENGTH;
import static com.sprint.mission.discodeit.constant.UserConstant.USERNAME_MIN_LENGTH;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateDto(
    @NotBlank(message = "username cannot be empty")
    @Size(
        min = USERNAME_MIN_LENGTH,
        max = USERNAME_MAX_LENGTH,
        message = "username must be between " + USERNAME_MIN_LENGTH + " ~ " + USERNAME_MAX_LENGTH
    )
    String newUsername,
    @NotBlank(message = "email cannot be empty")
    @Email(message = "invalid email format")
    String newEmail,
    @NotBlank(message = "password cannot be empty")
    @Size(min = PASSWORD_MIN_LENGTH, message = "password must at least be " + PASSWORD_MIN_LENGTH
        + " characters")
    String newPassword
) {

}

