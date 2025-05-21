package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.readstatus.ReadStatusAlreadyExistException;
import com.sprint.mission.discodeit.exception.readstatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final ReadStatusMapper readStatusMapper;

  @Override
  public ReadStatusDto create(ReadStatusCreateRequest request) {

    log.debug("읽음 상태 생성 시작: userId={}, channelId={}", request.userId(), request.channelId());

    UUID userId = request.userId();
    UUID channelId = request.channelId();

    User user = userRepository.findById(userId)
        .orElseThrow(() -> UserNotFoundException.withId(userId));
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> ChannelNotFoundException.withId(channelId));

    if (readStatusRepository.existsByUserIdAndChannelId(userId, channelId)) {
      throw ReadStatusAlreadyExistException.withUserIdAndChannelId(userId, channelId);
    }

    Instant lastReadAt = request.lastReadAt();
    ReadStatus readStatus = new ReadStatus(user, channel, lastReadAt);
    readStatusRepository.save(readStatus);

    log.info("읽음 상태 생성 완료: id={}, userId={}, channelId={}",
        readStatus.getId(), readStatus.getUser().getId(), readStatus.getChannel().getId());

    return readStatusMapper.toDto(readStatus);
  }

  @Override
  @Transactional(readOnly = true)
  public ReadStatusDto find(UUID readStatusId) {

    log.debug("읽음 상태 조회 시작: id={}", readStatusId);

    ReadStatusDto readStatusDto = readStatusRepository.findById(readStatusId)
        .map(readStatusMapper::toDto)
        .orElseThrow(() -> ReadStatusNotFoundException.withId(readStatusId));

    log.info("읽음 상태 조회 완료: id={}", readStatusId);

    return readStatusDto;
  }

  @Override
  @Transactional(readOnly = true)
  public List<ReadStatusDto> findAllByUserId(UUID userId) {

    log.debug("사용자별 읽음 상태 목록 조회 시작: userId={}", userId);

    List<ReadStatusDto> readStatusDtos = readStatusRepository.findAllByUserId(userId).stream()
        .map(readStatusMapper::toDto)
        .toList();

    log.info("사용자별 읽음 상태 목록 조회 완료: userId={}, 조회된 항목 수={}", userId, readStatusDtos.size());

    return readStatusDtos;
  }

  @Override
  public ReadStatusDto update(UUID readStatusId, ReadStatusUpdateRequest request) {

    log.debug("읽음 상태 수정 시작: id={}, newLastReadAt={}", readStatusId, request.newLastReadAt());

    Instant newLastReadAt = request.newLastReadAt();
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> ReadStatusNotFoundException.withId(readStatusId));
    readStatus.update(newLastReadAt);

    log.info("읽음 상태 수정 완료: id={}", readStatusId);

    return readStatusMapper.toDto(readStatus);
  }

  @Override
  public void delete(UUID readStatusId) {

    log.debug("읽음 상태 삭제 시작: id={}", readStatusId);

    if (!readStatusRepository.existsById(readStatusId)) {
      throw ReadStatusNotFoundException.withId(readStatusId);
    }
    readStatusRepository.deleteById(readStatusId);

    log.info("읽음 상태 삭제 완료: id={}", readStatusId);
  }
}