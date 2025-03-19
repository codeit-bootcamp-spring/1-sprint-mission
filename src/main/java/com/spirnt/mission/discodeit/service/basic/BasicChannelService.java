package com.spirnt.mission.discodeit.service.basic;

import com.spirnt.mission.discodeit.dto.channel.ChannelDto;
import com.spirnt.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.spirnt.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.spirnt.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.spirnt.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.spirnt.mission.discodeit.dto.user.UserDto;
import com.spirnt.mission.discodeit.enity.Channel;
import com.spirnt.mission.discodeit.enity.ChannelType;
import com.spirnt.mission.discodeit.enity.Message;
import com.spirnt.mission.discodeit.mapper.ChannelMapper;
import com.spirnt.mission.discodeit.mapper.UserMapper;
import com.spirnt.mission.discodeit.repository.ChannelRepository;
import com.spirnt.mission.discodeit.repository.MessageRepository;
import com.spirnt.mission.discodeit.repository.ReadStatusRepository;
import com.spirnt.mission.discodeit.repository.UserRepository;
import com.spirnt.mission.discodeit.service.ChannelService;
import com.spirnt.mission.discodeit.service.ReadStatusService;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final ChannelMapper channelMapper;
  private final UserMapper userMapper;

  private final ChannelRepository channelRepository;
  private final MessageRepository messageRepository;
  private final UserRepository userRepository;

  private final ReadStatusService readStatusService;
  private final ReadStatusRepository readStatusRepository;

  @Override
  public ChannelDto createChannelPublic(PublicChannelCreateRequest publicChannelCreateRequest) {
    Channel channel = channelRepository.save(new Channel(publicChannelCreateRequest.name(),
        publicChannelCreateRequest.description(), ChannelType.PUBLIC));
    return channelMapper.toDto(channel,
        getParticipants(channel),
        getLastMessageAt(channel.getId()).orElse(channel.getCreatedAt()));
  }

  @Override
  public ChannelDto createChannelPrivate(PrivateChannelCreateRequest privateChannelCreateRequest) {
    Channel channel = channelRepository.save(new Channel(null, null, ChannelType.PRIVATE));
    // 채널에 참여하는 유저별 ReadStatus 생성
    List<UUID> participantIds = privateChannelCreateRequest.participantIds();
    for (UUID userId : participantIds) {
      readStatusService.create(
          new ReadStatusCreateRequest(userId, channel.getId(), channel.getCreatedAt()));
    }
    return channelMapper.toDto(channel,
        getParticipants(channel),
        getLastMessageAt(channel.getId()).orElse(channel.getCreatedAt()));
  }

  @Override
  public ChannelDto find(UUID userId, UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException("Channel ID: " + channelId + " Not Found"));
    return channelMapper.toDto(channel,
        getParticipants(channel),
        getLastMessageAt(channel.getId()).orElse(channel.getCreatedAt()));
  }

  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    // User가 존재하지 않으면 예외 발생
    if (!userRepository.existsById(userId)) {
      throw new NoSuchElementException("User ID: " + userId + " Not Found");
    }
    List<Channel> channels = channelRepository.findAll();
    return channels.stream()
        //PUBLIC이거나 User가 참여한 PRIVATE 채널이거나
        //참여 여부는 ReadStatus 존재 여부로 확인
        .filter(channel -> (channel.getType() == ChannelType.PUBLIC) ||
            (channel.getType() == ChannelType.PRIVATE
                && readStatusRepository.existsByUserIdAndChannelId(
                userId, channel.getId())))
        .sorted(Comparator.comparing(channel -> channel.getCreatedAt()))
        .map(channel -> channelMapper.toDto(channel,
            getParticipants(channel),
            getLastMessageAt(channel.getId()).orElse(channel.getCreatedAt())))
        .toList();
  }

  @Transactional
  @Override
  public ChannelDto update(UUID channelId, PublicChannelUpdateRequest publicChannelUpdateRequest) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(
            () -> new NoSuchElementException("Channel with id " + channelId + " not found"));
    // PRIVATE 채널은 업데이트할 수 없음
    if (channel.getType() == ChannelType.PRIVATE) {
      throw new IllegalArgumentException("Private channel cannot be updated");
    }
    channel.update(publicChannelUpdateRequest.newName(),
        publicChannelUpdateRequest.newDescription());
    // 새롭게 업데이트 된 채널로 dto 반환
    return channelMapper.toDto(channel,
        getParticipants(channel),
        getLastMessageAt(channel.getId()).orElse(channel.getCreatedAt()));
  }

  @Override
  public void delete(UUID channelId) {
    if (!channelRepository.existsById(channelId)) {
      throw new NoSuchElementException("Channel with id " + channelId + " not found");
    }
    // 채널에 속한 메세지 삭제
    /**
     * 채널 -> 메시지 관계가 없음, 채널에서 메시지를 관리하지 않으므로
     * cascade 활용 대신 직접 삭제
     */
    List<Message> messages = messageRepository.findAllByChannelId(channelId);
    messages.stream()
        .forEach(message -> {
          messageRepository.delete(message);
        });
    // 채널과 관련된 ReadStatus 삭제
    readStatusRepository.deleteByChannelId(channelId);
    // 채널 삭제
    channelRepository.deleteById(channelId);
  }

  // 해당 채널 메세지를 정렬하여 가장 최근 메세지 시간 찾기
  public Optional<Instant> getLastMessageAt(UUID channelId) {
    Optional<Instant> lastSeenAt = messageRepository.findAllByChannelId(channelId).stream()
        .map(Message::getCreatedAt)
        .findFirst();
    return lastSeenAt;
  }

  public List<UserDto> getParticipants(Channel channel) {
    if (channel.getType().equals(ChannelType.PRIVATE)) {
      return readStatusRepository.findAllByChannelId(channel.getId()).stream()
          .map(readStatus -> userMapper.toDto(readStatus.getUser()))
          .collect(Collectors.toList());
    }
    return new ArrayList<>();
  }

}


