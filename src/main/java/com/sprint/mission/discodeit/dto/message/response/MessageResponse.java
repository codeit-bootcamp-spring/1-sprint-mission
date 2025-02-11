package com.sprint.mission.discodeit.dto.message.response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.dto.binaryContent.response.BinaryContentResponse;

public record MessageResponse(
	UUID id,
	String content,
	UUID authorId,
	UUID channelId,
	List<BinaryContentResponse> attachments,
	Instant createdAt,
	Instant updatedAt
) {
}
