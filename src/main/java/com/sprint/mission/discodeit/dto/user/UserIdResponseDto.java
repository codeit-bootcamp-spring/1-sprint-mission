package com.sprint.mission.discodeit.dto.user;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UserIdResponseDto(UUID id, String username) {
}
