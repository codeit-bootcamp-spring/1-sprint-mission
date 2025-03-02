package com.sprint.mission.discodeit.channel.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sprint.mission.discodeit.channel.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.channel.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.channel.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.channel.dto.response.ChannelResponse;
import com.sprint.mission.discodeit.channel.entity.Channel;
import com.sprint.mission.discodeit.channel.mapper.ChannelMapper;
import com.sprint.mission.discodeit.channel.service.ChannelService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/channels")
@Slf4j
@Tag(name = "Channel", description = "채널 관련 API")
public class ChannelController {

	private final ChannelService channelService;
	private final ChannelMapper channelMapper;

	@Operation(summary = "비공개 채널 생성", description = "특정 사용자에 메시지를 보냄으로 채널 생성 API")
	@PostMapping(value = "/private")
	public ResponseEntity<Channel> create(@RequestBody PrivateChannelCreateRequest request) {
		Channel createdChannel = channelService.createPrivateChannel(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdChannel);
	}

	//public Channel 생성
	@Operation(summary = "공개 채널 생성", description = "공개 채널 생성 API")
	@PostMapping(value = "/public")
	public ResponseEntity<Channel> create(@RequestBody PublicChannelCreateRequest request) {
		//Todo 한명이 public을 생성하더라도 생성해야될까?
		Channel createdChannel = channelService.createPublicChannel(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdChannel);
	}

	//공개 채널 정보 수정
	@Operation(summary = "공개 채널 수정", description = "공개 채널 수정 API")
	@PutMapping(value = "/{channelId}")
	public ResponseEntity<Channel> update(@PathVariable("channelId") UUID channelId,
		@RequestBody PublicChannelUpdateRequest request) {
		Channel udpatedChannel = channelService.update(channelId, request);
		return ResponseEntity.status(HttpStatus.OK).body(udpatedChannel);
	}

	//특정 사용자가 볼 수 있는 모든 채널 목록을 조회
	@Operation(summary = "사용자가 참여하는 모든 채널 조회", description = "사용자가 참여하는 모든 채널 조회 API")
	@GetMapping(value = "")
	public ResponseEntity<List<ChannelResponse>> findAll(@RequestParam UUID userId) {
		List<ChannelResponse> channels = channelService.findAllByUserId(userId);
		return ResponseEntity.status(HttpStatus.OK).body(channels);
	}

	//채널 삭제
	@Operation(summary = "채널 삭제", description = "특정 채널 삭제 API")
	@DeleteMapping(value = "")
	public ResponseEntity<Void> delete(@RequestParam("channelId") UUID channelId) {
		channelService.delete(channelId);
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
