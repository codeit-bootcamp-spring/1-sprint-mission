package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusResponseDto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatusResponseDto create(ReadStatusRequestDto readStatusReqDto);

    ReadStatusResponseDto find(UUID readStatusId);

    List<ReadStatusResponseDto> findAllByUserId(UUID userId);

    ReadStatusResponseDto update(UUID readStatusId, Instant lastReadAt);

    ReadStatusResponseDto delete(UUID readStatusId);
}
