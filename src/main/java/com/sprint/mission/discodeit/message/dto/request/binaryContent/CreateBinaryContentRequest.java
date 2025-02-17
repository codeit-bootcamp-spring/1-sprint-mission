package com.sprint.mission.discodeit.message.dto.request.binaryContent;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import com.sprint.mission.discodeit.message.entity.BinaryContentType;

public record CreateBinaryContentRequest(
	UUID authorId,
	UUID messageId,
	MultipartFile file,  // byte[] content 대신 MultipartFile 사용
	BinaryContentType binaryContentType
) {
}
