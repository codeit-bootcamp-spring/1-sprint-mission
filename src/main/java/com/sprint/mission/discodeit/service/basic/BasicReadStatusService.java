package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.validation.ReadStatusValidator;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final ReadStatusValidator readStatusValidator;
  private final ReadStatusMapper readStatusMapper;

  @Override
  public ReadStatusDto create(ReadStatusCreateRequest request) {

    UUID userId = request.userId();
    UUID channelId = request.channelId();
    Instant lastReadTime = request.lastReadTime();

    readStatusValidator.validateUser(userId);
    readStatusValidator.validateChannel(channelId);
    readStatusValidator.validateDuplicateReadStatus(userId, channelId);

    ReadStatus readStatus = new ReadStatus(userId, channelId, lastReadTime);
    ReadStatus createdReadStatus = readStatusRepository.save(readStatus);

    return readStatusMapper.readStatusEntityToDto(createdReadStatus);
  }

  @Override
  public ReadStatusDto findById(UUID readStatusId) {
    return readStatusRepository.findById(readStatusId)
        .map(readStatusMapper::readStatusEntityToDto)
        .orElseThrow(() -> new ResourceNotFoundException("ReadStatus not found: " + readStatusId));
  }

  @Override
  public List<ReadStatusDto> findAllByUserId(UUID userId) {
    return readStatusRepository.findAllByUserId(userId).stream()
        .map(readStatusMapper::readStatusEntityToDto)
        .toList();
  }

  @Override
  public ReadStatusDto update(UUID readStatusId, ReadStatusUpdateRequest request) {
    Instant newLastReadTime = request.newLastReadTime();
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> new ResourceNotFoundException("Read status not found: " + readStatusId));
    readStatus.update(newLastReadTime);
    ReadStatus updatedReadStatus = readStatusRepository.save(readStatus);
    return readStatusMapper.readStatusEntityToDto(updatedReadStatus);
  }

  @Override
  public void delete(UUID readStatusId) {
    readStatusValidator.validateReadStatus(readStatusId);
    readStatusRepository.deleteById(readStatusId);
  }
}
