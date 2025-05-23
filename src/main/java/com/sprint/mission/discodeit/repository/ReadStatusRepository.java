package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.status.ReadStatus;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {


  Optional<ReadStatus> findByChannelIdAndUserId(UUID channelId, UUID userId);

  List<ReadStatus> findByUserId(UUID userId);

  List<ReadStatus> findByChannelId(UUID channelId);

  @Query("SELECT r FROM ReadStatus r "
      + "JOIN FETCH r.user u "
      + "LEFT JOIN FETCH u.profile "
      + "WHERE r.channel.id = :channelId")
  List<ReadStatus> findAllByChannelIdWithUser(@Param("channelId") UUID channelId);
}
