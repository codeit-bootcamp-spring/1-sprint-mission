package com.sprint.mission.discodeit.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sprint.mission.discodeit.dto.message.request.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageRequest;
import com.sprint.mission.discodeit.dto.message.response.MessageResponse;
import com.sprint.mission.discodeit.global.dto.CommonResponse;
import com.sprint.mission.discodeit.service.MessageService;

@RestController
@RequestMapping("api/message")
public class MessageController {

	private final MessageService messageService;

	public MessageController(MessageService messageService) {
		this.messageService = messageService;
	}

	//메시지 보내기
	@RequestMapping(name = "/create", method = RequestMethod.POST)
	public ResponseEntity<CommonResponse<MessageResponse>> createMessage(@RequestBody CreateMessageRequest request) {
		MessageResponse response = messageService.create(request);
		return new ResponseEntity<>(CommonResponse.success("Message created successfully", response), HttpStatus.OK);
	}

	//메시지 수정
	@RequestMapping(name = "/update/{id}", method = RequestMethod.PATCH)
	public ResponseEntity<CommonResponse<MessageResponse>> updateMessage(@PathVariable("id") UUID messageId,
		@RequestBody UpdateMessageRequest request) {
		MessageResponse response = messageService.update(messageId, request);
		return new ResponseEntity<>(CommonResponse.success("Message updated successfully", response), HttpStatus.OK);
	}

	//특정 채널의 메시지 목록
	@RequestMapping(name = "/find-messages/{id}", method = RequestMethod.GET)
	public ResponseEntity<CommonResponse<List<MessageResponse>>> findAllMessagesByChannel(
		@PathVariable("id") UUID channelId) {
		List<MessageResponse> messages = messageService.findAllByChannelId(channelId);
		return new ResponseEntity<>(CommonResponse.success("Find Messages By Channel successfully", messages),
			HttpStatus.OK);
	}

	//메시지 삭제
	@RequestMapping(name = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<CommonResponse<Void>> deleteMessage(@PathVariable("id") UUID messageId) {
		messageService.delete(messageId);
		return new ResponseEntity<>(CommonResponse.success("Message deleted successfully"), HttpStatus.OK);
	}

}
