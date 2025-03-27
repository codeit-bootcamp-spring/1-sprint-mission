package com.sprint.mission.discodeit.dto.channel;

import java.util.UUID;

public record UpdatePublicChannelRequestDto(UUID id, String category, String name, String explanation) {
}
