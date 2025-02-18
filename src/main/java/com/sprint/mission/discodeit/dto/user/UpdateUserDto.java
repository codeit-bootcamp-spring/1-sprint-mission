package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.BinaryContent;
import java.util.Optional;
import java.util.UUID;

public record UpdateUserDto(
    UUID id,
    String newName,
    String newPassword,
    Optional<BinaryContent> profileImage
) {
}
