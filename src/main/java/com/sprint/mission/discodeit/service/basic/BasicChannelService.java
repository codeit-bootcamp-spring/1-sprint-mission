package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.CreateReadStatusRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.Interface.ChannelService;
import com.sprint.mission.discodeit.service.Interface.MessageService;
import com.sprint.mission.discodeit.service.Interface.ReadStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.sprint.mission.discodeit.entity.ChannelType.*;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {


  private final ChannelRepository channelRepository;
  private final MessageRepository messageRepository;
  private final UserRepository userRepository;
  private final ReadStatusRepository readStatusRepository;
  private final MessageService messageService;
  private final ReadStatusService readStatusService;


  @Override
  public Channel createPublicChannel(PublicChannelCreateRequestDto request) {
    Channel channel = new Channel(PUBLIC, request.getName(), request.getDescription());
    return channelRepository.save(channel);
  }

  @Override
  public Channel createPrivateChannel(PrivateChannelCreateRequestDto request) {
    if (request.getParticipantIds().stream()
        .anyMatch(userId -> !userRepository.existsById(userId))) {
      throw new IllegalArgumentException("User not found");
    }
    Channel channel = new Channel(PRIVATE, null, null);
    Channel savedChannel = channelRepository.save(channel);

    request.getParticipantIds().stream()
        .map(userId -> new ReadStatus(userId, savedChannel.getId(), Instant.MIN))
        .forEach(readStatusRepository::save);

    return savedChannel;
  }

  @Override
  public ChannelDto getChannelById(UUID id) {
    return channelRepository.getChannelById(id)
        .map(this::toDto)
        .orElseThrow(() -> new NoSuchElementException("Channel whith id " + " not found"));
  }

  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    List<UUID> mySubscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
        .map(ReadStatus::getChannelId)
        .toList();

    return channelRepository.getAllChannels().stream()
        .filter(channel ->
            channel.getType().equals(ChannelType.PUBLIC)
                || mySubscribedChannelIds.contains(channel.getId())
        )
        .map(this::toDto)
        .toList();
  }

  @Override
  public Channel updateChannel(UUID id, ChannelUpdateRequestDto request) {
    Channel channel = channelRepository.getChannelById(id)
        .orElseThrow(() -> new NoSuchElementException("Channel not found"));

    if (channel.getType().equals(PRIVATE)) {
      throw new IllegalStateException("Cannot update private channels");
    }
    channel.update(request.getNewName(), request.getNewDescription());
    return channelRepository.save(channel);
  }

  @Override
  public void deleteChannel(UUID id) {
    if (!channelRepository.existsById(id)) {
      throw new NoSuchElementException("Channel not found");
    }
    channelRepository.deleteChannel(id);
    messageService.deleteByChannelId(id);
    readStatusService.deleteByChannelId(id);

  }

  private ChannelDto toDto(Channel channel) {
    Instant lastMessageAt = messageService.findAllByChannelId(channel.getId())
        .stream()
        .sorted(Comparator.comparing(Message::getCreatedAt).reversed())
        .map(Message::getCreatedAt)
        .limit(1)
        .findFirst()
        .orElse(Instant.MIN);

    List<UUID> participantIds = new ArrayList<>();
    if (channel.getType().equals(ChannelType.PRIVATE)) {
      readStatusService.findAllByChannelId(channel.getId())
          .stream()
          .map(ReadStatus::getUserId)
          .forEach(participantIds::add);
    }

    return new ChannelDto(
        channel.getId(),
        channel.getName(),
        channel.getType(),
        channel.getDescription(),
        lastMessageAt,
        participantIds
    );
  }
}
