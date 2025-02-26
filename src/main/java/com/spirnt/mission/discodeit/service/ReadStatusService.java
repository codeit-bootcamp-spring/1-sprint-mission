package com.spirnt.mission.discodeit.service;

import com.spirnt.mission.discodeit.dto.readStatus.ReadStatusCreate;
import com.spirnt.mission.discodeit.dto.readStatus.ReadStatusUpdate;
import com.spirnt.mission.discodeit.enity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatus create(ReadStatusCreate readStatusCreate);

    ReadStatus find(UUID readStatusId);

    ReadStatus findByUserIdAndChannelId(UUID userId, UUID channelId);

    List<ReadStatus> findAllByUserId(UUID userId);

    List<ReadStatus> findAllByChannelId(UUID ChannelId);

    ReadStatus update(UUID readStatusId, ReadStatusUpdate readStatusUpdate);

    void delete(UUID id);

    void deleteByChannelId(UUID channelId);

    boolean existsByUserIdChannelId(UUID userId, UUID channelId);
}
