package com.sprint.mission.discodeit.service.basic;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
		log.info("Creating public channel with request: {}", request);
		try {
			String name = request.name();
			String description = request.description();
			Channel channel = new Channel(ChannelType.PUBLIC, name, description);

			channelRepository.save(channel);
			ChannelDto channelDto = channelMapper.toDto(channel);
			log.info("Created public channel: {}", channelDto);
			return channelDto;
		} catch (Exception e) {
			log.error("Error creating public channel", e);
			throw e;
		}
	}

	@Transactional
	@Override
	public ChannelDto create(PrivateChannelCreateRequest request) {
		log.info("Creating private channel with request: {}", request);
		try {
			Channel channel = new Channel(ChannelType.PRIVATE, null, null);
			channelRepository.save(channel);

			List<ReadStatus> readStatuses = userRepository.findAllById(request.participantIds()).stream()
				.map(user -> new ReadStatus(user, channel, channel.getCreatedAt()))
				.toList();
			readStatusRepository.saveAll(readStatuses);

			ChannelDto channelDto = channelMapper.toDto(channel);
			log.info("Created private channel: {}", channelDto);
			return channelDto;
		} catch (Exception e) {
			log.error("Error creating private channel", e);
			throw e;
		}
	}

	@Transactional(readOnly = true)
	@Override
	public ChannelDto find(UUID channelId) {
		log.info("Finding channel with id: {}", channelId);
		return channelRepository.findById(channelId)
			.map(channelMapper::toDto)
			.orElseThrow(() -> {
				log.warn("Channel with id {} not found", channelId);
				return new NoSuchElementException("Channel with id " + channelId + " not found");
			});
	}

	@Transactional(readOnly = true)
	@Override
	public List<ChannelDto> findAllByUserId(UUID userId) {
		log.info("Finding all channels for user id: {}", userId);
		List<UUID> mySubscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
			.map(ReadStatus::getChannel)
			.map(Channel::getId)
			.toList();

		List<Channel> channels = channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC,
			mySubscribedChannelIds);
		List<ChannelDto> channelDtos = channels.stream()
			.map(channelMapper::toDto)
			.toList();

		log.info("Found channels: {}", channelDtos);
		return channelDtos;
	}

	@Transactional
	@Override
	public ChannelDto update(UUID channelId, PublicChannelUpdateRequest request) {
		log.info("Updating channel with id: {}, request: {}", channelId, request);
		try {
			String newName = request.newName();
			String newDescription = request.newDescription();
			Channel channel = channelRepository.findById(channelId)
				.orElseThrow(() -> {
					log.warn("Channel with id {} not found", channelId);
					return new NoSuchElementException("Channel with id " + channelId + " not found");
				});
			if (channel.getType().equals(ChannelType.PRIVATE)) {
				log.warn("Private channel cannot be updated");
				throw new IllegalArgumentException("Private channel cannot be updated");
			}
			channel.update(newName, newDescription);
			ChannelDto channelDto = channelMapper.toDto(channel);
			log.info("Updated channel: {}", channelDto);
			return channelDto;
		} catch (Exception e) {
			log.error("Error updating channel with id: {}", channelId, e);
			throw e;
		}
	}

	@Transactional
	@Override
	public void delete(UUID channelId) {
		log.info("Deleting channel with id: {}", channelId);
		try {
			if (!channelRepository.existsById(channelId)) {
				log.warn("Channel with id {} not found", channelId);
				throw new NoSuchElementException("Channel with id " + channelId + " not found");
			}

			messageRepository.deleteAllByChannelId(channelId);
			readStatusRepository.deleteAllByChannelId(channelId);

			channelRepository.deleteById(channelId);
			log.info("Deleted channel with id: {}", channelId);
		} catch (Exception e) {
			log.error("Error deleting channel with id: {}", channelId, e);
			throw e;
		}
	}
}
