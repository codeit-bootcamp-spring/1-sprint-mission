package com.sprint.mission.discodeit.dto.channel;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.UUID;

@Builder
public record PublicChannelRequestDto(@NotNull String serverName, String description, @NotNull UUID ownerId) {
}
