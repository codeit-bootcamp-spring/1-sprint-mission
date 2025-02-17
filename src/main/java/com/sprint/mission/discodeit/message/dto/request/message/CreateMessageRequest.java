package com.sprint.mission.discodeit.message.dto.request.message;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public record CreateMessageRequest(
	String content,
	UUID authorId,
	UUID channelId,
	//privateChannel 생성을 위해 받는 사람 id 추가
	UUID receiverId,
	Instant createdAt,
	List<MultipartFile> attachments
) {
}
