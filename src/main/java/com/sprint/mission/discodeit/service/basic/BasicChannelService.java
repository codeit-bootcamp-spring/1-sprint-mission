package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.exception.BadRequestException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.validator.ChannelValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;

  private final ChannelValidator channelValidator;


  @Override
  public Channel create(ChannelCreatePublicDTO dto) {
    channelValidator.validateChannel(dto.getName(), dto.getDescription());
    return channelRepository.save(
        new Channel(dto.getName(), dto.getDescription(), ChannelType.PUBLIC));
  }

  @Override
  public Channel create(ChannelCreatePrivateDTO dto) {
    Channel channel = new Channel(null, null, ChannelType.PRIVATE);
    channelRepository.save(channel);

    dto.getParticipantIds().stream()
        .map(userId -> new ReadStatus(userId, channel.getId(), Instant.MIN))
        .forEach(readStatusRepository::save);

    return channel;
  }

  @Override
  public ChannelFindDTO find(UUID id) {
    Channel findChannel = channelRepository.findOne(id);
    Optional.ofNullable(findChannel)
        .orElseThrow(() -> new NotFoundException(ErrorCode.CHANNEL_NOT_FOUND));

    return toDTO(findChannel);
  }

  @Override
  public List<ChannelFindDTO> findAllByUserId(UUID userId) {
    List<UUID> channelIds = readStatusRepository.findAllByUserId(userId).stream()
        .map(ReadStatus::getChannelId).toList();

    return channelRepository.findAll().stream()
        .filter(ch -> ch.getType().equals(ChannelType.PUBLIC)
            || channelIds.contains(ch.getId()))
        .map(this::toDTO)
        .toList();
  }

  public Channel update(UUID id, ChannelUpdateDTO dto) {
    Channel findChannel = channelRepository.findOne(id);
    Optional.ofNullable(findChannel)
        .orElseThrow(() -> new NotFoundException(ErrorCode.CHANNEL_NOT_FOUND));
    if (findChannel.getType() == ChannelType.PRIVATE) {
      throw new BadRequestException(ErrorCode.PRIVATE_CHANNEL_IMMUTABLE);
    }
    findChannel.setChannel(dto.getNewName(), dto.getNewDescription());
    channelRepository.update(findChannel);
    return findChannel;
  }

  @Override
  public UUID delete(UUID id) {
    Channel findChannel = channelRepository.findOne(id);
    Optional.ofNullable(findChannel)
        .orElseThrow(() -> new NotFoundException(ErrorCode.CHANNEL_NOT_FOUND));

    readStatusRepository.deleteByChannelId(findChannel.getId());
    messageRepository.deleteByChannelId(findChannel.getId());

    return channelRepository.delete(findChannel.getId());
  }

  private ChannelFindDTO toDTO(Channel channel) {
    Instant lastMessageAt = messageRepository.findAllByChannelId(channel.getId()).stream()
        .map(Message::getCreatedAt)
        .max(Comparator.naturalOrder()).orElse(Instant.MIN);

    List<UUID> participantIds = new ArrayList<>();
    if (channel.getType() == ChannelType.PRIVATE) {
      readStatusRepository.findAllByChannelId(channel.getId())
          .stream()
          .map(ReadStatus::getUserId)
          .forEach(participantIds::add);
    }

    return new ChannelFindDTO(
        channel.getId(),
        channel.getType(),
        channel.getName(),
        channel.getDescription(),
        participantIds,
        lastMessageAt
    );
  }

}
