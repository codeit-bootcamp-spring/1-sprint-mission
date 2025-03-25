package com.sprint.mission.discodeit.service.Impl;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.ChannelJoinDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DomainErrorCode;
import com.sprint.mission.discodeit.exception.RestApiException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  private final ChannelMapper channelMapper;

  @Transactional
  @Override
  public ChannelDto create(ChannelDto channelDTO) {
    User creator = userRepository.findById(channelDTO.getUserId())
        .orElseThrow(() -> {
          log.error("유저를 찾을 수 없습니다: {}", channelDTO.getUserId());
          return new RestApiException(DomainErrorCode.USER_NOT_FOUND, "User not found");
        });

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

    Channel channel = new Channel(
        channelDTO.getName(),
        channelDTO.getDescription(),
        channelType);

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