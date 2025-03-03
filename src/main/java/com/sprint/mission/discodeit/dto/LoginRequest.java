package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record LoginRequest(
        UUID userId,
        String password
) {}
