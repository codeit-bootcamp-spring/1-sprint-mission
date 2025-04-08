package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.ReadStatusDto;
import com.sprint.mission.discodeit.dto.request.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.request.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.readstatus.ReadStatusAlreadyExistsException;
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

@RequiredArgsConstructor
@Service
@Slf4j
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final ReadStatusMapper readStatusMapper;

  @Transactional
  @Override
  public ReadStatusDto create(ReadStatusCreateRequest request) {
    UUID userId = request.userId();
    UUID channelId = request.channelId();

    User user = userRepository.findById(userId)
        .orElseThrow(
            () -> {
              log.error("[유저 조회 실패] 해당 유저를 찾을 수 없습니다. 유저 ID: {}", userId);
              return new UserNotFoundException();
            });

    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(
            () -> {
              log.error("[채널 조회 실패] 해당 채널을 찾을 수 없습니다. 채널 ID: {}", channelId);
              return new ChannelNotFoundException();
            }
        );

    if (readStatusRepository.existsByUserIdAndChannelId(userId, channelId)) {
      log.error("[읽음 정보 생성 실패] 이미 존재하는 읽음 정보입니다. 유저 ID: {}, 채널 ID: {}", userId, channelId);
      throw new ReadStatusAlreadyExistsException();
    }

    Instant lastReadAt = request.lastReadAt();
    ReadStatus readStatus = new ReadStatus(user, channel, lastReadAt);
    log.info("[읽음 정보 생성 시도] 읽음 정보 ID: {}", readStatus.getId());

    readStatusRepository.save(readStatus);
    log.info("[읽음 정보 생성 성공] 읽음 정보 ID: {}", readStatus.getId());

    return readStatusMapper.toDto(readStatus);
  }

  @Override
  public ReadStatusDto find(UUID readStatusId) {
    log.info("[읽음 정보 조회 시도] 읽음 정보 ID: {}", readStatusId);

    return readStatusRepository.findById(readStatusId)
        .map(readStatusMapper::toDto)
        .orElseThrow(
            () -> {
              log.error("[읽음 정보 조회 실패] 해당 읽음 정보를 찾을 수 없습니다. 읽음 정보 ID: {}", readStatusId);
              return new ReadStatusNotFoundException();
            });
  }

  @Override
  public List<ReadStatusDto> findAllByUserId(UUID userId) {
    log.info("[유저의 모든 읽음 정보 조회 시도] 읽음 정보 ID: {}", userId);

    return readStatusRepository.findAllByUserId(userId).stream()
        .map(readStatusMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public ReadStatusDto update(UUID readStatusId, ReadStatusUpdateRequest request) {
    log.info("[읽음 정보 수정 시도] 읽음 정보 ID: {}", readStatusId);

    Instant newLastReadAt = request.newLastReadAt();
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(
            () -> {
              log.error("[읽음 정보 조회 실패] 해당하는 읽음 정보를 찾을 수 없습니다. 읽음 정보 ID: {}", readStatusId);
              return new ReadStatusNotFoundException();
            });
    readStatus.update(newLastReadAt);
    log.info("[읽음 정보 수정 성공] 읽음 정보 ID: {}", readStatusId);

    return readStatusMapper.toDto(readStatus);
  }

  @Transactional
  @Override
  public void delete(UUID readStatusId) {
    log.info("[읽음 정보 삭제 시도] 읽음 정보 ID: {}", readStatusId);

    if (!readStatusRepository.existsById(readStatusId)) {
      log.error("[읽음 정보 조회 실패] 해당하는 읽음 정보가 없습니다. 읽음 정보 ID: {}", readStatusId);
      throw new ReadStatusNotFoundException();
    }

    readStatusRepository.deleteById(readStatusId);
    log.info("[읽음 정보 삭제 성공] 읽음 정보 ID: {}", readStatusId);
  }
}
