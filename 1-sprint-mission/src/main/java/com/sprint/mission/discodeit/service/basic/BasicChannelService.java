package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;
  private final MessageRepository messageRepository;
  private final ChannelMapper channelMapper;
  private final UserRepository userRepository;

  @Transactional
  @Override
  public ChannelDto create(PublicChannelCreateRequest request) {
    Channel channel = new Channel(
        ChannelType.PUBLIC,
        request.name(),
        request.description()
    );
    channel = channelRepository.save(channel);

    return channelMapper.toDto(channel, List.of(), null);

  }

  @Transactional
  @Override
  public ChannelDto create(PrivateChannelCreateRequest request) {
    Channel channel = new Channel(ChannelType.PRIVATE, null, null);
    Channel newChannel = channelRepository.save(channel);

    List<UUID> participantIds = request.participantIds().stream()
        .map(userId -> {
          User user = userRepository.findById(userId)
              .orElseThrow(() -> new NoSuchElementException("User not found for id=" + userId));

          ReadStatus rs = new ReadStatus(user, newChannel, newChannel.getCreatedAt());
          readStatusRepository.save(rs);
          return user.getId();
        })
        .toList();
    return channelMapper.toDto(channel, participantIds, null);
  }

  @Override
  public ChannelDto find(UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException("Channel not found for id=" + channelId));

    List<UUID> participantIds = calcParticipantIds(channel);
    Instant lastMessageAt = calcLastMessageAt(channel.getId());

    return channelMapper.toDto(channel, participantIds, lastMessageAt);
  }

  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User not found for id=" + userId));

    List<Channel> myPrivateChannels = readStatusRepository.findAllByUserId(userId).stream()
        .map(ReadStatus::getChannel)
        .filter(channel -> channel.getType() == ChannelType.PRIVATE)
        .distinct()
        .toList();

    return channelRepository.findAll().stream()
        .filter(channel -> channel.getType() == ChannelType.PUBLIC || myPrivateChannels.contains(
            channel))
        .map(channel -> channelMapper.toDto(
            channel,
            calcParticipantIds(channel),
            calcLastMessageAt(channel.getId())
        ))
        .toList();
  }

  @Transactional
  @Override
  public ChannelDto update(UUID channelId, PublicChannelUpdateRequest request) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(
            () -> new NoSuchElementException("Channel with id " + channelId + " not found"));

    if (channel.getType() == ChannelType.PRIVATE) {
      throw new IllegalArgumentException("Private channels cannot be updated");
    }

    channel.update(request.newName(), request.newDescription());
    channel = channelRepository.save(channel);

    return channelMapper.toDto(channel, calcParticipantIds(channel),
        calcLastMessageAt(channel.getId()));
  }

  @Transactional
  @Override
  public void delete(UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(
            () -> new NoSuchElementException("Channel with id " + channelId + " not found"));

    messageRepository.deleteAllByChannelId(channel.getId());
    readStatusRepository.deleteAllByChannelId(channel.getId());

    channelRepository.deleteById(channelId);
  }


  private List<UUID> calcParticipantIds(Channel channel) {
    if (channel.getType() == ChannelType.PRIVATE) {
      return readStatusRepository.findAllByChannelId(channel.getId()).stream()
          .map(rs -> rs.getUser().getId())
          .distinct()
          .toList();
    }
    return List.of();
  }

  private Instant calcLastMessageAt(UUID channelId) {
    // 메시지 중 가장 최신 createdAt
    return messageRepository.findAllByChannelId(channelId).stream()
        .map(Message::getCreatedAt)
        .max(Comparator.naturalOrder())
        .orElse(null);
  }

}
