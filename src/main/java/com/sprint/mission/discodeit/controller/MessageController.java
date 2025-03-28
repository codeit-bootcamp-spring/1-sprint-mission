package com.sprint.mission.discodeit.controller;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sprint.mission.discodeit.controller.api.MessageApi;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.service.MessageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/messages")
public class MessageController implements MessageApi {

	private final MessageService messageService;

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<MessageDto> create(
		@RequestPart("messageCreateRequest") MessageCreateRequest messageCreateRequest,
		@RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
	) {
		log.info("Creating message with request: {}, attachments: {}", messageCreateRequest,
			attachments != null ? attachments.size() : 0);
		try {
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
							log.error("Error processing attachment", e);
							throw new RuntimeException(e);
						}
					})
					.toList())
				.orElse(new ArrayList<>());
			MessageDto createdMessage = messageService.create(messageCreateRequest, attachmentRequests);
			log.info("Created message: {}", createdMessage);
			return ResponseEntity
				.status(HttpStatus.CREATED)
				.body(createdMessage);
		} catch (Exception e) {
			log.error("Error creating message", e);
			throw e;
		}
	}

	@PatchMapping(path = "{messageId}")
	public ResponseEntity<MessageDto> update(@PathVariable("messageId") UUID messageId,
		@RequestBody MessageUpdateRequest request) {
		log.info("Updating message with id: {}, request: {}", messageId, request);
		try {
			MessageDto updatedMessage = messageService.update(messageId, request);
			log.info("Updated message: {}", updatedMessage);
			return ResponseEntity
				.status(HttpStatus.OK)
				.body(updatedMessage);
		} catch (Exception e) {
			log.error("Error updating message with id: {}", messageId, e);
			throw e;
		}
	}

	@DeleteMapping(path = "{messageId}")
	public ResponseEntity<Void> delete(@PathVariable("messageId") UUID messageId) {
		log.info("Deleting message with id: {}", messageId);
		try {
			messageService.delete(messageId);
			log.info("Deleted message with id: {}", messageId);
			return ResponseEntity
				.status(HttpStatus.NO_CONTENT)
				.build();
		} catch (Exception e) {
			log.error("Error deleting message with id: {}", messageId, e);
			throw e;
		}
	}

	@GetMapping
	public ResponseEntity<PageResponse<MessageDto>> findAllByChannelId(
		@RequestParam("channelId") UUID channelId,
		@RequestParam(value = "cursor", required = false) Instant cursor,
		@PageableDefault(
			size = 50,
			page = 0,
			sort = "createdAt",
			direction = Direction.DESC
		) Pageable pageable) {
		log.info("Finding all messages for channel id: {}, cursor: {}, pageable: {}", channelId,
			cursor, pageable);
		PageResponse<MessageDto> messages = messageService.findAllByChannelId(channelId, cursor,
			pageable);
		log.info("Found messages: {}", messages);
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(messages);

	}
}
