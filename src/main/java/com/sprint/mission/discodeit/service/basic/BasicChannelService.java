package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.response.ChannelDto;
import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.channel.ChannelType;
import com.sprint.mission.discodeit.entity.channel.create.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.entity.channel.create.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.channel.update.ChannelModifyRequest;
import com.sprint.mission.discodeit.entity.status.read.ReadStatus;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.IllegalChannelException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelCanNotModifyException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.entitymapper.ChannelMapper;
import com.sprint.mission.discodeit.mapper.entitymapper.UserMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;
  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelMapper mapper;

  /**
   * 공개채널 생성
   */
  @Override
  public ChannelDto createPublic(PublicChannelCreateRequest request) {
    if (request.name() == null || request.description() == null) {
      throw new IllegalChannelException(Instant.now(), ErrorCode.ILLEGAL_CHANNEL,
          Map.of(ErrorCode.ILLEGAL_CHANNEL.getCode(), ErrorCode.ILLEGAL_CHANNEL.getMessage())
      );
    }

    Channel channel = createChannel(request);
    channelRepository.save(channel);

    List<UUID> participants = userRepository.findUsers().stream()
        .map(BaseEntity::getId)
        .toList();

    List<ReadStatus> readStatuses = createParticipantsReadStatus(participants, channel);
    List<UserDto> userDtoList = getParticipants(readStatuses);

    log.info("Public Channel 생성: {}", channel.getId());
    return convertToChannelDto(channel, userDtoList);
  }

  /**
   * 비공개 채널 생성
   */
  @Override
  public ChannelDto createPrivate(PrivateChannelCreateRequest request) {
    if (request.participantIds() == null || request.participantIds().isEmpty()) {
      throw new IllegalChannelException(Instant.now(), ErrorCode.ILLEGAL_CHANNEL,
          Map.of(ErrorCode.ILLEGAL_CHANNEL.getCode(), ErrorCode.ILLEGAL_CHANNEL.getMessage())
      );
    }

    Channel channel = Channel.builder()
        .type(ChannelType.PRIVATE)
        .build();
    channelRepository.save(channel);

    List<ReadStatus> readStatuses = createParticipantsReadStatus(request.participantIds(), channel);
    List<UserDto> participants = getParticipants(readStatuses);

    log.info("Private Channel 생성: {}", channel.getId());
    return convertToChannelDto(channel, participants);
  }

  /**
   * 채널 삭제
   * <p>
   * 관련된 정보 다 삭제(메세지, 파일)
   */
  @Override
  public void remove(UUID channelId) {
    channelRepository.deleteById(channelId);

    log.info("채널 삭제: {}", channelId);
  }

  /**
   * 채널 정보 수정
   */
  @Override
  public ChannelDto update(UUID channelId, ChannelModifyRequest request) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new ChannelNotFoundException(Instant.now(), ErrorCode.CHANNEL_NOT_FOUND,
            Map.of(channelId.toString(), ErrorCode.CHANNEL_NOT_FOUND.getMessage())
        ));

    if (channel.getType() == ChannelType.PRIVATE) {
      log.error("Private 채널 수정 시도");
      throw new PrivateChannelCanNotModifyException(Instant.now(), ErrorCode.MODIFY_PRIVATE_CHANNEL,
          Map.of(channelId.toString(), ErrorCode.MODIFY_PRIVATE_CHANNEL.getMessage())
      );
    }

    Channel modifiedChannel = channel.modify(request.newName(), request.newDescription());
    log.info("채널 수정: {}", channel.getId());

    return mapper.toDto(modifiedChannel);
  }

  /**
   * 유저가 참여 중인 모든 채널 뽑기
   */
  @Override
  @Transactional(readOnly = true)
  public List<ChannelDto> findAllChannelsByUserId(UUID userId) {
    if (!userRepository.existsById(userId)) {
      throw new UserNotFoundException(Instant.now(), ErrorCode.USER_NOT_FOUND,
          Map.of(userId.toString(), ErrorCode.USER_NOT_FOUND.getMessage())
      );
    }
    List<ReadStatus> readStatusList = readStatusRepository.findAllChannelsInUser(userId);

    return readStatusList.stream()
        .map(readStatus -> mapper.toDto(readStatus.getChannel()))
        .toList();
  }


  /**
   * user -> userDto
   */
  private List<UserDto> getUsers(List<ReadStatus> readStatusList) {

    return readStatusList.stream()
        .map(read -> UserMapper.toDto(read.getUser()))
        .toList();
  }

  private ChannelDto convertToChannelDto(Channel channel, List<UserDto> participants) {
    return ChannelDto.builder()
        .id(channel.getId())
        .type(channel.getType())
        .name(channel.getName())
        .description(channel.getDescription())
        .participants(participants)
        .lastMessageAt(Instant.now())
        .build();
  }

  // userDTO로 변환 후 저장
  private List<UserDto> getParticipants(List<ReadStatus> readStatuses) {
    List<ReadStatus> participantsReadStatus = readStatusRepository.saveAll(readStatuses);
    return getUsers(participantsReadStatus);
  }

  // 유저를 찾고, 유저의 readStatus 생성
  private List<ReadStatus> createParticipantsReadStatus(List<UUID> participantIds,
      Channel channel) {

    List<User> users = userRepository.findByIdIn(participantIds);
    return getReadStatuses(users, channel);
  }

  // 유저 정보로 readStatus 생성
  private List<ReadStatus> getReadStatuses(List<User> users, Channel channel) {
    return users.stream()
        .map(user -> {
          log.info("read status 생성: {}", user.getId());
          return ReadStatus.create(user, channel);
        })
        .toList();
  }

  // 채널 객체 생성
  private static Channel createChannel(PublicChannelCreateRequest request) {
    return Channel.builder()
        .name(request.name())
        .description(request.description())
        .type(ChannelType.PUBLIC)
        .build();
  }
}
