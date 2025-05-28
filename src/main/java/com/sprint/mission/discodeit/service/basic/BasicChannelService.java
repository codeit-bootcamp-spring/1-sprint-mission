package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelImmutableException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.jpa.ChannelRepository;
import com.sprint.mission.discodeit.repository.jpa.MessageRepository;
import com.sprint.mission.discodeit.repository.jpa.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.jpa.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final MessageRepository messageRepository;

  private final UserMapper userMapper;
  private final ChannelMapper channelMapper;
  private final SessionRegistry sessionRegistry;



  @Override
  public ChannelDto create(ChannelCreatePublicDTO dto) {
    Channel channel = new Channel(dto.getName(), dto.getDescription(), ChannelType.PUBLIC);
    channelRepository.save(channel);

    log.info("public 채널 생성 완료 id: {}", channel.getId());
    return channelMapper.toDto(channel, List.of(), Instant.MIN);
  }

  @Override
  @Transactional
  public ChannelDto create(ChannelCreatePrivateDTO dto) {
    Channel channel = new Channel(null, null, ChannelType.PRIVATE);
    channelRepository.save(channel);

    List<UserDto> participants = dto.getParticipantIds().stream()
            .map(userId -> {
              User user = userRepository.findById(userId)
                      .orElseThrow(() -> new UserNotFoundException(userId));
              readStatusRepository.save(new ReadStatus(user, channel, Instant.EPOCH));
              return userMapper.toDto(user, isUserOnline(user));
            })
            .toList();

    Instant lastMessageAt = findLastMessageAt(channel);

    log.info("private 채널 생성 완료 id: {}", channel.getId());
    return channelMapper.toDto(channel, participants, lastMessageAt);
  }

  @Override
  @Transactional(readOnly = true)
  public ChannelDto find(UUID id) {
    Channel findChannel = channelRepository.findById(id)
        .orElseThrow(() -> new ChannelNotFoundException(id));

    List<UserDto> participants = List.of();
    if (findChannel.getChannelType().equals(ChannelType.PRIVATE)) {
      participants = readStatusRepository.findAllByChannel(findChannel).stream()
              .map(ReadStatus::getUser)
              .map(user -> userMapper.toDto(user, isUserOnline(user)))
              .toList();
    }

    Instant lastMessageAt = findLastMessageAt(findChannel);
    return channelMapper.toDto(findChannel, participants, lastMessageAt);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ChannelDto> findAllByUserId(UUID userId) {
    List<Channel> channels = readStatusRepository.findAllByUser_Id(userId).stream()
        .map(ReadStatus::getChannel).toList();

    return channelRepository.findAll().stream()
        .filter(ch -> ch.getChannelType().equals(ChannelType.PUBLIC) || channels.contains(ch))
            .map(channel -> {
              List<UserDto> participants = List.of();
              if (channel.getChannelType().equals(ChannelType.PRIVATE)) {
                participants = readStatusRepository.findAllByChannel(channel).stream()
                        .map(ReadStatus::getUser)
                        .map(user -> userMapper.toDto(user, isUserOnline(user)))
                        .toList();
              }
              Instant lastMessageAt = findLastMessageAt(channel);
              return channelMapper.toDto(channel, participants, lastMessageAt);
            })
            .toList();
  }

  @Override
  @Transactional
  public ChannelDto update(UUID id, ChannelUpdateDTO dto) {
    Channel findChannel = channelRepository.findById(id)
        .orElseThrow(() -> new ChannelNotFoundException(id));

    if (findChannel.getChannelType() == ChannelType.PRIVATE) {
      throw new PrivateChannelImmutableException(id);
    }
    findChannel.setChannel(dto.getNewName(), dto.getNewDescription());

    Instant lastMessageAt = findLastMessageAt(findChannel);
    log.info("채널 수정 완료 id: {}", findChannel.getId());
    return channelMapper.toDto(findChannel, List.of(), lastMessageAt);
  }

  @Override
  public void delete(UUID id) {
    if (!channelRepository.existsById(id)) {
      throw new ChannelNotFoundException(id);
    }
    channelRepository.deleteById(id);
    log.info("채널 삭제 완료 id: {}", id);
  }

  private boolean isUserOnline(User user) {
    return !sessionRegistry.getAllSessions(user, false).isEmpty();
  }

  private Instant findLastMessageAt(Channel channel) {
    return messageRepository.findAllByChannel(channel).stream()
            .map(Message::getCreatedAt)
            .max(Comparator.naturalOrder())
            .orElse(Instant.MIN);
  }

}
