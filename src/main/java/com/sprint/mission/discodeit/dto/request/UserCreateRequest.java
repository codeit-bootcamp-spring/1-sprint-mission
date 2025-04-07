package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
        @NotBlank(message = "Username cannot be blank.")
        @Size(min = 1, max = 50, message = "Username must be between 1 and 50 characters.")
        String username,

        @NotBlank(message = "Email cannot be blank.")
        @Email(message = "Invalid email.")
        String email,

        @NotBlank(message = "Password cannot be blank.")
        @Size(min = 1, max = 60, message = "Password must be between 1 and 60 characters.")
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
                message = "Password must contain at least one letter, one number, and one special character."
        )
        String password
) {

}
