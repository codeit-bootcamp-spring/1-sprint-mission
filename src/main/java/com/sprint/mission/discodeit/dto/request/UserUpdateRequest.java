package com.sprint.mission.discodeit.dto.request;

import java.util.UUID;

public record UserUpdateRequest(
        UUID id,
        String name,
        int age,
        String newUsername,
        String newEmail,
        String newPassword,
        byte[] profileImageUrl
) {
}
