package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {


    UUID save(ReadStatus readStatus);
    ReadStatus findOne(UUID id);
    List<ReadStatus> findAll();
    List<ReadStatus> findAllByUserId(UUID userId);
    List<ReadStatus> findAllByChannelId(UUID channelId);
    UUID update(ReadStatus readStatus);
    UUID delete(UUID id);
    void deleteByChannelId(UUID channelId);
    Optional<ReadStatus> findByUserIdAndChannlId(UUID userId, UUID channelId);
}

