package com.sprint.mission.discodeit.dto.channelDto;

import java.util.UUID;

public record CreatePublicChannelRequestDto(UUID ownerId, String name, String explanation) {
}
