package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.Channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.Channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.User.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicChannelService implements ChannelService {

	private final ChannelRepository channelRepository;
	//
	private final ReadStatusRepository readStatusRepository;
	private final MessageRepository messageRepository;
	private final UserRepository userRepository;
	private final ChannelMapper channelMapper;

	@Transactional
	@Override
	public ChannelDto create(PublicChannelCreateRequest request) {
		log.info("공개 채널 생성 시도: channelName={}, channelDescription={} ",
			request.name(),
			request.description());
		String name = request.name();
		String description = request.description();
		Channel channel = new Channel(ChannelType.PUBLIC, name, description);

		channelRepository.save(channel);
		log.info("공개 채널 생성 성공: channelName={}, createdAt={}",
			channel.getName(),
			channel.getCreatedAt());
		return channelMapper.toDto(channel);
	}

	@Transactional
	@Override
	public ChannelDto create(PrivateChannelCreateRequest request) {
		log.info("비공개 채널 생성 시도");
		Channel channel = new Channel(ChannelType.PRIVATE, null, null);
		channelRepository.save(channel);
		log.info("채널 저장, ID: {}", channel.getId());

		List<ReadStatus> readStatuses = request.participantIds().stream()
			.map(userId -> {
				User user = userRepository.findById(userId)
					.orElseThrow(() -> {
						log.error("비공개 채널 생성 단계에서 유저를 찾지 못함: userId={}", userId);
						return new UserNotFoundException(ErrorCode.USER_NOT_FOUND);
					});
				return new ReadStatus(user, channel, channel.getCreatedAt());
			})
			.toList();

		readStatusRepository.saveAll(readStatuses);

		return channelMapper.toDto(channel);
	}

	@Transactional(readOnly = true)
	@Override
	public ChannelDto find(UUID channelId) {
		return channelRepository.findById(channelId)
			.map(channelMapper::toDto)
			.orElseThrow(
				() -> new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND));
	}

	@Transactional(readOnly = true)
	@Override
	public List<ChannelDto> findAllByUserId(UUID userId) {
		List<UUID> mySubscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
			.map(ReadStatus::getChannel)
			.map(Channel::getId)
			.toList();

		return channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, mySubscribedChannelIds)
			.stream()
			.map(channelMapper::toDto)
			.toList();
	}

	@Transactional
	@Override
	public ChannelDto update(UUID channelId, PublicChannelUpdateRequest request) {
		String newName = request.newName();
		String newDescription = request.newDescription();
		Channel channel = channelRepository.findById(channelId)
			.orElseThrow(
				() -> {
					log.error("채널 수정 단계에서 채널을 찾지 못함: channelId={}", channelId);
					return new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND);
				});
		if (channel.getType().equals(ChannelType.PRIVATE)) {
			log.warn("PRIVATE 채널 수정 불가: channelId={}", channelId);
			throw new PrivateChannelUpdateException(ErrorCode.CHANNEL_PRIVATE_UPDATE_DENIED);
		}
		channel.update(newName, newDescription);
		log.info("채널 수정 성공: channelName={}, updatedAt={}",
			channel.getName(),
			channel.getCreatedAt());
		return channelMapper.toDto(channel);
	}

	@Transactional
	@Override
	public void delete(UUID channelId) {
		log.info("채널 삭제 시도");
		if (!channelRepository.existsById(channelId)) {
			log.error("채널 삭제 단계에서 채널을 찾지 못함: channelId={}", channelId);
			throw new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND);
		}

		messageRepository.deleteAllByChannelId(channelId);
		readStatusRepository.deleteAllByChannelId(channelId);

		channelRepository.deleteById(channelId);
		log.info("채널 삭제 시도 성공");
	}
}
