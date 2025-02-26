package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository{
    ReadStatus save(ReadStatus readStatus);
    Optional<ReadStatus> findById(UUID readStatusId);
    Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId);
    List<ReadStatus> findAllByUserId(UUID userId);
    List<ReadStatus> findAllByChannelId(UUID id);
    List<ReadStatus> findAll();
    boolean existsById(UUID id);
    void deleteById(UUID id);
    void deleteAllByChannelId(UUID id);
}
