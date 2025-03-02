package com.sprint.mission.discodeit.channel.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import com.sprint.mission.discodeit.channel.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.channel.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.channel.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.channel.dto.response.ChannelResponse;
import com.sprint.mission.discodeit.channel.entity.Channel;
import com.sprint.mission.discodeit.channel.entity.ChannelType;
import com.sprint.mission.discodeit.channel.mapper.ChannelMapper;
import com.sprint.mission.discodeit.channel.repository.ChannelRepository;
import com.sprint.mission.discodeit.message.repository.MessageRepository;
import com.sprint.mission.discodeit.readStatus.entity.ReadStatus;
import com.sprint.mission.discodeit.readStatus.repository.ReadStatusRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {
	private final ChannelRepository channelRepository;
	private final ReadStatusRepository readStatusRepository;
	private final MessageRepository messageRepository;
	private final ChannelMapper channelMapper;

	/**
	 * 1:1 개인 채널을 생성합니다.
	 * @param request 채널 생성 요청 정보 (사용자1 ID, 사용자2 ID)
	 * @return 생성된 채널
	 */
	@Override
	public Channel createPrivateChannel(PrivateChannelCreateRequest request) {
		Channel channel = new Channel(ChannelType.PRIVATE, null, null);
		Channel createdChannel = channelRepository.save(channel);

		List<UUID> participantIds = request.participantIds();
		for (UUID userId : participantIds) {
			ReadStatus readStatus = new ReadStatus(userId, createdChannel.getId(), Instant.MIN);
			readStatusRepository.save(readStatus);
		}

		return createdChannel;
		/*// 참여자 확인 User객체가 아닌 dto로 받게 되는데
		// user객체에는 온라인 상태를 나타내주는 변수가 없기 때문에 해당 변수를 포함한 userresponse를 받는게 더 낫겠다는 생각을 하였다.
		User author = userService.findUser(request.authorId());

		//보내는 사람은 온라인 받는 사람은 아직 받지 않았으니 온라인 처리를 하지 않음
		//Todo 만약 receiver가 온라인 상태라면?
		userStatusService.updateByUserId(new UserStatusUpdateRequest(author.getId(), request.createdAt()));
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
		return savedChannel;*/
	}

	/**
	 * 그룹 채널을 생성합니다.
	 * @param request 채널 생성 요청 정보 (채널명, 설명, 참여자 ID 목록)
	 * @return 생성된 채널
	 */
	@Override
	public Channel createPublicChannel(PublicChannelCreateRequest request) {
		String name = request.name();
		String description = request.description();
		Channel channel = new Channel(ChannelType.PUBLIC, name, description);

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
			.orElseThrow(() -> new NoSuchElementException("Channel with id " + channelId + " not found"));

		return channelMapper.toDto(channel);
	}

	/**
	 * 사용자가 참여한 모든 채널을 조회합니다.
	 * @param userId 사용자 ID
	 * @return 채널 목록
	 */
	@Override
	public List<ChannelResponse> findAllByUserId(UUID userId) {
		List<UUID> mySubscribedChannelIds = new ArrayList<>();
		List<ReadStatus> readStatuses = readStatusRepository.findAllByUserId(userId);

		List<ChannelResponse> channelResponses = new ArrayList<>();
		List<Channel> channels = channelRepository.findAll();

		for (Channel channel : channels) {
			if (channel.getType().equals(ChannelType.PUBLIC) || mySubscribedChannelIds.contains(channel.getId())) {
				channelResponses.add(channelMapper.toDto(channel));
			}
		}
		return channelResponses;
	}

	/**
	 * 채널 정보를 업데이트합니다.
	 * @param channelId 채널 ID
	 * @param request 업데이트 요청 정보
	 * @return 업데이트된 채널
	 */
	@Override
	public Channel update(UUID channelId, PublicChannelUpdateRequest request) {
		String newName = request.newName();
		String newDescription = request.newDescription();
		Channel channel = channelRepository.findById(channelId)
			.orElseThrow(
				() -> new NoSuchElementException("Channel with id " + channelId + " not found"));
		if (channel.getType().equals(ChannelType.PRIVATE)) {
			throw new IllegalArgumentException("Private channel cannot be updated");
		}
		channel.update(newName, newDescription);
		return channelRepository.save(channel);
	}

	/**
	 * 채널을 삭제합니다.
	 * @param channelId 채널 ID
	 */
	@Override
	public void delete(UUID channelId) {
		Channel channel = channelRepository.findById(channelId)
			.orElseThrow(
				() -> new NoSuchElementException("Channel with id " + channelId + " not found"));

		messageRepository.deleteAllByChannelId(channel.getId());
		readStatusRepository.deleteAllByChannelId(channel.getId());

		channelRepository.deleteById(channelId);
	}
}
