package com.sprint.mission.discodeit.dto.authService;

public record LoginRequest(
        String email,
        String password
) {}
