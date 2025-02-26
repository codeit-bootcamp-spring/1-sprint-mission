package com.sprint.mission.discodeit.dto.channel;

import java.util.UUID;

public record UpdatePublicChannelRequestDto(UUID id, String updateCategory, String updateName, String updateExplanation) {
}
