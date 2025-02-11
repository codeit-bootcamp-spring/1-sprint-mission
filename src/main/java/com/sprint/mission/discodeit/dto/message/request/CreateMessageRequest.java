package com.sprint.mission.discodeit.dto.message.request;

import java.util.List;
import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

public record CreateMessageRequest(
	String content,
	UUID authorId,
	UUID channelId,
	List<MultipartFile> attachments
) {
}
