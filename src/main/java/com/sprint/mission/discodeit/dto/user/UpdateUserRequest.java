package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.util.type.BinaryContentType;

import java.util.UUID;

public record UpdateUserRequest(String oldPassword ,String newPassword, UUID fileId) {
}
