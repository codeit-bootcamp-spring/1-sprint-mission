package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

  List<ReadStatus> findAllUserId(UUID userId);

  List<ReadStatus> findAllChannelId(UUID channelId);

  void deleteAllByChannelId(UUID id);

  boolean existsByUserIdAndChannelId(UUID userid, UUID channelId);
}
