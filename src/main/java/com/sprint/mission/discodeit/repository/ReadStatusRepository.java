package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

  @Query("select rs from ReadStatus rs where rs.channel.id = :channelId and rs.notificationEnabled = true")
  List<ReadStatus> findByChannelIdAndNotificationEnabledTrue(@Param("channelId") UUID channelId);
  
}
