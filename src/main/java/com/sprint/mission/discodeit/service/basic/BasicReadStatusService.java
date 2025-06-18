package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.dto.readStatus.CreateReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.UpdateReadStatusDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.ReadStatus;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.readStatus.ReadStatusAlreadyExistException;
import com.sprint.mission.discodeit.exception.readStatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

  private final CacheManager cacheManager;
  private final ReadStatusRepository readStatusRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final ReadStatusMapper readStatusMapper;

  @CacheEvict(value = "userReadStatuses", key = "#createReadStatusDto.userId")
  @Override
  @Transactional
  public ReadStatusDto create(CreateReadStatusDto createReadStatusDto)
      throws DiscodeitException {

    if (createReadStatusDto == null || createReadStatusDto.channelId() == null
        || createReadStatusDto.userId() == null) {
      throw new DiscodeitException(ErrorCode.EMPTY_DATA);
    }
    Channel channel = channelRepository.findById(UUID.fromString(createReadStatusDto.channelId()))
        .orElseThrow(() -> new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND));

    User user = userRepository.findById(UUID.fromString(createReadStatusDto.userId()))
        .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

    ReadStatus readStatus = readStatusRepository.findByChannelIdAndUserId(
        UUID.fromString(createReadStatusDto.channelId()),
        UUID.fromString(createReadStatusDto.userId())).orElse(null);

    if (readStatus != null) {
      throw new ReadStatusAlreadyExistException(ErrorCode.READ_STATUS_ALREADY_EXIST);
    }

    if (channel.getType() == ChannelType.PRIVATE) {
      readStatus = new ReadStatus(channel, user, createReadStatusDto.lastReadAt(), true);
    } else {
      readStatus = new ReadStatus(channel, user, createReadStatusDto.lastReadAt(), false);
    }
    readStatusRepository.save(readStatus);

    return readStatusMapper.toDto(readStatus);
  }

  @Override
  @Transactional(readOnly = true)
  public ReadStatusDto findById(String readStatusId) {
    ReadStatus readStatus = readStatusRepository.findById(UUID.fromString(readStatusId))
        .orElseThrow(() -> new ReadStatusNotFoundException(ErrorCode.READ_STATUS_NOT_FOUND));
    return readStatusMapper.toDto(readStatus);
  }

  @CacheEvict(value = "userReadStatuses", key = "#result.userId.toString()", beforeInvocation = false)
  @PostAuthorize("authentication.principal.userDto.id == returnObject.userId()")
  @Override
  @Transactional
  public ReadStatusDto update(String readStatusId, UpdateReadStatusDto updateReadStatusDto) {

    if (updateReadStatusDto == null) {
      throw new DiscodeitException(ErrorCode.EMPTY_DATA);
    }

    ReadStatus readStatus = readStatusRepository.findById(UUID.fromString(readStatusId))
        .orElseThrow(() -> new ReadStatusNotFoundException(ErrorCode.READ_STATUS_NOT_FOUND));

    if (updateReadStatusDto.newLastReadAt() != null) {
      readStatus.setLastReadAt(updateReadStatusDto.newLastReadAt());
    }
    if (updateReadStatusDto.newNotificationEnabled() != null) {
      readStatus.setNotificationEnabled(updateReadStatusDto.newNotificationEnabled());
    }

    ReadStatus savedReadStatus = readStatusRepository.save(readStatus);

    return readStatusMapper.toDto(savedReadStatus);
  }


  @Cacheable(value = "userReadStatuses", key = "#userId")
  public List<ReadStatusDto> findAllByUserId(String userId) {

    List<ReadStatus> allReadStatusByUserId = readStatusRepository.findByUserId(
        UUID.fromString(userId));

    if (allReadStatusByUserId == null) {
      throw new ReadStatusNotFoundException(ErrorCode.READ_STATUS_NOT_FOUND);
    }

    List<ReadStatusDto> readStatusDtos = new ArrayList<>();

    for (ReadStatus readStatus : allReadStatusByUserId) {
      readStatusDtos.add(readStatusMapper.toDto(readStatus));
    }
    return readStatusDtos;
  }

  @Override
  @Transactional
  public boolean delete(String readStatusId) {
    log.info("readStatus 삭제 시작: readStatusId = {}", readStatusId);
    ReadStatus readStatus = readStatusRepository.findById(UUID.fromString(readStatusId))
        .orElseThrow(() -> new ReadStatusNotFoundException(ErrorCode.READ_STATUS_NOT_FOUND));

    log.debug("해당 readStatusId를 가진 유저의 캐시 무효화");
    Objects.requireNonNull(cacheManager.getCache("userReadStatuses"))
        .evictIfPresent(readStatus.getUser().getId());

    //cacheManager.getCache()는 Cache 객체를 반환하는데, 만약 해당 캐시 이름이 존재하지 않으면 null을 반환할 수 있다.
    // 따라서 null 체크가 필요하다.

    readStatusRepository.delete(readStatus);
    log.info("readStatus 삭제 완료");
    return true;
  }
}
