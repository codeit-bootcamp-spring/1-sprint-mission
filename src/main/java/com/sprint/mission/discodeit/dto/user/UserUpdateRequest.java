package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.BinaryContent;

import java.util.UUID;

public record UserUpdateRequest(
        String newUsername,
        String newEmail,
        String newPassword,
        BinaryContent binaryContent
) {
}
