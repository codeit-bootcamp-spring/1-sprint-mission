package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.UUID;

public record UserResponse(UUID id, String username, String email, UserStatus status, BinaryContent profileImage) {
}

