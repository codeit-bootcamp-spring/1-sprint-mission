package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReadStatusRepository {
    ReadStatus save(ReadStatus readStatus);

    ReadStatus findById(UUID id);

    List<ReadStatus> findAll();

    List<ReadStatus> findAllByUserId(UUID userId);

    List<ReadStatus> findAllByChannelId(UUID channelId);

    boolean existsById(UUID id);

    void deleteById(UUID id);

    void deleteAllByChannelId(UUID channelId);
}