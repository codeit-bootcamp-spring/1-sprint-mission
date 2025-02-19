package com.sprint.mission.discodeit.dto.binaryContent.response;

import java.time.Instant;
import java.util.UUID;

import com.sprint.mission.discodeit.entity.BinaryContentType;

public record BinaryContentResponse(
	UUID id,
	Instant createdAt,
	UUID authorId,
	UUID messageId,
	byte[] content,
	String contentType,
	String fileName,
	long fileSize,
	BinaryContentType binaryContentType
) {
}
