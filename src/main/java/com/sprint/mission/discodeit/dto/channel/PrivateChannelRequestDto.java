package com.sprint.mission.discodeit.dto.channel;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record PrivateChannelRequestDto(UUID ownerId, List<UUID> participantIds) {
}
