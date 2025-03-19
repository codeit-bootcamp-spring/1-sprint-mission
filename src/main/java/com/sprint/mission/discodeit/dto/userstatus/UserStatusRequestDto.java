package com.sprint.mission.discodeit.dto.userstatus;

import lombok.Builder;

import java.time.Instant;

@Builder
public record UserStatusRequestDto(Instant newLastActiveAt) {
}
