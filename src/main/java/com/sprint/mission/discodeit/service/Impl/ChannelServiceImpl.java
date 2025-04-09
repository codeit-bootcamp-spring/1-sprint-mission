package com.sprint.mission.discodeit.service.Impl;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.ChannelJoinDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DomainErrorCode;
import com.sprint.mission.discodeit.exception.RestApiException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Primary
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChannelServiceImpl implements ChannelService {

  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final ReadStatusRepository readStatusRepository;
  private final ChannelMapper channelMapper;

  @Transactional
  @Override
  public ChannelDto createPublicChannel(PublicChannelCreateRequest request) {
    Channel channel = Channel.builder()
        .name(request.getName())
        .description(request.getDescription())
        .type(ChannelType.PUBLIC)
        .build();

    Channel saved = channelRepository.save(channel);
    return channelMapper.toDto(saved);
  }

  @Transactional
  @Override
  public ChannelDto createPrivateChannel(PrivateChannelCreateRequest request) {
    // 참여자 목록이 비어있는지 확인
    if (request.getParticipantIds() == null || request.getParticipantIds().isEmpty()) {
      throw new RestApiException(DomainErrorCode.INVALID_INPUT, "참여자 목록은 필수입니다");
    }

    // 모든 참여자가 존재하는지 확인
    List<User> participants = new ArrayList<>();
    for (UUID userId : request.getParticipantIds()) {
      User user = userRepository.findById(userId)
          .orElseThrow(() -> {
            log.error("사용자를 찾을 수 없습니다: {}", userId);
            return new RestApiException(DomainErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다");
          });
      participants.add(user);
    }

    // 비공개 채널 생성
    String channelName = participants.stream()
        .map(User::getName)
        .collect(Collectors.joining(", "));

    Channel channel = Channel.builder()
        .name(channelName)
        .description("비공개 채널")
        .type(ChannelType.PRIVATE)
        .build();

    Channel saved = channelRepository.save(channel);

    // 모든 참여자들에 대한 ReadStatus 생성
    Timestamp now = Timestamp.from(Instant.now());
    for (User participant : participants) {
      ReadStatus readStatus = ReadStatus.builder()
          .user(participant)
          .channel(saved)
          .lastReadAt(now)
          .build();
      readStatusRepository.save(readStatus);
    }

    return channelMapper.toDto(saved);
  }

  @Transactional
  @Override
  public ChannelDto updateChannel(UUID channelId, PublicChannelUpdateRequest request) {
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() ->
            new RestApiException(DomainErrorCode.CHANNEL_NOT_FOUND, "채널을 찾을 수 없습니다"));

    // 비공개 채널은 수정할 수 없음
    if (channel.getType() == ChannelType.PRIVATE) {
      throw new RestApiException(DomainErrorCode.CHANNEL_INVALID_OPERATION, "비공개 채널은 수정할 수 없습니다");
    }

    // 변경할 필드가 있는 경우에만 업데이트
    if (request.getNewName() != null && !request.getNewName().isEmpty()) {
      channel.setName(request.getNewName());
    }

    if (request.getNewDescription() != null) {
      channel.setDescription(request.getNewDescription());
    }

    Channel saved = channelRepository.save(channel);
    return channelMapper.toDto(saved);
  }

  @Override
  public List<ChannelDto> findAllByUserId(UUID userId) {
    // 사용자가 존재하는지 확인
    if (!userRepository.existsById(userId)) {
      throw new RestApiException(DomainErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다");
    }

    // 사용자가 참여한 채널 목록 조회 (ReadStatus를 통해)
    List<Channel> channels = readStatusRepository.findAllByUserId(userId).stream()
        .map(ReadStatus::getChannel)
        .distinct()
        .collect(Collectors.toList());

    return channels.stream()
        .map(channelMapper::toDto)
        .collect(Collectors.toList());
  }

  @Transactional
  @Override
  public ChannelDto create(ChannelDto channelDTO) {

    // 채널 타입 처리 - 타입이 없거나 유효하지 않은 경우 기본값으로 PUBLIC 설정
    ChannelType channelType;
    try {
      if (channelDTO.getType() == null || channelDTO.getType().isEmpty()) {
        channelType = ChannelType.PUBLIC;
      } else {
        channelType = ChannelType.valueOf(channelDTO.getType().toUpperCase());
      }
    } catch (IllegalArgumentException e) {
      // 유효하지 않은 타입인 경우 기본값으로 PUBLIC 설정
      channelType = ChannelType.PUBLIC;
    }

    Channel channel = Channel.builder()
        .name(channelDTO.getName())
        .description(channelDTO.getDescription())
        .type(channelType)
        .build();

    Channel saved = channelRepository.save(channel);
    return channelMapper.toDto(saved);
  }

  @Override
  public ChannelDto find(UUID id) {
    Channel channel = channelRepository.findById(id)
        .orElseThrow(
            () -> new RestApiException(DomainErrorCode.CHANNEL_NOT_FOUND, "Channel not found"));

    return channelMapper.toDto(channel);
  }

  @Override
  public ChannelDto findById(UUID id) {
    Channel channel = channelRepository.findById(id)
        .orElseThrow(
            () -> new RestApiException(DomainErrorCode.CHANNEL_NOT_FOUND, "Channel not found"));

    return channelMapper.toDto(channel);
  }

  @Transactional
  @Override
  public Map<User, Channel> join(ChannelJoinDto joinDTO) {

    Channel channel = channelRepository.findById(joinDTO.getChannelId())
        .orElseThrow(
            () -> new RestApiException(DomainErrorCode.CHANNEL_NOT_FOUND, "Channel not found"));

    User user = userRepository.findById(joinDTO.getUserId())
        .orElseThrow(() -> new RestApiException(DomainErrorCode.USER_NOT_FOUND, "User not found"));

    channelRepository.save(channel);

    return Map.of(user, channel);
  }

  @Transactional
  @Override
  public ChannelDto update(UUID id, ChannelDto channelDTO) {
    Channel channel = channelRepository.findById(id)
        .orElseThrow(
            () -> new RestApiException(DomainErrorCode.CHANNEL_NOT_FOUND, "Channel not found"));

    // 채널 타입 처리 - 타입이 제공되지 않은 경우 기존 타입 유지
    ChannelType channelType = channel.getType(); // 기본값으로 기존 타입 사용

    if (channelDTO.getType() != null && !channelDTO.getType().isEmpty()) {
      try {
        channelType = ChannelType.valueOf(channelDTO.getType().toUpperCase());
      } catch (IllegalArgumentException e) {
        log.error("유호하지 않은 채널 타입 : {}", channelDTO.getType());
        throw new RestApiException(DomainErrorCode.INVALID_CHANNEL_TYPE, "Invalid channel type");
      }
    }

    channel.update(
        channelDTO.getName(),
        channelDTO.getDescription(),
        channelType
    );

    Channel saved = channelRepository.save(channel);
    return channelMapper.toDto(saved);
  }

  @Transactional
  @Override
  public void delete(UUID id) {
    channelRepository.deleteById(id);
  }


  @Override
  public List<ChannelDto> findAll() {
    return channelRepository.findAll()
        .stream()
        .map(channelMapper::toDto)
        .collect(Collectors.toList());
  }
}