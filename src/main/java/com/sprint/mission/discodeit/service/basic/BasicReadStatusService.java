package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatusDto.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatusDto.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatusDto.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.duplication.DuplicateResourceException;
import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;

  @Override
  public ReadStatusDto create(ReadStatusCreateRequest request) {
    UUID userId = request.userId();
    UUID channelId = request.channelId();

    if (!userRepository.existsById(userId)) {
      throw new ResourceNotFoundException("User not found: " + userId);
    }
    if (!channelRepository.existsById(channelId)) {
      throw new ResourceNotFoundException("Channel not found: " + channelId);
    }
    if (readStatusRepository.findAllByUserId(userId).stream()
        .anyMatch(readStatus -> readStatus.getChannelId().equals(channelId))) {
      throw new DuplicateResourceException(
          "ReadStatus already exists. " + "User id: " + userId + ". Channel id: " + channelId);
    }

    Instant lastReadAt = request.lastReadAt();
    ReadStatus readStatus = new ReadStatus(userId, channelId, lastReadAt);
    return toDto(readStatusRepository.save(readStatus));
  }

  @Override
  public ReadStatusDto findById(UUID readStatusId) {
    return readStatusRepository.findById(readStatusId)
        .map(this::toDto)
        .orElseThrow(() -> new ResourceNotFoundException("Read status not found: " + readStatusId));
  }

  @Override
  public List<ReadStatusDto> findAllByUserId(UUID userId) {
    return readStatusRepository.findAllByUserId(userId).stream()
        .map(this::toDto)
        .toList();
  }

  @Override
  public ReadStatusDto update(UUID readStatusId, ReadStatusUpdateRequest request) {
    Instant newLastReadAt = request.newLastReadAt();
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> new ResourceNotFoundException("Read status not found: " + readStatusId));
    readStatus.update(newLastReadAt);
    return toDto(readStatusRepository.save(readStatus));
  }

  @Override
  public void delete(UUID readStatusId) {
    if (!readStatusRepository.existsById(readStatusId)) {
      throw new ResourceNotFoundException("Read status not found: " + readStatusId);
    }
    readStatusRepository.deleteById(readStatusId);
  }

  private ReadStatusDto toDto(ReadStatus readStatus) {
    return new ReadStatusDto(readStatus.getId(),
        readStatus.getCreatedAt(),
        readStatus.getUpdatedAt(),
        readStatus.getUserId(),
        readStatus.getChannelId(),
        readStatus.getLastReadAt());
  }
}
