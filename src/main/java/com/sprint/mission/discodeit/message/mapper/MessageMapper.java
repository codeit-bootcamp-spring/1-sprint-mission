package com.sprint.mission.discodeit.message.mapper;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.sprint.mission.discodeit.message.dto.response.message.MessageResponse;
import com.sprint.mission.discodeit.message.entity.BinaryContent;
import com.sprint.mission.discodeit.message.entity.Message;

@Component
public class MessageMapper {

	public MessageResponse messageToMessageResponse(Message message, List<BinaryContent> attachments) {
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

	public List<MessageResponse> messageListToMessageResponseList(List<Message> messages,
		List<BinaryContent> attachments) {
		List<MessageResponse> responses = new ArrayList<>();
		for (Message message : messages) {
			responses.add(messageToMessageResponse(message, attachments));
		}
		return responses;
	}

	// UUID 변환 전 체크 메서드
	private UUID parseUUID(String uuidStr) {
		if (uuidStr == null || uuidStr.isBlank()) {
			return null; // null을 허용하는 경우
		}
		return UUID.fromString(uuidStr);
	}

	// Instant 변환 전 체크 메서드
	private Instant parseInstant(String instantStr) {
		if (instantStr == null || instantStr.isBlank()) {
			return Instant.now(); // 기본값을 현재 시간으로 설정
		}
		return Instant.parse(instantStr);
	}
}
