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

import com.sprint.mission.discodeit.dto.channel.request.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.dto.channel.request.UpdateChannelRequest;
import com.sprint.mission.discodeit.dto.channel.response.ChannelListResponse;
import com.sprint.mission.discodeit.dto.channel.response.ChannelResponse;
import com.sprint.mission.discodeit.dto.message.request.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.response.MessageResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.global.dto.CommonResponse;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;

@RestController
@RequestMapping("api/channel")
public class ChannelController {

	private final ChannelService channelService;
	private final MessageService messageService;

	public ChannelController(ChannelService channelService, MessageService messageService) {
		this.channelService = channelService;
		this.messageService = messageService;
	}

	//private channel은 message가 생성될때 같이 생성이 되는데 이건 그렇다면 messagechannel로 가는게 맞는거 아닌가....
	@RequestMapping(value = "/create-privateChannel", method = RequestMethod.POST)
	public ResponseEntity<CommonResponse<ChannelResponse>> createPrivateChannel(@RequestBody
	CreateMessageRequest request) {
		MessageResponse messageResponse = messageService.create(request);
		ChannelResponse channelResponse = channelService.find(messageResponse.channelId());
		return new ResponseEntity<>(CommonResponse.success("Private Chanel Created!", channelResponse), HttpStatus.OK);
	}

	//public Channel 생성
	@RequestMapping(value = "/create-publicChannel", method = RequestMethod.POST)
	public ResponseEntity<CommonResponse<ChannelResponse>> createPrivateChannel(@RequestBody
	CreatePublicChannelRequest request) {
		Channel channelResponse = channelService.createPublicChannel(request);
		return new ResponseEntity<>(
			CommonResponse.success("Private Chanel Created!", ChannelResponse.from(channelResponse)), HttpStatus.OK);
	}

	//공개 채널 정보 수정
	@RequestMapping(value = "/update/{id}", method = RequestMethod.PATCH)
	public ResponseEntity<CommonResponse<ChannelResponse>> updatePublicChannel(@PathVariable("id") UUID channelId,
		UpdateChannelRequest request) {
		Channel updatedChannel = channelService.updateChannel(channelId, request);

		return new ResponseEntity<>(
			CommonResponse.success("Channel updated successfully", ChannelResponse.from(updatedChannel)),
			HttpStatus.OK);
	}

	//특정 사용자가 볼 수 있는 모든 채널 목록을 조회
	@RequestMapping(value = "/find-all/{id}", method = RequestMethod.GET)
	public ResponseEntity<CommonResponse<List<ChannelListResponse>>> getAllChannelsByUser(
		@PathVariable("id") UUID userId) {
		List<ChannelListResponse> channels = channelService.findAllByUserId(userId);
		return new ResponseEntity<>(CommonResponse.success("User's channels get successfully", channels),
			HttpStatus.OK);
	}

	//채널 삭제
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<CommonResponse<Void>> deleteChannel(@PathVariable("id") UUID channelId) {
		channelService.deleteChannel(channelId);
		return new ResponseEntity<>(CommonResponse.success("Channel deleted successfully"), HttpStatus.OK);
	}
}
