package com.sprint.mission.discodeit.message.mapper;

import java.util.ArrayList;
import java.util.List;

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
}
