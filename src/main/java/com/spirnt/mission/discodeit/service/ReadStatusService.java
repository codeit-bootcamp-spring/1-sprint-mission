package com.spirnt.mission.discodeit.service;

import com.spirnt.mission.discodeit.dto.readStatus.ReadStatusCreateRequest;
import com.spirnt.mission.discodeit.dto.readStatus.ReadStatusUpdateRequest;
import com.spirnt.mission.discodeit.enity.ReadStatus;
import java.util.List;
import java.util.UUID;

public interface ReadStatusService {

  ReadStatus create(ReadStatusCreateRequest readStatusCreateRequest);

  ReadStatus find(UUID readStatusId);

  ReadStatus findByUserIdAndChannelId(UUID userId, UUID channelId);

  List<ReadStatus> findAllByUserId(UUID userId);

  List<ReadStatus> findAllByChannelId(UUID ChannelId);

  ReadStatus update(UUID readStatusId, ReadStatusUpdateRequest readStatusUpdateRequest);

  void delete(UUID id);

  void deleteByChannelId(UUID channelId);

  boolean existsByUserIdChannelId(UUID userId, UUID channelId);
}
