package com.sprint.mission.discodeit.dto.user;

import java.util.UUID;

public record UpdateUserDto(
    UUID id,
    String newName,
    String newPassword,
    UUID profileImageId
) {
}
