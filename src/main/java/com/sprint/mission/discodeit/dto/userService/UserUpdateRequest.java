package com.sprint.mission.discodeit.dto.userService;

import java.util.UUID;

public record UserUpdateRequest(
        UUID userId,
        String newUsername,
        String newEmail,
        String newPassword
) {}
