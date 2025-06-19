package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.config.CacheName;
import com.sprint.mission.discodeit.dto.request.PublicChannelRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Channel.Type;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.event.PrivateChannelCreatedEvent;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChannelService {

  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;
  private final BinaryContentStorage binaryContentStorage;
  private final ChannelMapper channelMapper;
  private final ApplicationEventPublisher eventPublisher;

  @Transactional
  public ChannelDto createPrivateChannel(List<UUID> userIds) {
    log.debug("createPrivateChannel() 호출");
    Channel channel = channelRepository.save(Channel.create(Channel.Type.PRIVATE, null, null));
    log.info("Private Channel 생성. id: {}", channel.getId());

    userIds.stream()
        .map(id -> userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(Map.of("id", id))))
        .forEach(user -> readStatusRepository.save(ReadStatus.create(user, channel)));
    ChannelDto dto = channelMapper.toDto(channel);

    eventPublisher.publishEvent(PrivateChannelCreatedEvent.of(dto, userIds));
    return dto;
  }

  @Transactional
  @PreAuthorize("hasRole('CHANNEL_MANAGER')")
  @CacheEvict(cacheNames = CacheName.CHANNELS_BY_USER, allEntries = true)
  public ChannelDto createPublicChannel(PublicChannelRequest publicChannelRequest) {
    log.debug("createPublicChannel() 호출");
    Channel channel = channelRepository
        .save(Channel.create(Type.PUBLIC, publicChannelRequest.name(),
            publicChannelRequest.description()));
    log.info("Public Channel 생성. id: {}", channel.getId());
    return channelMapper.toDto(channel);
  }

  @Cacheable(cacheNames = CacheName.CHANNELS_BY_USER, key = "#userId")
  public List<ChannelDto> readAllByUserId(UUID userId) {
    log.debug("readAllByUserId() 호출");
    if (!userRepository.existsById(userId)) {
      throw new UserNotFoundException(Map.of("id", userId));
    }

    List<UUID> subscribedChannelIds = readStatusRepository.findByUserId(userId).stream()
        .map(ReadStatus::getChannel)
        .map(Channel::getId)
        .toList();

    return channelRepository.findAll().stream()
        .filter(channel -> channel.getType() == Type.PUBLIC ||
            subscribedChannelIds.contains(channel.getId()))
        .map(channelMapper::toDto)
        .toList();
  }

  @Transactional
  @PreAuthorize("hasRole('CHANNEL_MANAGER')")
  @CacheEvict(cacheNames = CacheName.CHANNELS_BY_USER, allEntries = true)
  public ChannelDto updateChannel(UUID channelId, PublicChannelUpdateRequest updateRequest) {
    log.debug("updateChannel() 호출");
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new ChannelNotFoundException(Map.of("id", channelId)));
    if (channel.getType() == Channel.Type.PRIVATE) {
      throw new PrivateChannelUpdateException(Map.of());
    }

    channel.updateName(updateRequest.newName());
    channel.updateDescription(updateRequest.newDescription());

    log.info("Channel 수정. id: {}", channel.getId());
    channelRepository.save(channel);
    return channelMapper.toDto(channel);
  }

  @Transactional
  @PreAuthorize("hasRole('CHANNEL_MANAGER')")
  @CacheEvict(cacheNames = CacheName.CHANNELS_BY_USER, allEntries = true)
  public void deleteChannel(UUID channelId) {
    log.debug("deleteChannel() 호출");
    Optional<Channel> optionalChannel = channelRepository.findById(channelId);
    if (optionalChannel.isEmpty()) {
      return;
    }
    Channel channel = optionalChannel.get();
    List<Message> messages = messageRepository.findByChannelIdWithAttachments(channelId);

    for (Message message : messages) {
      message.getAttachments()
          .forEach(attachment -> binaryContentStorage.delete(attachment.getId()));
      messageRepository.delete(message);
    }

    readStatusRepository.deleteByChannel(channel);

    log.info("Channel 삭제. id: {}", channelId);
    channelRepository.delete(channel);
  }
}