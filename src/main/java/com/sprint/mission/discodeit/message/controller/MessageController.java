package com.sprint.mission.discodeit.message.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sprint.mission.discodeit.binaryContent.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.message.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.message.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.message.entity.Message;
import com.sprint.mission.discodeit.message.service.MessageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/messages")
@Slf4j
@Tag(name = "Message", description = "메시지 관련 API")
public class MessageController {

	private final MessageService messageService;

	//메시지 보내기
	@Operation(summary = "메시지 생성", description = "메시지 생성 API")
	@PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Message> create(
		@RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
		@RequestPart(value = "attachments", required = false) List<MultipartFile> attachments) {
		List<BinaryContentCreateRequest> attachmentRequests = Optional.ofNullable(attachments)
			.map(files -> files.stream()
				.map(file -> {
					try {
						return new BinaryContentCreateRequest(
							file.getOriginalFilename(),
							file.getContentType(),
							file.getBytes()
						);
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				})
				.toList())
			.orElse(new ArrayList<>());
		log.info("attachments 생성 완료!");
		Message createdMessage = messageService.create(messageCreateRequest, attachmentRequests);
		log.info("message 생성 완료!");
		return ResponseEntity.status(HttpStatus.CREATED).body(createdMessage);
	}

	//메시지 수정
	@Operation(summary = "메시지 수정", description = "메시지 수정 API")
	@PutMapping(value = "/{messageId}")
	public ResponseEntity<Message> update(@PathVariable("messageId") UUID messageId,
		@RequestBody MessageUpdateRequest request) {
		Message updatedMessage = messageService.update(messageId, request);
		return ResponseEntity.status(HttpStatus.OK).body(updatedMessage);
	}

	//특정 채널의 메시지 목록
	//Todo 만약 private이라면 누가 누구에게 보냈는지도 dto에 담아서 보내줘야될까...?
	@Operation(summary = "특정 채널의 메시지 목록 조회", description = "특정 채널의 모든 메시지 목록 조회 API")
	@GetMapping(value = "")
	public ResponseEntity<List<Message>> findAllByChannelId(@RequestParam("channelId") UUID channelId) {
		List<Message> messages = messageService.findAllByChannelId(channelId);
		return ResponseEntity.status(HttpStatus.OK).body(messages);
	}

	//메시지 삭제
	@Operation(summary = "메시지 삭제", description = "메시지 삭제 API")
	@DeleteMapping(value = "/{messageId}")
	public ResponseEntity<Void> delete(@PathVariable("messageId") UUID messageId) {
		messageService.delete(messageId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}

}
