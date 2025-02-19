package com.spirnt.mission.discodeit.service;

import com.spirnt.mission.discodeit.dto.readStatus.ReadStatusDto;
import com.spirnt.mission.discodeit.enity.ReadStatus;

import java.util.List;
import java.util.UUID;

public interface ReadStatusService {
    ReadStatus create(ReadStatusDto readStatusDto);

    ReadStatus find(UUID readStatusId);

    List<ReadStatus> findAllByUserId(UUID userId);

    List<ReadStatus> findAllByChannelId(UUID ChannelId);

    ReadStatus update(UUID readStatusId, ReadStatusDto readStatusDto);

    void delete(UUID id);

    void deleteByChannelId(UUID channelId);

    boolean existsByUserIdChannelId(UUID userId, UUID channelId);
}
