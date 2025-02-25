package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readStatus.CreateReadStatusDto;
import com.sprint.mission.discodeit.dto.readStatus.UpdateReadStatusDto;
import com.sprint.mission.discodeit.entity.status.ReadStatus;

import java.util.List;

public interface ReadStatusService {

    ReadStatus create(CreateReadStatusDto createReadStatusDto);

    ReadStatus findById(String userStatusId);

    List<ReadStatus> findAllByUserId(String userId);

    List<ReadStatus> findAllByChannelId(String channelId);

    ReadStatus update(String id, UpdateReadStatusDto updateReadStatusDto);

    boolean delete(String userStatusId);
}
