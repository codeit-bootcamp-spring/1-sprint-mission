package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.CreatePublicChannelDto;
import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelDTo;
import com.sprint.mission.discodeit.dto.channel.UpdateChannelDto;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelCategory;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicChannelService implements ChannelService {

  private final ChannelRepository channelRepository;

  private final UserRepository userRepository;

  private final MessageRepository messageRepository;

  private final ReadStatusRepository readStatusRepository;

  private final ChannelMapper channelMapper;
  private final MessageMapper messageMapper;
  private final UserMapper userMapper;

  @Override
  @Transactional
  public ChannelDto create(CreatePublicChannelDto createPublicChannelDto)
      throws DiscodeitException {
    log.info("Public 채널 생성 시작: {}", createPublicChannelDto);

    if (createPublicChannelDto == null) {
      log.error("채널 정보 누락");
      throw new DiscodeitException(ErrorCode.EMPTY_DATA);
    }

    Channel channel = new Channel(createPublicChannelDto.name(), ChannelType.PUBLIC,
        ChannelCategory.TEXT, createPublicChannelDto.description());
    Channel createdChannel = channelRepository.save(channel);
    log.info("Public 채널 생성 완료: channelId = {}", createdChannel.getId());
    return channelMapper.toDto(channel);
  }

  @Override
  @Transactional //channel create 동작 중, readStatus 생성 오류시 롤백 되도록 해야되는데?
  //todo - 고민: 읽기용 메서드인 경우에도 트랜젝션이 적용되나?
  public ChannelDto create(CreatePrivateChannelDTo createPrivateChannelDTo) {

    log.info("Private 채널 생성 시작: {}", createPrivateChannelDTo);

    if (createPrivateChannelDTo == null || createPrivateChannelDTo.participantIds().isEmpty()) {
      log.error("채널 정보 누락");
      throw new DiscodeitException(ErrorCode.EMPTY_DATA);
    }

    Channel channel = new Channel(null, ChannelType.PRIVATE, ChannelCategory.TEXT, null);
    Channel savedChannel = channelRepository.save(channel);
    log.debug("Private 채널 생성 완료: channelId = {}", savedChannel.getId());

    List<String> userIds = createPrivateChannelDTo.participantIds().stream().distinct().toList();
    List<UserDto> participants = new ArrayList<>();

    for (String userId : userIds) {
      User user = userRepository.findById(UUID.fromString(userId)).orElse(null);
      if (user == null) {
        log.warn("Private 채널에 존재하지 않는 사용자 추가 시도");
        throw new UserNotFoundException(ErrorCode.USER_NOT_FOUND);
      }
      ReadStatus readStatus = new ReadStatus(channel, user, Instant.now());
      ReadStatus savedReadStatus = readStatusRepository.save(readStatus);
      log.debug("Private 채널과 사용자를 ReadStatus로 연결 : readStatusId = {}, channelId = {}, userId = {}",
          savedReadStatus.getId(), savedReadStatus.getChannel().getId(),
          savedReadStatus.getUser().getId());
      participants.add(userMapper.toDto(user));
      log.info("Private 채널에 사용자 추가:  channelId = {}, userId = {}", channel.getId(), user.getId());
    }
    log.info("Private 채널 생성 완료: channelId = {}", savedChannel.getId());
    return channelMapper.toDto(channel);
  }

  @Override
  @Transactional(readOnly = true)
  public List<ChannelDto> findAllByUserId(String userId) {

    if (userId == null) {
      throw new IllegalArgumentException("userId cannot be null");
    }

    //fetch 조인으로 한번에 가져온다.
    //todo - 고민 : user id가 유효한지 검사 안해도 되나?
    List<Channel> channels = channelRepository.findChannelsWithReadStatusByUserId(
        UUID.fromString(userId));

    return channels.stream().map(channelMapper::toDto).toList();
  }

  @Override
  @Transactional(readOnly = true)
  public List<MessageDto> findAllMessagesByChannelId(String channelId) {
    Channel channel = channelRepository.findById(UUID.fromString(channelId))
        .orElseThrow(() -> new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND));

    return messageRepository.findByChannelId(channel.getId()).stream().map(messageMapper::toDto)
        .toList();
  }

  @Override
  @Transactional(readOnly = true)
  public ChannelDto findById(String channelId, String userId) throws DiscodeitException {
    Channel channel = channelRepository.findById(UUID.fromString(channelId))
        .orElseThrow(() -> new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND));

    ReadStatus readStatus = readStatusRepository.findByChannelIdAndUserId(
        channel.getId(), UUID.fromString(userId)).orElse(null);

    if (readStatus == null) {
      throw new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND);
    }

    List<UserDto> participants = List.of();
    if (channel.getType() == ChannelType.PRIVATE) {
      participants = readStatusRepository.findByChannelId(channel.getId()).stream().map(
          r -> userMapper.toDto(r.getUser())).toList();
    }

    return channelMapper.toDto(channel);
  }


  @Override
  @Transactional
  public ChannelDto updateChannel(String channelId, UpdateChannelDto updateChannelDto)
      throws DiscodeitException {

    log.info("체널 수정 시작: channelId = {}", channelId);

    //dto가 비어있는 경우, 채널 조회를 수행하지 않도록 수정
    if (updateChannelDto == null) {
      log.error("채널 정보 누락");
      throw new DiscodeitException(ErrorCode.EMPTY_DATA);
    }

    Channel channel = channelRepository.findById(UUID.fromString(channelId))
        .orElseThrow(() -> new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND));

    if (channel.getType() == ChannelType.PRIVATE) {
      log.error("Private 채널 수정 시도: channelId = {}, channelType = {}", channel.getId(),
          channel.getType());
      throw new PrivateChannelUpdateException(ErrorCode.CHANNEL_PRIVATE_NOT_UPDATABLE);
    }

    channel.setName(updateChannelDto.channelName());
    channel.setDescription(updateChannelDto.description());

    Channel updatedChannel = channelRepository.save(channel);
    log.info("Public 채널 정보 수정 완료: channelId = {}", updatedChannel.getId());

    return channelMapper.toDto(updatedChannel);
  }

  @Override
  @Transactional
  public boolean delete(String channelId) throws DiscodeitException {
    log.info("체널 삭제 시작: channelId = {}", channelId);
    Channel channel = channelRepository.findById(UUID.fromString(channelId))
        .orElseThrow(() -> new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND));

    //레포지토리에서 한번에 삭제 할 수 있는 방법이 있을까?
    //대용량 서비스라면? -> 삭제 완료될 때까지 기다려야함
    //1. 벌크 삭제 쿼리(JPQL) 사용
    //2. 배치 처리 -> 청크 단위로 나누어 처리
    //3. 비동기 처리 -> 삭제 작업을 비동기로 처리하고 사용자에게는 즉시 응답
    //4. 소프트 삭제 -> 실제로 데이터를 삭제하지 않고 삭제 플래그만 설정

    // -> 찾아본 바로는 DB cascade로 인해 같이 지워지나, JPA 영속성 컨텍스트는 이 변경을 즉시 알지 못할 수 있음
    // 따라서 필요시 영속성 컨텍스트도 초기화 해야한다.
    // 대용량 데이터의 경우 CASCADE 삭제도 시간이 오래 걸릴 수 있으므로, 비동기 처리나 배치 처리를 고려해볼 수 있다.
    channelRepository.delete(channel);
    log.info("체널 삭제 완료: channelId = {}", channelId);
    return true;
  }

}
