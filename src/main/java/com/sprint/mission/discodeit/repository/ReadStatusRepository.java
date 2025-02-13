package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {
    ReadStatus save(ReadStatus readStatus);
    Optional<ReadStatus> findById(UUID id);
    List<ReadStatus> findAllByUserId(UUID userId);
    List<ReadStatus> findAllByChannelId(UUID channelId);
    boolean existsById(UUID id);
    void deleteById(UUID id);
    void deleteAllByChannelId(UUID channelId);

//    ReadStatus save(ReadStatus readStatus);
//    Optional<ReadStatus> findByChannelId(UUID channelId);
//    Optional<ReadStatus> findByUserId(UUID userId);
//    Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId);
//    List<ReadStatus> findAllByUserId(UUID userId);
//    Optional<ReadStatus> findById(UUID id);
//    void delete(UUID readStatusId);
//    void deleteByChannelId(UUID channelId);
//    boolean existsByUserIdAndChannelId(UUID userId, UUID channelId);
//

}

