package com.sprint.mission.discodeit.dto.message.response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import com.sprint.mission.discodeit.dto.binaryContent.response.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.Message;

public record MessageResponse(
	UUID id,
	String content,
	UUID authorId,
	UUID channelId,
	List<BinaryContentResponse> attachments,
	Instant createdAt,
	Instant updatedAt
) {
	public static MessageResponse from(Message message, List<BinaryContentResponse> attachments) {
		return new MessageResponse(
			message.getId(),
			message.getContent(),
			message.getAuthorId(),
			message.getChannelId(),
			attachments,
			message.getCreatedAt(),
			message.getUpdatedAt()
		);
	}
}
