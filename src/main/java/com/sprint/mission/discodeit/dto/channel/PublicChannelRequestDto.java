package com.sprint.mission.discodeit.dto.channel;

import lombok.Builder;

import java.util.UUID;

@Builder
public record PublicChannelRequestDto(String serverName, String description, UUID ownerId) {
}
