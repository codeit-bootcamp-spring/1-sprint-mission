package com.sprint.mission.discodeit.dto.readstatus;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record ReadStatusRequestDto(@NotBlank UUID userId, @NotBlank UUID channelId, @NotBlank Instant lastReadAt) {
}
