package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.ReadStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {
    ReadStatus save(ReadStatus readStatus);

    Optional<ReadStatus> findById(UUID id);

    Optional<ReadStatus> findByChannelId(UUID channelID);

    List<ReadStatus> findAllByUserId(UUID userID);

    void delete(ReadStatus readStatus);
}
