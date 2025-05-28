package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

  boolean existsById(UUID id);

  boolean existsByChannelIdAndUserId(UUID channelId, UUID userId);

  List<ReadStatus> findAllByUserId(UUID userId);

  List<ReadStatus> findByChannelId(UUID channelId);
}
