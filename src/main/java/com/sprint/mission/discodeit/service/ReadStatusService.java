package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateDTO;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusFindDTO;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusUpdateDTO;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

    ReadStatus create(ReadStatusCreateDTO readStatusCreateDTO);

    ReadStatus findbyId(UUID uuid);

    List<ReadStatus> findAllByUserId(UUID userId);

    ReadStatus update(ReadStatusUpdateDTO readStatusUpdateDTO);

    void delete(UUID uuid);

    void deleteByChannelId(UUID id);
}
