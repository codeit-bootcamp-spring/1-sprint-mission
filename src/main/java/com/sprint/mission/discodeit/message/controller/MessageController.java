package com.sprint.mission.discodeit.message.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sprint.mission.discodeit.global.dto.CommonResponse;
import com.sprint.mission.discodeit.message.dto.request.message.CreateMessageRequest;
import com.sprint.mission.discodeit.message.dto.request.message.UpdateMessageRequest;
import com.sprint.mission.discodeit.message.dto.response.message.MessageResponse;
import com.sprint.mission.discodeit.message.entity.BinaryContent;
import com.sprint.mission.discodeit.message.entity.Message;
import com.sprint.mission.discodeit.message.mapper.MessageMapper;
import com.sprint.mission.discodeit.message.service.BinaryContentService;
import com.sprint.mission.discodeit.message.service.MessageService;

@RestController
@RequestMapping("api/messages")
public class MessageController {

	private final MessageService messageService;
	private final BinaryContentService binaryContentService;
	private final MessageMapper messageMapper;

	public MessageController(MessageService messageService, BinaryContentService binaryContentService,
		MessageMapper messageMapper) {
		this.messageService = messageService;
		this.binaryContentService = binaryContentService;
		this.messageMapper = messageMapper;
	}

	//메시지 보내기
	@RequestMapping(name = "", method = RequestMethod.POST)
	public ResponseEntity<CommonResponse<MessageResponse>> createMessage(@RequestBody CreateMessageRequest request) {
		Message newMessage = messageService.create(request);
		List<BinaryContent> attachments = binaryContentService.findAllByMessageId(newMessage.getId());
		MessageResponse response = messageMapper.messageToMessageResponse(newMessage, attachments);
		return new ResponseEntity<>(CommonResponse.success("Message created successfully", response), HttpStatus.OK);
	}

	//메시지 수정
	@RequestMapping(name = "/{messageId}", method = RequestMethod.PATCH)
	public ResponseEntity<CommonResponse<MessageResponse>> updateMessage(@PathVariable("messageId") UUID messageId,
		@RequestBody UpdateMessageRequest request) {
		Message updatedMessage = messageService.update(messageId, request);
		List<BinaryContent> attachments = binaryContentService.findAllByMessageId(updatedMessage.getId());
		MessageResponse response = messageMapper.messageToMessageResponse(updatedMessage, attachments);
		return new ResponseEntity<>(CommonResponse.success("Message updated successfully", response), HttpStatus.OK);
	}

	//특정 채널의 메시지 목록
	@RequestMapping(name = "", method = RequestMethod.GET)
	public ResponseEntity<CommonResponse<List<MessageResponse>>> findAllMessagesByChannel(
		@PathVariable("id") UUID channelId) {
		List<Message> messages = messageService.findAllByChannelId(channelId);
		List<BinaryContent> attachments = new ArrayList<>();
		for (Message message : messages) {
			// 각 메시지에 연결된 첨부파일을 binaryContentService를 통해 조회
			attachments = binaryContentService.findAllByMessageId(message.getId());
		}
		List<MessageResponse> responses = messageMapper.messageListToMessageResponseList(messages, attachments);
		return new ResponseEntity<>(CommonResponse.success("Find Messages By Channel successfully", responses),
			HttpStatus.OK);
	}

	//메시지 삭제
	@RequestMapping(name = "/{messageId}", method = RequestMethod.DELETE)
	public ResponseEntity<CommonResponse<Void>> deleteMessage(@PathVariable("messageId") UUID messageId) {
		messageService.delete(messageId);
		return new ResponseEntity<>(CommonResponse.success("Message deleted successfully"), HttpStatus.OK);
	}

}
