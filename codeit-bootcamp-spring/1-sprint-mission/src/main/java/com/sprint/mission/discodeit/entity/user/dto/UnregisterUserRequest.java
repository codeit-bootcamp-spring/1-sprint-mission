package com.sprint.mission.discodeit.entity.user.dto;

import java.util.UUID;

public record UnregisterUserRequest(UUID id, String username) {
}
