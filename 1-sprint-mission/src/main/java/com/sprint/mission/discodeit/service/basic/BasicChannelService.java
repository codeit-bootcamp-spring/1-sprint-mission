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
import com.sprint.mission.discodeit.exception.channel.CannotUpdatePrivateChannelException;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
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
    log.info("create() [Public Channel] called with - name: {}. description: {}", request.name(),
        request.description());

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
    log.info("create() [Private Channel] called with - 참여자 수 : {}",
        request.participantIds().size());

    Channel channel = new Channel(ChannelType.PRIVATE, null, null);
    Channel newChannel = channelRepository.save(channel);

    List<UUID> participantIds = request.participantIds().stream()
        .map(userId -> {
          User user = userRepository.findById(userId)
              .orElseThrow(() -> {
                log.error("비공개 채널 생성 실패 - 존재하지 않는 사용자 ID: {}", userId);
                return new UserNotFoundException(userId);
              });

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
        .orElseThrow(() -> new ChannelNotFoundException(channelId));

    List<UUID> participantIds = calcParticipantIds(channel);
    Instant lastMessageAt = calcLastMessageAt(channel.getId());

    return channelMapper.toDto(channel, participantIds, lastMessageAt);
  }

  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));

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

    log.info("update() called with - channelId : {}, newName: {}, newDescription: {}", channelId,
        request.newName(), request.newDescription());

    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(
            () -> {
              log.error("채널 수정 실패 - 존재하지 않는 채널 ID: {}", channelId);
              return new ChannelNotFoundException(channelId);
            });

    if (channel.getType() == ChannelType.PRIVATE) {
      log.warn("비공개 채널 수정이 안됨 - ID : {}", channelId);
      throw new CannotUpdatePrivateChannelException(channelId);
    }

    channel.update(request.newName(), request.newDescription());
    channel = channelRepository.save(channel);

    return channelMapper.toDto(channel, calcParticipantIds(channel),
        calcLastMessageAt(channel.getId()));
  }

  @Transactional
  @Override
  public void delete(UUID channelId) {
    log.debug("delete() called - channelId: {}", channelId);
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(
            () -> {
              log.error("채널 삭제 실패 - 존재하지 않은 채널 ID: {}", channelId);
              return new ChannelNotFoundException(channelId);
            });

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
