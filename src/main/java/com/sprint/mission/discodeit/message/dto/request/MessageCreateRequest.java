package com.sprint.mission.discodeit.message.dto.request;

import java.util.UUID;

public record MessageCreateRequest(
	String content,
	UUID channelId,
	UUID authorId) {
}
