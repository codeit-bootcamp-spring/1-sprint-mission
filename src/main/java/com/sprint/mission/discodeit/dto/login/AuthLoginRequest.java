package com.sprint.mission.discodeit.dto.login;


import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequest(
        String username,

        String password
) {}
