package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.ReadStatus.CreateReadStatusDto;
import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.InvalidOperationException;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.sprint.mission.discodeit.constant.ChannelConstant.PRIVATE_CHANNEL_CANNOT_BE_UPDATED;

@Slf4j
@Service
@ConditionalOnProperty(name = "app.service.type", havingValue = "basic")
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private static volatile BasicChannelService instance;

  private final ReadStatusService readStatusService;

  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final MessageRepository messageRepository;

  public static BasicChannelService getInstance(ChannelRepository channelRepository, UserRepository userRepository, ReadStatusService readStatusService, MessageRepository messageRepository) {
    if (instance == null) {
      synchronized (BasicChannelService.class) {
        if (instance == null) {
          instance = new BasicChannelService(readStatusService, channelRepository, userRepository, messageRepository);
        }
      }
    }
    return instance;
  }

  @Override
  public PrivateChannelResponseDto createPrivateChannel(CreatePrivateChannelDto channelDto) {

    // User 존재 여부 검증
    channelDto.userIds().stream().filter(id -> userRepository.findById(id).isEmpty()).findAny().ifPresent(
        id -> {
          throw new UserNotFoundException();
        }
    );

    // Channel 객체 생성
    Channel channel = new Channel.ChannelBuilder(null, channelDto.channelType())
        .serverUUID(channelDto.serverId())
        .maxNumberOfPeople(channelDto.maxNumberOfPeople())
        .isPrivate(true)
        .build();

    // channel 저장
    channelRepository.save(channel);

    // user 별 read status 생성
    // TODO : repository 에 saveAll()
    channelDto.userIds().stream().forEach(id -> {
      readStatusService.create(
          new CreateReadStatusDto(channel.getUUID(), id), true
      );
    });

    return new PrivateChannelResponseDto(channel.getUUID(), channel.getServerUUID(), channel.getChannelType(), channel.getCreatedAt());
  }

  @Override
  public PublicChannelResponseDto createPublicChannel(CreateChannelDto channelDto) {

    //TODO: exception tuning, other validations
    Channel channel = new Channel.ChannelBuilder(channelDto.channelName(), channelDto.channelType())
        .serverUUID(channelDto.serverId())
        .isPrivate(false)
        .maxNumberOfPeople(channelDto.maxNumberOfPeople())
        .build();

    channelRepository.save(channel);

    return new PublicChannelResponseDto(channel.getUUID(), channel.getServerUUID(), channel.getChannelType(), channel.getChannelName(), channel.getCreatedAt(), false);
  }


  /**
   * ChannelId 로 Channel을 찾음
   * {@link ChannelNotFoundException} channelId의 channel 이 없는경우 예외
   * channel이 private 이라면 해당 channel 에 속한 userId 리스트
   * private 이 아니라면 해당 리스트는 빈 리스트
   * MessageRepository 에서 가장 최근 메시지 반환
   *
   * @param channelId 찾으려는 channel 의 id
   * @return {@link FindChannelResponseDto}
   */
  @Override
  public FindChannelResponseDto getChannelById(String channelId) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(ChannelNotFoundException::new);
    List<String> userIds = getParticipatingUsersByChannel(channelId);

    Instant lastMessageTime = findLatestMessageTimeByChannel(channelId);
    return FindChannelResponseDto.from(channel, lastMessageTime, userIds);
  }


  /**
   * 모든 Channel 을 조회,
   * accessibleChannelIds -> private 채널 중,
   *
   * @param userId
   * @return
   */
  @Override
  public List<FindChannelResponseDto> findAllChannelsByUserId(String userId) {
    // 모든 Channel 조회
    List<Channel> allChannels = channelRepository.findAll();

    // userId 를 가지는 모든 read status
    Set<String> accessibleChannelIds = readStatusService.findAllByUserId(userId)
        .stream()
        .map(ReadStatus::getChannelId)
        .collect(Collectors.toSet());

    // public 이거나 set 에 포함되어 있거나 (set 에 포함되었다 = 해당 private channel 에 user가 참여 중이다)
    List<Channel> channelsVisibleToUser = allChannels.stream()
        .filter(channel -> !channel.getIsPrivate() ||
            accessibleChannelIds.contains(channel.getUUID())).toList();


    // TODO : batch 로 한번에 마지막 메시지 시간, 참여 유저 불러오기
    return channelsVisibleToUser.stream()
        .map(channel -> {
          return FindChannelResponseDto.from(
              channel,
              findLatestMessageTimeByChannel(channel.getUUID()),  // 채널별 마지막 메시지 시간 : 채널 1개당 쿼리 한번 실행
              getParticipatingUsersByChannel(channel.getUUID()) // 채널별 참여 userID : 채널 1개당 쿼리 한번 실행
          );
        }).toList();
  }

  private Instant findLatestMessageTimeByChannel(String channelId) {
    return Optional.ofNullable(messageRepository.findLatestChannelMessage(channelId))
        .map(Message::getCreatedAt)
        .orElse(Instant.EPOCH);
  }

  private List<String> getParticipatingUsersByChannel(String channelId) {
    List<String> userIds = readStatusService.findAllByChannelId(channelId).stream().map(ReadStatus::getUserId).toList();
    return userIds.isEmpty() ? Collections.emptyList() : userIds;
  }

  @Override
  public List<Channel> getChannelsByCategory(String categoryId) {
    return null;
  }

  /**
   * {@link ChannelUpdateDto}
   * channelId 로 channel 검색 후 없다면 {@link ChannelNotFoundException}
   * channel 이 private 이라면 {@link InvalidOperationException}
   *
   * @param updatedChannel
   */
  @Override
  public void updateChannel(ChannelUpdateDto updatedChannel) {

    Channel channel = channelRepository.findById(updatedChannel.getChannelId()).orElseThrow(
        ChannelNotFoundException::new
    );

    if (channel.getIsPrivate()) {
      throw new InvalidOperationException(PRIVATE_CHANNEL_CANNOT_BE_UPDATED);
    }

    synchronized (channel) {
      if (updatedChannel.getChannelName() != null && !updatedChannel.getChannelName().isEmpty()) {
        channel.setChannelName(updatedChannel.getChannelName());
      }
      if (updatedChannel.getMaxNumberOfPeople() != null) {
        channel.setMaxNumberOfPeople(updatedChannel.getMaxNumberOfPeople());
      }
      channel.updateUpdatedAt();
    }
    channelRepository.update(channel);
  }

  @Override
  public void deleteChannel(String channelId) {
    Channel channel = channelRepository.findById(channelId).orElseThrow(
        ChannelNotFoundException::new
    );

    messageRepository.deleteByChannel(channelId);
    readStatusService.deleteByChannel(channelId);
    channelRepository.delete(channelId);
  }

  @Override
  public String generateInviteCode(Channel channel) {
    return null;
  }

}
