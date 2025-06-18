package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusResponseDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.exception.readstatus.ReadStatusNotFoundException;
import com.sprint.mission.discodeit.mapper.ReadStatusMapper;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ReadStatusService;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BasicReadStatusService implements ReadStatusService {

  private final ReadStatusRepository readStatusRepository;
  private final ReadStatusMapper readStatusMapper;

  @Transactional
  @Override
  public ReadStatusResponseDto create(ReadStatusRequestDto readStatusReqDto) {

    ReadStatus readStatus = readStatusMapper.toEntity(readStatusReqDto);
    readStatusRepository.save(readStatus);
    return readStatusMapper.toResponseDto(readStatus);
  }

  @Override
  public ReadStatusResponseDto find(UUID readStatusId) {
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> new ReadStatusNotFoundException(readStatusId) {
        });
    return readStatusMapper.toResponseDto(readStatus);
  }

  @Override
  public List<ReadStatusResponseDto> findAllByUserId(UUID userId) {
    return readStatusRepository.findAll().stream()
        .filter(readStatus -> readStatus.getUser().getId().equals(userId))
        .map(readStatusMapper::toResponseDto)
        .collect(Collectors.toList());
  }

  @Transactional
  @Override
  public ReadStatusResponseDto update(UUID readStatusId,
      ReadStatusUpdateRequestDto updateRequestDto) {
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> new ReadStatusNotFoundException(readStatusId));
    // 마지막에 읽은 것만 업데이트 하면 됨
    readStatus.updateLastReadAt(updateRequestDto.newLastReadAt());
    readStatus.updateNotificationEnabled(updateRequestDto.notificationEnabled());

    return readStatusMapper.toResponseDto(readStatus);
  }

  @Transactional
  @Override
  public ReadStatusResponseDto delete(UUID readStatusId) {
    ReadStatus readStatus = readStatusRepository.findById(readStatusId)
        .orElseThrow(() -> new ReadStatusNotFoundException(readStatusId));
    readStatusRepository.delete(readStatus);
    return readStatusMapper.toResponseDto(readStatus);
  }
}
