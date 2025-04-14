package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.CreateReadStatusRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.UpdateReadStatusRequest;
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
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final ReadStatusMapper readStatusMapper;

  @Override
  @Transactional
  public ReadStatusDto create(CreateReadStatusRequest request) {

    log.debug("ReadStatus 생성 시작: userId={}, channelId={}", request.userId(), request.channelId());

    UUID userId = request.userId();
    UUID channelId = request.channelId();

    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(() -> new ChannelNotFoundException(channelId));

    if (readStatusRepository.existsByUserIdAndChannelId(userId, channelId)) {
      throw new ReadStatusAlreadyExistException(userId, channelId);
    }

    Instant lastReadAt = request.lastReadAt();
    ReadStatus readStatus = new ReadStatus(user, channel, lastReadAt);
    readStatusRepository.save(readStatus);

    log.info("ReadStatus 생성 완료: id={}, userId={}, channelId={}",
        readStatus.getId(), userId, channelId);

    return readStatusMapper.toDto(readStatus);
  }

  @Override
  public ReadStatusDto find(UUID readStatusId) {

    log.debug("ReadStatus 조회 시작: id={}", readStatusId);

    ReadStatusDto readStatusDto = readStatusRepository.findById(readStatusId)
        .map(readStatusMapper::toDto)
        .orElseThrow(() -> new ReadStatusNotFoundException(readStatusId));

    log.info("ReadStatus 조회 완료: id={}", readStatusId);

    return readStatusDto;
  }

  @Override
  public List<ReadStatusDto> findAllByUserId(UUID userId) {

    log.debug("사용자별 ReadStatus 목록 조회 시작: userId={}", userId);

    List<ReadStatusDto> readStatusDtos = readStatusRepository.findAllByUserId(userId).stream()
        .map(readStatusMapper::toDto)
        .toList();

    log.info("사용자별 ReadStatus 목록 조회 완료: userId={}, 조회된 항목 수={}", userId, readStatusDtos.size());

    return readStatusDtos;
  }

  @Override
  @Transactional
  public ReadStatusDto update(UUID readStatusId, UpdateReadStatusRequest request) {

    log.debug("ReadStatus 수정 시작: id={}, newLastReadAt={}", readStatusId, request.newLastReadAt());

    Instant newLastReadAt = request.newLastReadAt();
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> new ReadStatusNotFoundException(readStatusId));
    readStatus.update(newLastReadAt);

    log.info("ReadStatus 수정 완료: id={}", readStatusId);

    return readStatusMapper.toDto(readStatus);
  }

  @Override
  @Transactional
  public void delete(UUID readStatusId) {

    log.debug("ReadStatus 삭제 시작: id={}", readStatusId);

    if (!readStatusRepository.existsById(readStatusId)) {
      throw new ReadStatusNotFoundException(readStatusId);
    }
    readStatusRepository.deleteById(readStatusId);
    log.info("ReadStatus 삭제 완료: id={}", readStatusId);
  }
}