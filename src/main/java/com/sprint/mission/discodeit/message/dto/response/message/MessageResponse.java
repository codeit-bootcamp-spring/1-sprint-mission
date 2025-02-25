package com.sprint.mission.discodeit.message.dto.response.message;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.message.entity.BinaryContent;

public record MessageResponse(
	UUID id,
	String content,
	UUID authorId,
	UUID channelId,
	List<BinaryContent> attachments,
	Instant createdAt,
	Instant updatedAt
) {
}
