package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.status.ReadStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {


  Optional<ReadStatus> findByChannelIdAndUserId(UUID channelId, UUID userId);

  List<ReadStatus> findByUserId(UUID userId);

  List<ReadStatus> findByChannelId(UUID channelId);

}
