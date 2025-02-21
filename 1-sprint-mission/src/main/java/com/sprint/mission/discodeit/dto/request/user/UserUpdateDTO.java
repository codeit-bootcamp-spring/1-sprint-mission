package com.sprint.mission.discodeit.dto.request.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.UUID;

public record UserUpdateDTO(
    UUID id,

    @NotBlank(message = "Username is required") String newUsername,

    @NotBlank(message = "Email is required") @Email(message = "Invalid email format") String newEmail,

    @Size(min = 6, message = "Password must be at least 6 char") String newPassword
) {

}
