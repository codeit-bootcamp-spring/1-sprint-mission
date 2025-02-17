package com.sprint.mission.discodeit.dto.authService;

import java.util.UUID;

public record LoginResponse(UUID userId, String username, String email, String token) {
}
