package com.sprint.mission.discodeit.dto.message;

import java.util.UUID;

public record MessageRequestDto(String content, UUID authorId, UUID channelId) {
}
