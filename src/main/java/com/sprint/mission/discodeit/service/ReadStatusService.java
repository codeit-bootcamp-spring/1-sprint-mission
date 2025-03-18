package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readStatusDto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatusDto.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatusDto.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.duplication.DuplicateResourceException;
import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final ReadStatusMapper readStatusMapper;

  public ReadStatusDto create(ReadStatusCreateRequest request) {
    // 예외 처리
    Channel channel = channelRepository.findById(request.channelId())
        .orElseThrow(() -> new NoSuchElementException("Channel not found."));
    User user = userRepository.findById(request.userId())
        .orElseThrow(() -> new NoSuchElementException("User not found."));
    if (readStatusRepository.findAllByUserId(request.userId()).stream()
        .anyMatch(readStatus -> readStatus.getChannel().getId().equals(request.channelId()))) {
      throw new DuplicateResourceException(
          "ReadStatus already exists. " + "User id: " + request.userId() + ". Channel id: "
              + request.channelId());
    }

    ReadStatus readStatus = new ReadStatus(user, channel, request.lastReadAt());
    readStatusRepository.save(readStatus);
    return readStatusMapper.toDto(readStatus);
  }

  public void create(List<User> users, Channel channel) {
    List<ReadStatus> readStatuses = users.stream()
        .map(user -> new ReadStatus(user, channel, Instant.MIN))
        .toList();
    readStatusRepository.saveAll(readStatuses);
  }

  @Transactional(readOnly = true)
  public ReadStatusDto findById(UUID readStatusId) {
    return readStatusRepository.findById(readStatusId).map(readStatusMapper::toDto)
        .orElseThrow(() -> new ResourceNotFoundException("Read status not found: " + readStatusId));
  }

  @Transactional(readOnly = true)
  public List<ReadStatusDto> findAllByUserId(UUID userId) {
    return readStatusRepository.findAllByUserId(userId).stream()
        .map(readStatusMapper::toDto)
        .toList();
  }

  @Transactional(readOnly = true)
  public List<ReadStatusDto> findAllByChannelId(UUID channelId) {
    return readStatusRepository.findAllByChannelId(channelId).stream()
        .map(readStatusMapper::toDto)
        .toList();
  }

  public ReadStatusDto update(UUID readStatusId, ReadStatusUpdateRequest request) {
    Instant newLastReadAt = request.newLastReadAt();
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> new ResourceNotFoundException("Read status not found: " + readStatusId));
    readStatus.update(newLastReadAt);
    readStatusRepository.save(readStatus);
    return readStatusMapper.toDto(readStatus);
  }

  public void delete(UUID readStatusId) {
    if (!readStatusRepository.existsById(readStatusId)) {
      throw new ResourceNotFoundException("Read status not found: " + readStatusId);
    }
    readStatusRepository.deleteById(readStatusId);
  }

  public void deleteAllByChannelId(UUID channelId) {
    readStatusRepository.deleteAllByChannelId(channelId);
  }
}
