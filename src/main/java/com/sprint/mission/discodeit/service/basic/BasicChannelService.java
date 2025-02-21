package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelUpdateDto;
import com.sprint.mission.discodeit.dto.ReadStatus.CreateReadStatusDto;
import com.sprint.mission.discodeit.dto.channel.*;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.InvalidOperationException;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.validator.EntityValidator;
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

  private final ReadStatusService readStatusService;

  private final EntityValidator validator;

  private final ChannelRepository channelRepository;
  private final MessageRepository messageRepository;

  /**
   * {@link CreatePrivateChannelDto} 다음 작업 후 {@link PrivateChannelResponseDto} 를 반환한다
   * 1) user 의 존재 여부 검증
   * 2) Channel 객체 생성 및 저장
   * 3) 속한 user 별 {@link ReadStatus} 생성 및 저장
   * @param channelDto 속할 userId list 를 포함하는 비공개 채널 생성 dto
   * @return 비공개 채널 반환
   */
  @Override
  public PrivateChannelResponseDto createPrivateChannel(CreatePrivateChannelDto channelDto) {

    // User 존재 여부 검증
    channelDto.userIds().forEach(id -> validator.findOrThrow(User.class, id, new UserNotFoundException()));

    // Channel 객체 생성
    Channel channel = buildPrivateChannelFromDto(channelDto);

    // channel 저장
    channelRepository.save(channel);

    // user 별 read status 생성
    readStatusService.createMultipleReadStatus(channelDto.userIds(), channel.getUUID());

    return new PrivateChannelResponseDto(channel.getUUID(), channel.getServerUUID(), channel.getChannelType(), channel.getCreatedAt(), channel.getParticipatingUsers());
  }


  @Override
  public PublicChannelResponseDto createPublicChannel(CreateChannelDto channelDto) {
    //TODO: exception tuning, other validations
    Channel channel = buildPublicChannelFromDto(channelDto);

    channelRepository.save(channel);

    return new PublicChannelResponseDto(channel.getUUID(), channel.getServerUUID(), channel.getChannelType(), channel.getChannelName(), channel.getCreatedAt(), false);
  }

  /**
   * CreatePrivateChannelDto 를 사용해 Channel 을 만들어 반환
   */
  private Channel buildPrivateChannelFromDto(CreatePrivateChannelDto dto){
    return new Channel.ChannelBuilder(null, dto.channelType())
        .serverUUID(dto.serverId())
        .maxNumberOfPeople(dto.maxNumberOfPeople())
        .isPrivate(true)
        .participatingUsers(dto.userIds())
        .build();
  }
  /**
   * CreateChannelDto 를 사용해 Channel 을 만들어 반환
   */
  private Channel buildPublicChannelFromDto(CreateChannelDto dto){
    return new Channel.ChannelBuilder(dto.channelName(), dto.channelType())
        .serverUUID(dto.serverId())
        .isPrivate(false)
        .maxNumberOfPeople(dto.maxNumberOfPeople())
        .build();
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
    Channel channel = validator.findOrThrow(Channel.class, channelId, new ChannelNotFoundException());

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
