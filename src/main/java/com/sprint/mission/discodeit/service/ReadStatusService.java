package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readStatusDto.CreateReadStatusRequestDto;
import com.sprint.mission.discodeit.dto.readStatusDto.FindReadStatusResponseDto;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    FindReadStatusResponseDto create(CreateReadStatusRequestDto createReadStatusRequestDto);
    FindReadStatusResponseDto find(UUID id);
    List<FindReadStatusResponseDto> findAllByUserId(UUID userId);
    FindReadStatusResponseDto update(UUID id);
    void delete(UUID id);
}
