package com.sprint.mission.discodeit.service.basic;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.sprint.mission.discodeit.dto.channel.request.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.dto.channel.request.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.dto.channel.request.UpdateChannelRequest;
import com.sprint.mission.discodeit.dto.channel.response.ChannelListResponse;
import com.sprint.mission.discodeit.dto.channel.response.ChannelResponse;
import com.sprint.mission.discodeit.dto.readStatus.request.CreateReadStatusRequest;
import com.sprint.mission.discodeit.dto.user.response.UserResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
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
	private final ReadStatusRepository readStatusRepository;

	/**
	 * 1:1 개인 채널을 생성합니다.
	 * @param request 채널 생성 요청 정보 (사용자1 ID, 사용자2 ID)
	 * @return 생성된 채널
	 */
	@Override
	public Channel createPrivateChannel(CreatePrivateChannelRequest request) {
		// 참여자 확인 User객체가 아닌 dto로 받게 되는데
		// user객체에는 온라인 상태를 나타내주는 변수가 없기 때문에 해당 변수를 포함한 userresponse를 받는게 더 낫겠다는 생각을 하였다.
		UserResponse user1 = userService.findUser(request.userId1());
		UserResponse user2 = userService.findUser(request.userId2());

		// 참여자 맵 생성
		Map<UUID, UserResponse> participants = new HashMap<>();
		participants.put(request.userId1(), user1);
		participants.put(request.userId2(), user2);

		// 채널 생성
		Channel channel = new Channel(null, null, participants, new ArrayList<>(), ChannelType.PRIVATE);
		Channel savedChannel = channelRepository.save(channel);

		// ReadStatus 생성 - 각 참여자의 읽음 상태 초기화
		CreateReadStatusRequest readStatus1 = new CreateReadStatusRequest(request.userId1(), savedChannel.getId(),
			Instant.now());
		CreateReadStatusRequest readStatus2 = new CreateReadStatusRequest(request.userId2(), savedChannel.getId(),
			Instant.now());

		readStatusService.create(readStatus1);
		readStatusService.create(readStatus2);

		return savedChannel;
	}

	/**
	 * 그룹 채널을 생성합니다.
	 * @param request 채널 생성 요청 정보 (채널명, 설명, 참여자 ID 목록)
	 * @return 생성된 채널
	 */
	@Override
	public Channel createPublicChannel(CreatePublicChannelRequest request) {
		Map<UUID, UserResponse> participants = new HashMap<>();

		// 모든 참여자 정보 조회 및 저장
		for (UUID userId : request.participantIds()) {
			UserResponse user = userService.findUser(userId);
			participants.put(userId, user);
		}

		Channel channel = new Channel(request.name(), request.description(), participants, new ArrayList<>(),
			ChannelType.PUBLIC);

		return channelRepository.save(channel);
	}

	/**
	 * 채널 정보를 조회합니다.
	 * @param channelId 채널 ID
	 * @return 채널 상세 정보
	 */
	@Override
	public ChannelResponse find(UUID channelId) {
		Channel channel = channelRepository.findById(channelId)
			.orElseThrow(() -> new IllegalArgumentException("Channel not found"));

		// 마지막 메시지 시간 조회
		Optional<Message> latestMessage = messageRepository.findLatestMessageByChannelId(channel.getId());
		Instant lastMessageAt;

		if (latestMessage.isPresent()) {
			lastMessageAt = latestMessage.get().getCreatedAt();
		} else {
			lastMessageAt = channel.getCreatedAt();
		}

		return new ChannelResponse(channel.getId(), channel.getName(), channel.getDescription(),
			channel.getParticipants(), lastMessageAt, channel.getChannelType(), channel.getCreatedAt(),
			channel.getUpdatedAt());
	}

	/**
	 * 사용자가 참여한 모든 채널을 조회합니다.
	 * @param userId 사용자 ID
	 * @return 채널 목록
	 */
	@Override
	public List<ChannelListResponse> findAllByUserId(UUID userId) {
		// 사용자 존재 확인
		userService.findUser(userId);

		// PUBLIC 채널 전체와 사용자의 PRIVATE 채널 조회
		List<Channel> publicChannels = channelRepository.findAllPublicChannels();
		List<Channel> privateChannels = channelRepository.findPrivateChannelsByUserId(userId);

		List<Channel> allChannels = new ArrayList<>();
		allChannels.addAll(publicChannels);
		allChannels.addAll(privateChannels);

		List<ChannelListResponse> responses = new ArrayList<>();

		for (Channel channel : allChannels) {
			Optional<Message> latestMessage = messageRepository.findLatestMessageByChannelId(channel.getId());
			Instant lastMessageAt;

			if (latestMessage.isPresent()) {
				lastMessageAt = latestMessage.get().getCreatedAt();
			} else {
				lastMessageAt = channel.getCreatedAt();
			}

			ChannelListResponse response = new ChannelListResponse(channel.getId(), channel.getName(),
				channel.getDescription(), channel.getParticipants(), lastMessageAt, channel.getChannelType(),
				channel.getCreatedAt(), channel.getUpdatedAt());
			responses.add(response);
		}

		return responses;
	}

	/**
	 * 채널 정보를 업데이트합니다.
	 * @param channelId 채널 ID
	 * @param request 업데이트 요청 정보
	 * @return 업데이트된 채널
	 */
	@Override
	public Channel updateChannel(UUID channelId, UpdateChannelRequest request) {
		Channel channel = channelRepository.findById(channelId)
			.orElseThrow(() -> new IllegalArgumentException("Channel not found"));

		if (channel.getChannelType() == ChannelType.PRIVATE) {
			throw new IllegalArgumentException("Cannot update PRIVATE channel");
		}

		// 참여자 정보 업데이트
		Map<UUID, UserResponse> updatedParticipants = new HashMap<>();
		for (UUID userId : request.participantIds()) {
			UserResponse user = userService.findUser(userId);
			updatedParticipants.put(userId, user);
		}

		channel.updateName(request.name());
		channel.updateDescription(request.description());
		channel.updateParticipants(updatedParticipants);
		channel.updateTime();

		return channelRepository.save(channel);
	}

	/**
	 * 채널에 새로운 참여자를 추가합니다.
	 * @param channelId 채널 ID
	 * @param userId 새로운 참여자 ID
	 */
	@Override
	public void addParticipantToChannel(UUID channelId, UUID userId) {
		Channel channel = channelRepository.findById(channelId)
			.orElseThrow(() -> new IllegalArgumentException("Channel not found"));

		UserResponse user = userService.findUser(userId);
		channel.addParticipant(userId, user);

		channelRepository.save(channel);

		// 새로운 참여자의 ReadStatus 생성
		CreateReadStatusRequest readStatusRequest = new CreateReadStatusRequest(userId, channelId, Instant.now());
		readStatusService.create(readStatusRequest);
	}

	/**
	 * 채널을 삭제합니다.
	 * @param channelId 채널 ID
	 */
	@Override
	public void deleteChannel(UUID channelId) {
		Channel channel = channelRepository.findById(channelId)
			.orElseThrow(() -> new IllegalArgumentException("Channel not found"));

		// 관련 도메인 삭제 (메시지, 읽음 상태)
		messageRepository.deleteAllByChannelId(channelId);
		readStatusRepository.deleteAllByChannelId(channelId);

		channelRepository.delete(channelId);
	}
}
