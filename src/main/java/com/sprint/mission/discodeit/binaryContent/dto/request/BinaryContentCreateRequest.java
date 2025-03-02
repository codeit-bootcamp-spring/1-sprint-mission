package com.sprint.mission.discodeit.binaryContent.dto.request;

public record BinaryContentCreateRequest(
	String fileName,
	String contentType,
	byte[] bytes
) {
}
