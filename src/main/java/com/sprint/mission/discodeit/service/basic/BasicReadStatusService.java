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
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistsException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
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

    User user = getUserById(userId);
    Channel channel = getChannelById(channelId);

    if (isExistsReadStatusByUserAndChannel(user, channel)) {
      throw ReadStatusAlreadyExistsException.of(userId, channelId);
    }

    Instant lastReadAt = request.lastReadAt();
    ReadStatus readStatus = new ReadStatus(user, channel, lastReadAt);
    readStatusRepository.save(readStatus);

    return readStatusMapper.toDto(readStatus);
  }

  private User getUserById(UUID userId) {
    return userRepository.findById(userId)
        .orElseThrow(() -> UserAlreadyExistsException.of(userId));
  }

  private Channel getChannelById(UUID channelId) {
    return channelRepository.findById(channelId)
        .orElseThrow(() -> ChannelNotFoundException.of(channelId));
  }

  private Boolean isExistsReadStatusByUserAndChannel(User user, Channel channel) {
    return readStatusRepository.existsByUserIdAndChannelId(user.getId(), channel.getId());
  }

  @Override
  public ReadStatusDto find(UUID readStatusId) {
    ReadStatus foundReadStatus = getReadStatus(readStatusId);
    return readStatusMapper.toDto(foundReadStatus);
  }

  @Override
  public List<ReadStatusDto> findAllByUserId(UUID userId) {
    return readStatusRepository.findAllByUserId(userId).stream()
        .map(readStatusMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public ReadStatusDto update(UUID readStatusId, ReadStatusUpdateRequest request) {
    Instant newLastReadAt = request.newLastReadAt();
    ReadStatus readStatus = getReadStatus(readStatusId);
    readStatus.update(newLastReadAt);
    return readStatusMapper.toDto(readStatus);
  }

  @Transactional
  @Override
  public void delete(UUID readStatusId) {
    if (readStatusRepository.existsById(readStatusId)) {
      readStatusRepository.deleteById(readStatusId);
    }

    throw ReadStatusNotFoundException.of(readStatusId);
  }

  private ReadStatus getReadStatus(UUID readStatusId) {
    return readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> ReadStatusNotFoundException.of(readStatusId));
  }
}
