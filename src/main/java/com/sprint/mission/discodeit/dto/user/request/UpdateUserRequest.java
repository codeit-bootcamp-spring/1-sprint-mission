package com.sprint.mission.discodeit.dto.user.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UpdateUserRequest(

    @NotBlank
    String newUsername,

    @NotBlank
    @Email
    String newEmail,

    @NotBlank
    String newPassword
) {

}
