package com.sprint.mission.discodeit.service.status;

import com.sprint.mission.discodeit.dto.response.ReadStatusDto;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.status.read.ReadStatus;
import com.sprint.mission.discodeit.entity.status.read.ReadStatusRequest;
import com.sprint.mission.discodeit.entity.status.read.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.readstatus.ReadStatusExistsException;
import com.sprint.mission.discodeit.exception.readstatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.mapper.entitymapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;

  /**
   * 읽음 상태 생성
   */
  public ReadStatusDto create(ReadStatusRequest request) {
    User user = userRepository.getReferenceById(request.userId());
    Channel channel = channelRepository.getReferenceById(request.channelId());

    IsAlreadyReadStatus(request, user, channel);

    ReadStatus readStatus = new ReadStatus(user, channel, request.lastReadAt());
    readStatusRepository.save(readStatus);
    log.info("read status 생성: {}", readStatus.getId());

    return ReadStatusMapper.toDto(readStatus);
  }

  /**
   * 읽음 상태 조회
   */
  public List<ReadStatusDto> findByUserId(UUID userId) {
    List<ReadStatus> readStatuses = readStatusRepository.findAllByUser_Id(userId);

    if (readStatuses.isEmpty()) {
      throw new ReadStatusNotFoundException(Instant.now(), ErrorCode.READ_STATUS_NOT_FOUND,
          Map.of(userId.toString(), ErrorCode.READ_STATUS_NOT_FOUND.getMessage())
      );
    }

    return readStatuses.stream()
        .map(ReadStatusMapper::toDto)
        .toList();
  }

  /**
   * 읽음 상태 수정
   */
  public ReadStatusDto update(UUID readStatusId, ReadStatusUpdateRequest request) {
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(
            () -> new ReadStatusNotFoundException(Instant.now(), ErrorCode.READ_STATUS_NOT_FOUND,
                Map.of(readStatusId.toString(), ErrorCode.READ_STATUS_NOT_FOUND.getMessage())
            ));

    ReadStatus modifiedStat = readStatus.changeLastReadAt(request.newLastReadAt());
    log.info("Read Status 수정: {}", readStatusId);

    return ReadStatusMapper.toDto(modifiedStat);
  }

  // read status가 이미 존재하는지 확인
  private void IsAlreadyReadStatus(ReadStatusRequest request, User user, Channel channel) {
    Optional<ReadStatus> findReadStatus = readStatusRepository.findReadStatusByUser_IdAndChannel_Id(
        user.getId(), channel.getId());

    if (findReadStatus.isPresent()) {
      throw new ReadStatusExistsException(Instant.now(), ErrorCode.EXIST_READ_STATUS,
          Map.of(user.getStatus().getId().toString(), ErrorCode.EXIST_READ_STATUS.getMessage())
      );
    }
  }
}
