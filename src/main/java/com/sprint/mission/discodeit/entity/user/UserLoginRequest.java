package com.sprint.mission.discodeit.entity.user;

import jakarta.validation.constraints.NotBlank;

import java.time.Instant;

public record UserLoginRequest(
        @NotBlank
        String userName,

        @NotBlank
        String userPassword,

        Instant requestLoginAt
) {
}
