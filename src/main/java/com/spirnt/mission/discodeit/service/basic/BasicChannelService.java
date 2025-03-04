package com.spirnt.mission.discodeit.service.basic;

import com.spirnt.mission.discodeit.dto.channel.ChannelDto;
import com.spirnt.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.spirnt.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.spirnt.mission.discodeit.dto.channel.PublicChannelUpdateRequest;
import com.spirnt.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.spirnt.mission.discodeit.dto.userStatus.UserStatusUpdateRequest;
import com.spirnt.mission.discodeit.enity.Channel;
import com.spirnt.mission.discodeit.enity.ChannelType;
import com.spirnt.mission.discodeit.enity.Message;
import com.spirnt.mission.discodeit.repository.ChannelRepository;
import com.spirnt.mission.discodeit.repository.MessageRepository;
import com.spirnt.mission.discodeit.repository.UserRepository;
import com.spirnt.mission.discodeit.service.ChannelService;
import com.spirnt.mission.discodeit.service.ReadStatusService;
import com.spirnt.mission.discodeit.service.UserStatusService;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final MessageRepository messageRepository;
  private final UserRepository userRepository;

  private final ReadStatusService readStatusService;
  private final UserStatusService userStatusService;

  @Override
  public Channel createChannelPublic(PublicChannelCreateRequest publicChannelCreateRequest) {
    Channel channel = new Channel(publicChannelCreateRequest.getName(),
        publicChannelCreateRequest.getDescription(), ChannelType.PUBLIC);
    channelRepository.save(channel);
    return channel;
  }

  @Override
  public Channel createChannelPrivate(PrivateChannelCreateRequest privateChannelCreateRequest) {
    Channel channel = new Channel(null, null, ChannelType.PRIVATE);
    channelRepository.save(channel);
    // 채널에 참여하는 유저별 ReadStatus 생성
    for (UUID userId : privateChannelCreateRequest.getParticipantIds()) {
      readStatusService.create(new ReadStatusCreateRequest(userId, channel.getId(), Instant.now()));
    }
    return channel;
  }

  @Override
  public ChannelDto find(UUID userId, UUID channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new NoSuchElementException("Channel ID: " + channelId + " Not Found"));
    // 스펙에 명시되지 않은 경우이므로 주석 처리
//    // PRIVATE 채널인 경우 참여한 유저만 조회 가능
//    // ReadStatus 존재 여부로 확인
//    if (channel.getType().equals(ChannelType.PRIVATE) &&
//        !readStatusService.existsByUserIdChannelId(userId, channelId)) {
//      throw new IllegalArgumentException("User did not joined this private channel");
//    }
    // PRIVATE 채널인 경우 참여 유저 찾기
    List<UUID> participantIds = new ArrayList<>();
    if (channel.getType().equals(ChannelType.PRIVATE)) {
      readStatusService.findAllByChannelId(channelId).stream()
          .map(readStatus -> readStatus.getUserId())
          .forEach(participantIds::add);
    }
    // 해당 채널의 가장 최근 메세지 작성 시간 찾기
    Instant lastMessageAt = findLastMessageInChannel(channelId)
        .orElse(channel.getCreatedAt());    // 채널 내 메세지가 없는 경우 채널 생성 시간을 디폴트로 함

    return new ChannelDto(channel, participantIds, lastMessageAt);
  }

  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    // User가 존재하지 않으면 예외 발생
    if (!userRepository.existsById(userId)) {
      // UserStatus 업데이트 -> 온라인 && 현재 활동 중으로 간주
      userStatusService.updateByUserId(userId, new UserStatusUpdateRequest(Instant.now()));
    }
    Map<UUID, Channel> data = channelRepository.findAll();
    return data.values().stream()
        //PUBLIC이거나 User가 참여한 PRIVATE 채널이거나
        //참여 여부는 ReadStatus 존재 여부로 확인
        .filter(channel -> (channel.getType() == ChannelType.PUBLIC) ||
            (channel.getType() == ChannelType.PRIVATE && readStatusService.existsByUserIdChannelId(
                userId, channel.getId())))
        .sorted(Comparator.comparing(channel -> channel.getCreatedAt()))
        // ChannelResponse로 mapping
        .map(channel -> find(userId, channel.getId()))
        .collect(Collectors.toList());
  }

  @Override
  public Channel update(UUID channelId, PublicChannelUpdateRequest publicChannelUpdateRequest) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(
            () -> new NoSuchElementException("Channel with id " + channelId + " not found"));
    // PRIVATE 채널은 업데이트할 수 없음
    if (channel.getType() == ChannelType.PRIVATE) {
      throw new IllegalArgumentException("Private channel cannot be updated");
    }
    channel.update(publicChannelUpdateRequest.getNewName(),
        publicChannelUpdateRequest.getNewDescription());
    channelRepository.save(channel);
    return channel;
  }

  @Override
  public void delete(UUID channelId) {
    if (!channelRepository.existsById(channelId)) {
      throw new NoSuchElementException("Channel with id " + channelId + " not found");
    }
    // 채널에 속한 메세지 삭제
    Map<UUID, Message> data = messageRepository.findAll();
    if (data == null) {
      return;
    }
    data.values().stream()
        .forEach(message -> {
          // 해당 채널에 쓰여진 메세지 객체를 찾고
          if (message.getChannelId().equals(channelId)) {
            // 그 객체를 레포지토리에서 삭제
            messageRepository.delete(message.getId());
          }
        });
    // 채널과 관련된 ReadStatus 삭제
    readStatusService.deleteByChannelId(channelId);
    // 채널 삭제
    channelRepository.delete(channelId);
  }


  // 해당 채널 메세지를 정렬하여 가장 최근 메세지 시간 찾기
  public Optional<Instant> findLastMessageInChannel(UUID channelId) {
    Optional<Instant> lastSeenAt = messageRepository.findAllByChannelId(channelId).stream()
        .map(Message::getCreatedAt)
        .findFirst();
    return lastSeenAt;
  }
}


