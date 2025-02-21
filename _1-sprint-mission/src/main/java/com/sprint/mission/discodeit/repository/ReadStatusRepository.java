package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReadStatusRepository{
    ReadStatus save(ReadStatus readStatus);
    void saveAll(List<ReadStatus> readStatuses);
    Optional<ReadStatus> findById(UUID readStatusId);
    Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId);
    List<ReadStatus> findAllByUserId(UUID userId);
    List<ReadStatus> findAll();
    void deleteById(UUID id);
    boolean existsById(UUID id);
}
