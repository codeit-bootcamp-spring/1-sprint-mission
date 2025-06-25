package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.readStatus.ReadStatusAlreadyExistsException;
import com.sprint.mission.discodeit.exception.readStatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  //
  private final ReadStatusRepository readStatusRepository;
  //
  private final ReadStatusMapper readStatusMapper;


  @CacheEvict(value = "userChannel", key = "#request.user().id")
//  @PreAuthorize("#request.user.id == authentication.principal.id")
  @Transactional
  @Override
  public ReadStatusDto createReadStatus(ReadStatusCreateRequest request) {

    if (channelRepository.findById(request.channel().getId()).isEmpty()) {
      throw new ChannelNotFoundException(Map.of("channelId", request.channel().getId()));
    }

    if (userRepository.findById(request.user().getId()).isEmpty()) {
      throw new UserNotFoundException(Map.of("userId", request.user().getId()));
    }

    if (readStatusRepository.existsByChannelIdAndUserId(request.channel().getId(),
        request.user().getId())) {
      throw new ReadStatusAlreadyExistsException(Map.of("userId", request.user().getId(),
          "channelId", request.channel().getId()));
    }

    boolean enabled = false;
    if (request.channel().getType() == ChannelType.PUBLIC) {
      enabled = true;
    }

    ReadStatus readStatus = ReadStatus.builder()
        .lastReadAt(request.lastReadAt())
        .user(request.user())
        .channel(request.channel())
        .notificationEnabled(enabled)
        .build();

    readStatusRepository.save(readStatus);

    return readStatusMapper.toDto(readStatus);
  }

  @Override
  public ReadStatusDto findReadStatusById(UUID readStatusId) {
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(
            () -> new ReadStatusNotFoundException(Map.of("readStatusId", readStatusId)));
    return readStatusMapper.toDto(readStatus);
  }

  @Override
  public List<ReadStatusDto> findAllByUserId(UUID userId) {
    // TODO 예외 처리
    return readStatusRepository.findAllByUserId(userId).stream()
        .map(readStatusMapper::toDto)
        .toList();
  }

  @Override
  public List<ReadStatus> findAllByChannelId(UUID channelId) {
    // TODO 예외 처리
    return readStatusRepository.findByChannelId(channelId);
  }


  @PreAuthorize("#id == authentication.principal.id")
  @Transactional
  @Override
  public ReadStatusDto updateReadStatus(
      UUID id, ReadStatusUpdateRequest readStatusUpdateRequest) {

    ReadStatus readStatus = readStatusRepository.findById(id)
        .orElseThrow(() -> new ReadStatusNotFoundException(Map.of("readStatusId", id)));

    if (readStatusUpdateRequest.newNotificationEnabled()) { // 우선은 항상 t/f 값이 들어온다고 상정
      readStatus.updateNotificationEnabled(true);
    } else {
      readStatus.updateNotificationEnabled(false);
    }

    readStatus.updateLastMessageReadAt(readStatusUpdateRequest.lastReadAt());
    readStatus.refreshUpdateAt();

    // JPA 의 더티 채킹으로 save 하지 않아도 DB에 자동 업데이트인데 전 미션 때 save를 빼먹었습니다

    return readStatusMapper.toDto(readStatus);
  }

  @Transactional
  @Override
  public void deleteReadStatusById(UUID id) {
    readStatusRepository.deleteById(id);

    ReadStatus readStatus = readStatusRepository.findById(id)
        .orElseThrow(() -> new ReadStatusNotFoundException(Map.of("id", id)));

    evictUserChannelCache(readStatus.getUser().getId());
  }

  @CacheEvict(value = "userChannel", key = "#userId")
  public void evictUserChannelCache(UUID userId) {
    log.info("readStatus 삭제로 인한 userChannel Cache 삭제 전파 : userId={}", userId);
  }
}
