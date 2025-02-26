package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateResponse;
import com.sprint.mission.discodeit.dto.readStatus.ReadStautsfindAllByUserIdResponse;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

  ReadStatus createReadStatus(ReadStatusCreateRequest readStatusRequest);

  ReadStatus findReadStatusById(UUID readStatusId);

  List<ReadStatus> findAllByUserId(UUID userId);

  List<ReadStatus> findAllByChannelId(UUID channelId);


  ReadStatus updateReadStatus(UUID id,
      ReadStatusUpdateRequest readStatusUpdateRequest);

  void deleteReadStatusById(UUID readStatusId);

}
