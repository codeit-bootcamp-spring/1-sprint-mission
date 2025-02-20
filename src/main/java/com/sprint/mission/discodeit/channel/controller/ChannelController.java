package com.sprint.mission.discodeit.channel.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sprint.mission.discodeit.channel.dto.request.channel.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.channel.dto.request.channel.UpdateChannelRequest;
import com.sprint.mission.discodeit.channel.dto.response.channel.ChannelResponse;
import com.sprint.mission.discodeit.channel.dto.response.channel.PrivateChannelResponse;
import com.sprint.mission.discodeit.channel.entity.Channel;
import com.sprint.mission.discodeit.channel.mapper.ChannelMapper;
import com.sprint.mission.discodeit.channel.service.ChannelService;
import com.sprint.mission.discodeit.global.dto.CommonResponse;
import com.sprint.mission.discodeit.message.dto.request.message.CreateMessageRequest;
import com.sprint.mission.discodeit.message.entity.Message;
import com.sprint.mission.discodeit.message.service.MessageService;

@RestController
@RequestMapping("api/channels")
public class ChannelController {

	private final ChannelService channelService;
	private final MessageService messageService;
	private final ChannelMapper channelMapper;

	public ChannelController(ChannelService channelService, MessageService messageService,
		ChannelMapper channelMapper) {
		this.channelService = channelService;
		this.messageService = messageService;
		this.channelMapper = channelMapper;
	}

	@RequestMapping(value = "/private", method = RequestMethod.POST)
	public ResponseEntity<CommonResponse<PrivateChannelResponse>> createPrivateChannel(@RequestBody
	CreateMessageRequest request) {
		Message messageResponse = messageService.create(request);
		Channel channelResponse = channelService.find(messageResponse.getChannelId());
		PrivateChannelResponse response = channelMapper.channelToPrivateChannelResponse(channelResponse);
		return new ResponseEntity<>(CommonResponse.success("Private Chanel Created!", response), HttpStatus.OK);
	}

	//public Channel 생성
	@RequestMapping(value = "/public", method = RequestMethod.POST)
	public ResponseEntity<CommonResponse<ChannelResponse>> createPrivateChannel(@RequestBody
	CreatePublicChannelRequest request) {
		Channel channelResponse = channelService.createPublicChannel(request);
		ChannelResponse response = channelMapper.channelToChannelResponse(channelResponse);
		return new ResponseEntity<>(
			CommonResponse.success("Private Chanel Created!", response), HttpStatus.OK);
	}

	//공개 채널 정보 수정
	@RequestMapping(value = "/{channelId}", method = RequestMethod.PATCH)
	public ResponseEntity<CommonResponse<ChannelResponse>> updatePublicChannel(
		@PathVariable("channelId") UUID channelId,
		UpdateChannelRequest request) {
		Channel updatedChannel = channelService.updateChannel(channelId, request);
		ChannelResponse response = channelMapper.channelToChannelResponse(updatedChannel);
		return new ResponseEntity<>(
			CommonResponse.success("Channel updated successfully", response),
			HttpStatus.OK);
	}

	//특정 사용자가 볼 수 있는 모든 채널 목록을 조회
	@RequestMapping(value = "/{channelId}", method = RequestMethod.GET)
	public ResponseEntity<CommonResponse<List<ChannelResponse>>> getAllChannelsByUser(
		@PathVariable("channelId") UUID userId) {
		List<Channel> channels = channelService.findAllByUserId(userId);
		List<ChannelResponse> responses = channelMapper.channelListToChannelResponseList(channels);
		return new ResponseEntity<>(CommonResponse.success("User's channels get successfully", responses),
			HttpStatus.OK);
	}

	//채널 삭제
	@RequestMapping(value = "/{channelId}", method = RequestMethod.DELETE)
	public ResponseEntity<CommonResponse<Void>> deleteChannel(@PathVariable("channelId") UUID channelId) {
		channelService.deleteChannel(channelId);
		return new ResponseEntity<>(CommonResponse.success("Channel deleted successfully"), HttpStatus.OK);
	}
}
