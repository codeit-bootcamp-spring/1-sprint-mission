package com.sprint.mission.discodeit.dto.userDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
    @NotBlank(message = "Username cannot be blank.")
    @Size(min = 1, max = 50, message = "Username must be between 1 and 50 characters.")
    String newUsername,

    @NotBlank(message = "Email cannot be blank.")
    @Email(message = "Invalid email.")
    String newEmail,

    @NotBlank(message = "Password cannot be blank.")
    @Size(min = 1, max = 60, message = "Password must be between 1 and 60 characters.")
    @Pattern(
        regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
        message = "Password must contain at least one letter, one number, and one special character."
    )
    String newPassword
) {

}
