package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readStatus.CreateReadStatusRequestDto;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusDto;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatusDto create(CreateReadStatusRequestDto createReadStatusRequestDto);
    ReadStatusDto find(UUID id);
    List<ReadStatusDto> findAllByUserId(UUID userId);
    ReadStatusDto update(UUID id);
    void delete(UUID id);
    void deleteByChannelId(UUID channelId);
}
