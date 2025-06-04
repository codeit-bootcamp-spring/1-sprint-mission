package com.sprint.mission.discodeit.service.Interface;

import com.sprint.mission.discodeit.dto.readstatus.CreateReadStatusRequestDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.readstatus.UpdateReadStatusRequestDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

    ReadStatusDto create(CreateReadStatusRequestDto request);

    ReadStatus find(UUID id);

    List<ReadStatusDto> findAllByUserId(UUID userId);

    ReadStatusDto update(UUID readStatusId, UpdateReadStatusRequestDto request);

    List<ReadStatus> findAllByChannelId(UUID userId);

    void delete(UUID id);

    void deleteByChannelId(UUID id);
}
