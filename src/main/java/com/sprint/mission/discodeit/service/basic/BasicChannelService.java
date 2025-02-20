package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.validation.ChannelValidator;
import java.time.Instant;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;
  private final ChannelValidator channelValidator;
  private final ChannelMapper channelMapper;

  @Override
  public ChannelDto createPrivateChannel(PrivateChannelCreateRequest request) {
    Channel channel = new Channel(null, null, ChannelType.PRIVATE);
    Channel createdChannel = channelRepository.save(channel);
    for (UUID userId : request.participants()) {
      readStatusRepository.save(new ReadStatus(userId, createdChannel.getId(), Instant.MIN));
    }
    List<UUID> participantIds = getParticipantIds(createdChannel);
    Instant lastMessageTime = getLastMessageAt(createdChannel.getId());
    return channelMapper.channelEntityToDto(createdChannel, participantIds, lastMessageTime);
  }

  @Override
  public ChannelDto createPublicChannel(PublicChannelCreateRequest request) {
    String channelName = request.name();
    String description = request.description();

    channelValidator.validatePublicChannel(channelName, description);

    Channel channel = new Channel(channelName, description, ChannelType.PUBLIC);
    Channel createdChannel = channelRepository.save(channel);
    List<UUID> participantIds = getParticipantIds(createdChannel);
    Instant lastMessageTime = getLastMessageAt(createdChannel.getId());
    return channelMapper.channelEntityToDto(createdChannel, participantIds, lastMessageTime);
  }

  @Override
  public ChannelDto findById(UUID channelId) {
    return channelRepository.findById(channelId)
        .map(channel -> {
          List<UUID> participantIds = getParticipantIds(channel);
          Instant lastMessageTime = getLastMessageAt(channel.getId());
          return channelMapper.channelEntityToDto(channel, participantIds, lastMessageTime);
        })
        .orElseThrow(() -> new ResourceNotFoundException("Channel not found: " + channelId));
  }

  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    List<UUID> channelIds = readStatusRepository.findAllByUserId(userId).stream()
        .map(ReadStatus::getChannelId)
        .toList();
    return channelRepository.findAll().stream()
        .filter(
            channel -> channel.getChannelType().equals(ChannelType.PUBLIC) || channelIds.contains(
                channel.getId()))
        .map(channel -> {
          List<UUID> participantIds = getParticipantIds(channel);
          Instant lastMessageTime = getLastMessageAt(channel.getId());
          return channelMapper.channelEntityToDto(channel, participantIds, lastMessageTime);
        })
        .toList();
  }

  @Override
  public ChannelDto update(UUID channelId, PublicChannelUpdateRequest request) {
    String newChannelName = request.newName();
    String newDescription = request.newDescription();

    channelValidator.validatePublicChannel(newChannelName, newDescription);

    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new ResourceNotFoundException("Channel not found."));

    channel.update(newChannelName, newDescription);
    Channel updatedChannel = channelRepository.save(channel);

    List<UUID> participantIds = getParticipantIds(updatedChannel);
    Instant lastMessageTime = getLastMessageAt(updatedChannel.getId());
    return channelMapper.channelEntityToDto(updatedChannel, participantIds, lastMessageTime);
  }

  @Override
  public void delete(UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(
            () -> new NoSuchElementException("Channel with id " + channelId + " not found"));

    messageRepository.deleteAllByChannelId(channel.getId());
    readStatusRepository.deleteAllByChannelId(channel.getId());
    channelRepository.deleteById(channelId);
  }

  private Instant getLastMessageAt(UUID channelId) {
    return messageRepository.findAllByChannelId(channelId).stream()
        .max(Comparator.comparing(Message::getCreatedAt))
        .map(Message::getCreatedAt)
        .orElse(Instant.MIN);
  }

  private List<UUID> getParticipantIds(Channel channel) {
    if (!channel.getChannelType().equals(ChannelType.PRIVATE)) {
      return Collections.emptyList();
    }
    return readStatusRepository.findAllByChannelId(channel.getId()).stream()
        .map(ReadStatus::getUserId)
        .toList();
  }
}
