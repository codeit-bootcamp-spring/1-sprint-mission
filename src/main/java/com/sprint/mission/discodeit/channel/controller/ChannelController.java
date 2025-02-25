package com.sprint.mission.discodeit.channel.controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
import com.sprint.mission.discodeit.message.mapper.MessageMapper;
import com.sprint.mission.discodeit.message.service.MessageService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/channels")
@Slf4j
public class ChannelController {

	private final ChannelService channelService;
	private final MessageService messageService;
	private final ChannelMapper channelMapper;
	private final MessageMapper messageMapper;

	public ChannelController(ChannelService channelService, MessageService messageService,
		ChannelMapper channelMapper, MessageMapper messageMapper) {
		this.channelService = channelService;
		this.messageService = messageService;
		this.channelMapper = channelMapper;
		this.messageMapper = messageMapper;
	}

	@PostMapping(value = "/private", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<CommonResponse<PrivateChannelResponse>> createPrivateChannel(
		@ModelAttribute CreateMessageRequest request) {
		Message messageResponse = messageService.create(request);
		log.info("메시지 생성 성공 : " + messageResponse);
		Channel channelResponse = channelService.find(messageResponse.getChannelId());
		log.info("채널 조회 성공 : " + channelResponse.getMessageList());
		//channel controller단에서 dtoresponse를 user객체가 아닌 userReseponsedto를 가져와야될것 같은데...
		//문제는 service에서 전부 객체 반환으로 바꿔놓은 상태라 지금은 find메서드를 통해 channel 객체가 아니라 UserResponse를 map으로 받는
		//PrivateChannelResponse를 User가 아닌 UserResponse로 수정해놓고 여기에서 수정을 해야될지 find메서드에서 미리 수정을 해야될지....
		PrivateChannelResponse response = channelMapper.channelToPrivateChannelResponse(channelResponse);
		return new ResponseEntity<>(CommonResponse.success("Private Chanel Created!", response), HttpStatus.OK);
	}

	//public Channel 생성
	@PostMapping(value = "/public")
	public ResponseEntity<CommonResponse<ChannelResponse>> createPrivateChannel(@RequestBody
	CreatePublicChannelRequest request) {
		//Todo 한명만 초대가 되어도 public은 생성이 되어야 한다
		//Todo 중복
		log.info("Channel 생성중....");
		Channel channelResponse = channelService.createPublicChannel(request);
		log.info("Channel 생성 완료: " + channelResponse);
		ChannelResponse response = channelMapper.channelToChannelResponse(channelResponse);
		return new ResponseEntity<>(CommonResponse.success("Private Chanel Created!", response), HttpStatus.OK);
	}

	//공개 채널 정보 수정
	@PutMapping(value = "/{channelId}")
	public ResponseEntity<CommonResponse<ChannelResponse>> updatePublicChannel(
		@PathVariable("channelId") UUID channelId,
		@RequestBody UpdateChannelRequest request) {
		log.info("channel update중...");
		Channel updatedChannel = channelService.updateChannel(channelId, request);
		log.info("channel update 완료!");
		ChannelResponse response = channelMapper.channelToChannelResponse(updatedChannel);
		return new ResponseEntity<>(
			CommonResponse.success("Channel updated successfully", response),
			HttpStatus.OK);
	}

	//특정 사용자가 볼 수 있는 모든 채널 목록을 조회
	@GetMapping(value = "/{userid}")
	public ResponseEntity<CommonResponse<List<ChannelResponse>>> getAllChannelsByUser(
		@PathVariable("userid") UUID userId) {
		List<Channel> channels = channelService.findAllByUserId(userId);
		List<ChannelResponse> responses = channelMapper.channelListToChannelResponseList(channels);
		return new ResponseEntity<>(CommonResponse.success("User's channels get successfully", responses),
			HttpStatus.OK);
	}

	//채널 삭제
	@DeleteMapping(value = "/{channelId}")
	public ResponseEntity<CommonResponse<Void>> deleteChannel(@PathVariable("channelId") UUID channelId) {
		channelService.deleteChannel(channelId);
		return new ResponseEntity<>(CommonResponse.success("Channel deleted successfully"), HttpStatus.OK);
	}
}
