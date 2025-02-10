package com.sprint.mission.discodeit.service.basic;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.sprint.mission.discodeit.dto.channel.request.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.dto.channel.request.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.dto.channel.response.ChannelResponse;
import com.sprint.mission.discodeit.dto.readStatus.request.CreateReadStatusRequest;
import com.sprint.mission.discodeit.dto.user.response.UserResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
	private final ChannelRepository channelRepository;
	private final UserService userService;
	private final ReadStatusService readStatusService;
	private final MessageRepository messageRepository;

	@Override
	public Channel createPrivateChannel(CreatePrivateChannelRequest request) {
		// 참여자 확인
		UserResponse user1 = userService.findUser(request.userId1());
		UserResponse user2 = userService.findUser(request.userId2());

		// 참여자 맵 생성
		Map<UUID, UserResponse> participants = new HashMap<>();
		participants.put(request.userId1(), user1);
		participants.put(request.userId2(), user2);

		// 채널 생성
		Channel channel = new Channel(
			null,  // PRIVATE 채널은 이름 없음
			null,  // PRIVATE 채널은 설명 없음
			participants,
			new ArrayList<>(),
			ChannelType.PRIVATE
		);

		Channel savedChannel = channelRepository.save(channel);

		// ReadStatus 생성
		CreateReadStatusRequest readStatus1 = new CreateReadStatusRequest(
			request.userId1(),
			savedChannel.getId(),
			Instant.now()
		);
		CreateReadStatusRequest readStatus2 = new CreateReadStatusRequest(
			request.userId2(),
			savedChannel.getId(),
			Instant.now()
		);

		readStatusService.create(readStatus1);
		readStatusService.create(readStatus2);

		return savedChannel;
	}

	@Override
	public Channel createPublicChannel(CreatePublicChannelRequest request) {
		Map<UUID, UserResponse> participants = new HashMap<>();

		// 참여자 정보 수집
		for (UUID userId : request.participantIds()) {
			UserResponse user = userService.findUser(userId);
			participants.put(userId, user);
		}

		Channel channel = new Channel(
			request.name(),
			request.description(),
			participants,
			new ArrayList<>(),
			ChannelType.PUBLIC
		);

		return channelRepository.save(channel);
	}

	@Override
	public ChannelResponse find(UUID channelId) {
		Channel channel = channelRepository.findById(channelId)
			.orElseThrow(() -> new IllegalArgumentException("Channel not found"));

		// 최근 메시지 시간 조회
		Instant lastMessageAt = channel.getMessageList().stream()
			.map(Message::getCreatedAt)
			.max(Instant::compareTo)
			.orElse(channel.getCreatedAt());

		return new ChannelResponse(
			channel.getId(),
			channel.getName(),
			channel.getDescription(),
			channel.getParticipants(),
			lastMessageAt,
			channel.getChannelType(),
			channel.getCreatedAt(),
			channel.getUpdatedAt()
		);
	}

	@Override
	public List<ChannelResponse> findAll() {
		return channelRepository.findAll().stream()
			.map(channel -> {
				Instant lastMessageAt = channel.getMessageList().stream()
					.map(Message::getCreatedAt)
					.max(Instant::compareTo)
					.orElse(channel.getCreatedAt());

				return new ChannelResponse(
					channel.getId(),
					channel.getName(),
					channel.getDescription(),
					channel.getParticipants(),
					lastMessageAt,
					channel.getChannelType(),
					channel.getCreatedAt(),
					channel.getUpdatedAt()
				);
			})
			.collect(Collectors.toList());
	}

	@Override
	public Channel updateChannel(UUID existChannelId, Channel updateChannel) {
		Channel existChannel = channelRepository.findById(existChannelId)
			.orElseThrow(() -> new IllegalArgumentException("Channel does not exist: " + existChannelId));

		System.out.println("업데이트 이전 채널 = " + existChannel.toString());
		existChannel.updateName(updateChannel.getName());
		existChannel.updateDescription(updateChannel.getDescription());
		existChannel.updateParticipants(updateChannel.getParticipants());
		existChannel.updateMessageList(updateChannel.getMessageList());
		existChannel.updateTime();

		Channel updatedChannel = channelRepository.save(existChannel);
		System.out.println("업데이트 이후 채널 = " + updatedChannel.toString());
		return updatedChannel;
	}

	@Override
	public void addParticipantToChannel(UUID channelId, UUID userId) {
		Channel existChannel = channelRepository.findById(channelId)
			.orElseThrow(() -> new IllegalArgumentException("Channel does not exist: " + channelId));

		UserResponse existUser = userService.findUser(userId);

		existChannel.addParticipant(userId, existUser);
		channelRepository.save(existChannel);

		System.out.println(existChannel.toString());
		System.out.println(existChannel.getName() + " 채널에 " + existUser.username() + " 유저 추가 완료\n");
	}

	@Override
	public boolean deleteChannel(UUID channelId) {
		Optional<Channel> channelOptional = channelRepository.findById(channelId);
		if (channelOptional.isEmpty()) {
			return false;
		}
		channelRepository.delete(channelId);
		return true;
	}
}
