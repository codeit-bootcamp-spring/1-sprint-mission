package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readStatus.CreateReadStatusRequestDto;
import com.sprint.mission.discodeit.dto.readStatus.FindReadStatusResponseDto;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    FindReadStatusResponseDto create(CreateReadStatusRequestDto createReadStatusRequestDto);
    FindReadStatusResponseDto find(UUID id);
    List<FindReadStatusResponseDto> findAllByUserId(UUID userId);
    FindReadStatusResponseDto update(UUID id);
    void delete(UUID id);
}
