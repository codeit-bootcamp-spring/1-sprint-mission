package com.sprint.mission.discodeit.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sprint.mission.discodeit.controller.api.ChannelApi;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.service.ChannelService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/channels")
public class ChannelController implements ChannelApi {

	private final ChannelService channelService;

	@PostMapping(path = "public")
	public ResponseEntity<ChannelDto> create(@Valid @RequestBody PublicChannelCreateRequest request) {
		log.info("Creating public channel with request: {}", request);
		ChannelDto createdChannel = channelService.create(request);
		log.info("Created public channel: {}", createdChannel);
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(createdChannel);
	}

	@PostMapping(path = "private")
	public ResponseEntity<ChannelDto> create(@Valid @RequestBody PrivateChannelCreateRequest request) {
		log.info("Creating private channel with request: {}", request);
		ChannelDto createdChannel = channelService.create(request);
		log.info("Created private channel: {}", createdChannel);
		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(createdChannel);
	}

	@PatchMapping(path = "{channelId}")
	public ResponseEntity<ChannelDto> update(@PathVariable("channelId") UUID channelId,
		@Valid @RequestBody PublicChannelUpdateRequest request) {
		log.info("Updating channel with id: {}, request: {}", channelId, request);
		ChannelDto updatedChannel = channelService.update(channelId, request);
		log.info("Updated channel: {}", updatedChannel);
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(updatedChannel);
	}

	@DeleteMapping(path = "{channelId}")
	public ResponseEntity<Void> delete(@PathVariable("channelId") UUID channelId) {
		log.info("Deleting channel with id: {}", channelId);
		channelService.delete(channelId);
		log.info("Deleted channel with id: {}", channelId);
		return ResponseEntity
			.status(HttpStatus.NO_CONTENT)
			.build();
	}

	@GetMapping
	public ResponseEntity<List<ChannelDto>> findAll(@RequestParam("userId") UUID userId) {
		log.info("Finding all channels for user id: {}", userId);
		List<ChannelDto> channels = channelService.findAllByUserId(userId);
		log.info("Found channels: {}", channels);
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(channels);
	}
}
