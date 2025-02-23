package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.UserStatus;
import java.util.UUID;

public record FindUserDto(
    UUID id,
    String name,
    String email,
    UUID profileImageId,
    UserStatus status) {
}
