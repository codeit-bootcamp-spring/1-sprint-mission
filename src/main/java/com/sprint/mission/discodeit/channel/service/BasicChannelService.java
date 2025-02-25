package com.sprint.mission.discodeit.channel.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.sprint.mission.discodeit.channel.dto.request.channel.CreatePrivateChannelRequest;
import com.sprint.mission.discodeit.channel.dto.request.channel.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.channel.dto.request.channel.UpdateChannelRequest;
import com.sprint.mission.discodeit.channel.dto.request.readStatus.CreateReadStatusRequest;
import com.sprint.mission.discodeit.channel.entity.Channel;
import com.sprint.mission.discodeit.channel.entity.ChannelType;
import com.sprint.mission.discodeit.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.message.entity.Message;
import com.sprint.mission.discodeit.message.repository.MessageRepository;
import com.sprint.mission.discodeit.user.dto.request.UpdateUserStatusRequest;
import com.sprint.mission.discodeit.user.entity.User;
import com.sprint.mission.discodeit.user.service.UserService;
import com.sprint.mission.discodeit.user.service.UserStatusService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BasicChannelService implements ChannelService {
	private final ChannelRepository channelRepository;
	private final UserService userService;
	private final ReadStatusService readStatusService;
	private final UserStatusService userStatusService;
	private final MessageRepository messageRepository;

	public BasicChannelService(ChannelRepository channelRepository, UserService userService,
		ReadStatusService readStatusService, UserStatusService userStatusService, MessageRepository messageRepository) {
		this.channelRepository = channelRepository;
		this.userService = userService;
		this.readStatusService = readStatusService;
		this.userStatusService = userStatusService;
		this.messageRepository = messageRepository;
	}

	/**
	 * 1:1 개인 채널을 생성합니다.
	 * @param request 채널 생성 요청 정보 (사용자1 ID, 사용자2 ID)
	 * @return 생성된 채널
	 */
	@Override
	public Channel createPrivateChannel(CreatePrivateChannelRequest request) {
		// 참여자 확인 User객체가 아닌 dto로 받게 되는데
		// user객체에는 온라인 상태를 나타내주는 변수가 없기 때문에 해당 변수를 포함한 userresponse를 받는게 더 낫겠다는 생각을 하였다.
		User author = userService.findUser(request.authorId());

		//보내는 사람은 온라인 받는 사람은 아직 받지 않았으니 온라인 처리를 하지 않음
		//Todo 만약 receiver가 온라인 상태라면?
		userStatusService.updateByUserId(new UpdateUserStatusRequest(author.getId(), request.createdAt()));
		User receiver = userService.findUser(request.receiverId());

		// 참여자 맵 생성
		Map<UUID, User> participants = new HashMap<>();
		participants.put(request.authorId(), author);
		participants.put(request.receiverId(), receiver);

		// 채널 생성
		Channel channel = new Channel(null, null, participants, request.createdAt(), new ArrayList<>(),
			ChannelType.PRIVATE);
		Channel savedChannel = channelRepository.save(channel);

		// ReadStatus 생성 - 각 참여자의 읽음 상태 초기화
		CreateReadStatusRequest readStatus1 = new CreateReadStatusRequest(request.authorId(), savedChannel.getId(),
			null,
			Instant.now());
		CreateReadStatusRequest readStatus2 = new CreateReadStatusRequest(request.receiverId(), savedChannel.getId(),
			null,
			Instant.now());

		readStatusService.create(readStatus1);
		readStatusService.create(readStatus2);
		return savedChannel;
	}

	/**
	 * 그룹 채널을 생성합니다.
	 * @param channelId privateChannel에서 메시지 생성 요청
	 * @param message privateChannel을 보낼때 만들어진 message
	 */
	@Override
	public void addMessageToChannel(UUID channelId, Message message) {
		Channel channel = channelRepository.findById(channelId)
			.orElseThrow(() -> new IllegalArgumentException("Channel not found"));

		// 🔥 채널의 메시지 리스트에 메시지 추가
		channel.getMessageList().add(message);
		channelRepository.save(channel); // 변경사항 저장
	}

	/**
	 * 그룹 채널을 생성합니다.
	 * @param request 채널 생성 요청 정보 (채널명, 설명, 참여자 ID 목록)
	 * @return 생성된 채널
	 */
	@Override
	public Channel createPublicChannel(CreatePublicChannelRequest request) {
		Map<UUID, User> participants = new HashMap<>();

		//유저 online처리
		userStatusService.updateByUserId(new UpdateUserStatusRequest(request.creatorId(), request.createdAt()));

		//creator도 channel에 추가
		participants.put(request.creatorId(), userService.findUser(request.creatorId()));

		// 모든 참여자 정보 조회 및 저장
		for (UUID userId : request.participantIds()) {
			User user = userService.findUser(userId);
			participants.put(userId, user);
		}

		Channel channel = new Channel(request.name(), request.description(), participants, request.createdAt(),
			new ArrayList<>(),
			ChannelType.PUBLIC);

		return channelRepository.save(channel);
	}

	/**
	 * 채널 정보를 조회합니다.
	 * @param channelId 채널 ID
	 * @return 채널 상세 정보
	 */
	@Override
	public Channel find(UUID channelId) {
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
		channel.updateLastMessageAt(lastMessageAt);

		return channel;
	}

	/**
	 * 사용자가 참여한 모든 채널을 조회합니다.
	 * @param userId 사용자 ID
	 * @return 채널 목록
	 */
	@Override
	public List<Channel> findAllByUserId(UUID userId) {
		// 사용자 존재 확인
		userService.findUser(userId);
		// 사용자 존재한다면 online으로 처리
		userStatusService.updateByUserId(new UpdateUserStatusRequest(userId, Instant.now()));

		// PUBLIC 채널 전체와 사용자의 PRIVATE 채널 조회
		List<Channel> publicChannels = channelRepository.findAllPublicChannels();
		List<Channel> privateChannels = channelRepository.findPrivateChannelsByUserId(userId);

		List<Channel> allChannels = new ArrayList<>();
		allChannels.addAll(publicChannels);
		allChannels.addAll(privateChannels);

		List<Channel> responses = new ArrayList<>();

		for (Channel channel : allChannels) {
			Optional<Message> latestMessage = messageRepository.findLatestMessageByChannelId(channel.getId());
			Instant lastMessageAt;

			if (latestMessage.isPresent()) {
				lastMessageAt = latestMessage.get().getCreatedAt();
			} else {
				lastMessageAt = channel.getCreatedAt();
			}
			channel.updateLastMessageAt(lastMessageAt);
			responses.add(channel);
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
		Map<UUID, User> updatedParticipants = new HashMap<>();
		for (UUID userId : request.participantIds()) {
			User user = userService.findUser(userId);
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
	//Todo inviter를 명시하는게 좋을까..?
	@Override
	public void addParticipantToChannel(UUID channelId, UUID userId) {
		Channel channel = channelRepository.findById(channelId)
			.orElseThrow(() -> new IllegalArgumentException("Channel not found"));

		User user = userService.findUser(userId);
		channel.addParticipant(userId, user);

		channelRepository.save(channel);

		// 채널의 최신 메시지 조회
		Message latestMessageOpt = messageRepository.findLatestMessageByChannelId(channel.getId())
			.orElse(null);
		UUID latestMessageId;
		if (latestMessageOpt != null) {
			latestMessageId = latestMessageOpt.getId();
		} else {
			latestMessageId = null;
		}

		// 새로운 참여자의 ReadStatus 생성
		CreateReadStatusRequest readStatusRequest = new CreateReadStatusRequest(userId, channelId, latestMessageId,
			Instant.now());
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
		messageRepository.deleteAllByChannelId(channel.getId());
		readStatusService.deleteAllByChannelId(channel.getId());
		channelRepository.delete(channel.getId());
	}
}
