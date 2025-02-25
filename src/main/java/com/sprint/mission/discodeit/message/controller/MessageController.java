package com.sprint.mission.discodeit.message.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/messages")
@Slf4j
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
	@PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<CommonResponse<MessageResponse>> createMessage(@ModelAttribute CreateMessageRequest request) {
		/*	CreateMessageRequest messageRequest = messageMapper.requestToServiceRequest(request);*/
		Message newMessage = messageService.create(request);
		List<BinaryContent> attachments = binaryContentService.findAllByMessageId(newMessage.getId());
		MessageResponse response = messageMapper.messageToMessageResponse(newMessage, attachments);
		return new ResponseEntity<>(CommonResponse.success("Message created successfully", response), HttpStatus.OK);
	}

	//메시지 수정
	@PutMapping(value = "/{messageId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<CommonResponse<MessageResponse>> updateMessage(
		@PathVariable("messageId") UUID messageId,
		@ModelAttribute UpdateMessageRequest request) {
		Message updatedMessage = messageService.update(messageId, request);
		List<BinaryContent> attachments = binaryContentService.findAllByMessageId(updatedMessage.getId());
		MessageResponse response = messageMapper.messageToMessageResponse(updatedMessage, attachments);
		return new ResponseEntity<>(CommonResponse.success("Message updated successfully", response), HttpStatus.OK);
	}

	//특정 채널의 메시지 목록
	//Todo 만약 private이라면 누가 누구에게 보냈는지도 dto에 담아서 보내줘야될까...?
	@GetMapping(value = "/{channelId}")
	public ResponseEntity<CommonResponse<List<MessageResponse>>> findAllMessagesByChannel(
		@PathVariable("channelId") UUID channelId) {
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
	@DeleteMapping(value = "/{messageId}")
	public ResponseEntity<CommonResponse<Void>> deleteMessage(@PathVariable("messageId") UUID messageId) {
		messageService.delete(messageId);
		return new ResponseEntity<>(CommonResponse.success("Message deleted successfully"), HttpStatus.OK);
	}

}
