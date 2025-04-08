package com.sprint.mission.discodeit.dto.authDto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
    @NotBlank(message = "Username cannot be blank.")
    @Size(min = 1, max = 50, message = "username must be between 1 and 50 characters.")
    String username,

    @NotBlank(message = "Password cannot be blank.")
    @Size(min = 1, max = 60, message = "Password must be between 1 and 60 characters.")
    String password
) {

}
