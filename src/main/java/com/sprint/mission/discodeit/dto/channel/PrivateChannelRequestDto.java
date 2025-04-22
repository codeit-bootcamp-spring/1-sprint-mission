package com.sprint.mission.discodeit.dto.channel;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record PrivateChannelRequestDto(@NotBlank UUID ownerId, List<UUID> participantIds) {
}
