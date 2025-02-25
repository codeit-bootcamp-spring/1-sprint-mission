package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.ReadStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {
    ReadStatus save(ReadStatus readStatus);

    List<ReadStatus> saveAll(List<ReadStatus> readStatuses);

    Optional<ReadStatus> findById(UUID id);

    List<ReadStatus> findAllByChannelId(UUID channelID);

    List<ReadStatus> findAllByUserId(UUID userID);

    void delete(ReadStatus readStatus);

    void deleteAll(List<ReadStatus> readStatuses);
}
