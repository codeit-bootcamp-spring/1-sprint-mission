package com.sprint.mission.discodeit.dto.userstatus;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.Instant;

@Builder
public record UserStatusRequestDto(@NotBlank Instant newLastActiveAt) {
}
