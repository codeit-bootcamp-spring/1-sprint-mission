package com.sprint.mission.discodeit.dto.binaryContent.request;

import java.util.UUID;

import com.sprint.mission.discodeit.entity.BinaryContentType;

public record CreateBinaryContentRequest(
	UUID authorId,
	UUID messageId,
	byte[] content,
	String contentType,
	String fileName,
	long fileSize,
	BinaryContentType binaryContentType
) {
}
