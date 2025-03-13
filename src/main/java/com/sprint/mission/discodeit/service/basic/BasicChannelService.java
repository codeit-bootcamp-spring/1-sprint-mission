package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  //
  private final UserRepository userRepository;
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;
  private final ChannelMapper channelMapper;
  private final UserMapper userMapper;

  @Override
  public ChannelDto create(PublicChannelCreateRequest request) {
    String name = request.name();
    String description = request.description();
    Channel channel = new Channel(ChannelType.PUBLIC, name, description);
    Channel save = channelRepository.save(channel);
    return toDto(channel);
  }

  @Override
  public ChannelDto create(PrivateChannelCreateRequest request) {
    Channel channel = new Channel(ChannelType.PRIVATE, null, null);
    Channel createdChannel = channelRepository.save(channel);
    for (UUID userId : request.participantIds()) {
      User user = userRepository.findById(userId).get();
      ReadStatus save = readStatusRepository.save(
          new ReadStatus(user, channel, channel.getCreatedAt()));
      System.out.println("save = " + save);
    }
    //request.participantIds().stream().map(user -> new ReadStatus(user, createdChannel, Instant.now())).forEach(System.out::println);
    //.forEach(readStatusRepository::save);
    List<ReadStatus> readStatuses = readStatusRepository.findAll();
    System.out.println("readStatuses = " + readStatuses);

    return toDto(createdChannel);
  }

  public Optional<Instant> getLastMessageAt(UUID channelId) {
    return messageRepository.findAllByChannelId(channelId).stream()
        .map(Message::getCreatedAt)
        .findFirst();
  }

  public List<UserDto> getParticipants(Channel channel) {
    if (channel.getType().equals(ChannelType.PRIVATE)) {
      return readStatusRepository.findReadStatusesByChannelId(channel.getId()).stream()
          .map(readStatus -> userMapper.toDto(readStatus.getUser()))
          .collect(Collectors.toList());
    }
    return new ArrayList<>();
  }

  @Override
  public ChannelDto find(UUID channelId) {
    return channelRepository.findById(channelId)
        .map(this::toDto)
        .orElseThrow(
            () -> new NoSuchElementException("Channel with id " + channelId + " not found"));
  }

  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    List<UUID> mySubscribedChannelIds = readStatusRepository.findAllByUserId(userId).stream()
        .map(s -> s.getChannel().getId())
        .toList();

    return channelRepository.findAll().stream()
        .filter(channel ->
            channel.getType().equals(ChannelType.PUBLIC)
                || mySubscribedChannelIds.contains(channel.getId())
        )
        .map(this::toDto)
        .toList();
  }

  @Override
  public List<ChannelDto> findAll() {
    List<Channel> channels = channelRepository.findAll();
    List<ChannelDto> channelDtos = new ArrayList<>();
    for (Channel channel : channels) {
      channelDtos.add(toDto(channel));
    }
    return channelDtos;
  }

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

  @Override
  public void delete(UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(
            () -> new NoSuchElementException("Channel with id " + channelId + " not found"));

    //messageRepository.deleteAllByChannelId(channel.getId()); //cascade.all?
    readStatusRepository.deleteAllByChannelId(channel.getId());

    channelRepository.deleteById(channelId);
  }

  private ChannelDto toDto(Channel channel) {
    Instant lastMessageAt = messageRepository.findAllByChannelId(channel.getId())
        .stream()
        .sorted(Comparator.comparing(Message::getCreatedAt).reversed())
        .map(Message::getCreatedAt)
        .limit(1)
        .findFirst()
        .orElse(Instant.MIN);

    List<UserDto> participants = getParticipants(channel);
    Instant instant = getLastMessageAt(channel.getId()).get();
    /*new ArrayList<>();
    if (channel.getType().equals(ChannelType.PRIVATE)) {
      readStatusRepository.findReadStatusesByChannelId(channel.getId())
          .stream()
          .map(ReadStatus::getUser)
          .forEach(UserDto::new);
    }*/
    System.out.println("instant = " + instant);
    System.out.println("participants = " + participants);
    System.out.println("channel = " + channel);

    return new ChannelDto(
        channel.getId(),
        channel.getType(),
        channel.getName(),
        channel.getDescription(),
        participants,
        instant
    );
  }
}
