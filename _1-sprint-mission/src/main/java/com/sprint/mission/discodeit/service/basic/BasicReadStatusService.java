package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.readstatus.ExistUserAndChannelException;
import com.sprint.mission.discodeit.exception.readstatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundExeption;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.time.Instant;
import java.util.Map;
import lombok.Locked.Read;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final ReadStatusMapper readStatusMapper;

  @Transactional
  @Override
  public ReadStatusDto create(ReadStatusCreateRequest readStatusCreateRequest) {
    UUID userId = readStatusCreateRequest.userId();
    UUID channelId = readStatusCreateRequest.channelId();

    //유저와 채널 존재 여부 확인
    User user = userRepository.findById(userId)
        .orElseThrow(
            () -> new UserNotFoundExeption(ErrorCode.USER_NOT_FOUND, Map.of("userId", userId)));
    Channel channel = channelRepository.findById(channelId)
        .orElseThrow(
            () -> new ChannelNotFoundException(ErrorCode.CHANNEL_NOT_FOUND,
                Map.of("channelId", channelId))
        );

    //이미 존재하는 경우 예외 발생
    if (readStatusRepository.existsByUserIdAndChannelId(user.getId(), channel.getId())) {
      throw new ExistUserAndChannelException(ErrorCode.EXIST_USERID_OR_CHANNELID,
          Map.of("userId", userId, "channelId", channelId));
    }

    Instant lastReadAt = readStatusCreateRequest.lastReadAt();
    ReadStatus readStatus = new ReadStatus(user, channel, lastReadAt);
    readStatusRepository.save(readStatus);

    return readStatusMapper.toDto(readStatus);
  }

  @Override
  public ReadStatusDto findById(UUID readStatusId) {
    return readStatusRepository.findById(readStatusId)
        .map(readStatusMapper::toDto)
        .orElseThrow(
            () -> new ReadStatusNotFoundException(ErrorCode.USER_NOT_FOUND,
                Map.of("readStatusId", readStatusId)));
  }

  @Override
  public List<ReadStatusDto> findAllByUserId(UUID userId) {
    return readStatusRepository.findAllByUserId(userId).stream()
        .map(readStatusMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public ReadStatusDto update(UUID readStatusId, ReadStatusUpdateRequest readStatusUpdateRequest) {
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(
            () -> new ReadStatusNotFoundException(ErrorCode.USER_NOT_FOUND,
                Map.of("readStatusId", readStatusId)));
    readStatus.updateLastReadAt(readStatusUpdateRequest.newLastReadAt());
    return readStatusMapper.toDto(readStatus);
  }

  @Transactional
  @Override
  public void delete(UUID readStatusId) {
    if (!readStatusRepository.existsById(readStatusId)) {
      throw new ReadStatusNotFoundException(ErrorCode.USER_NOT_FOUND,
          Map.of("readStatusId", readStatusId));
    }
    readStatusRepository.deleteById(readStatusId);
  }


}
