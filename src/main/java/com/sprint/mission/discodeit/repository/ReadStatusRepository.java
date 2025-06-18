package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

  @Query("SELECT rs FROM ReadStatus rs "
      + "JOIN FETCH rs.channel "
      + "JOIN FETCH rs.user "
      + "WHERE rs.user.id = :userId")
  List<ReadStatus> findByUserId(@Param("userId") UUID userId);

  List<ReadStatus> findByChannel(Channel channel);

  @Query("SELECT rs FROM ReadStatus rs "
      + "JOIN FETCH rs.user "
      + "JOIN FETCH rs.channel "
      + "WHERE rs.channel.id = :channelId "
      + "AND rs.notificationEnabled = true ")
  List<ReadStatus> findByChannelIdAndNotificationEnabledTure(@Param("channelId") UUID channelId);

  void deleteByChannel(Channel channel);

  boolean existsByUser_IdAndChannel_Id(UUID userId, UUID channelId);
}
