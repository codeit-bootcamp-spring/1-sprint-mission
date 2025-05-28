package com.sprint.mission.discodeit.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginDto(
    @NotBlank(message = "Empty username not allowed")
    String username,
    @NotBlank(message = "Empty password not allowed")
    String password,
    Boolean rememberMe
) {

}
