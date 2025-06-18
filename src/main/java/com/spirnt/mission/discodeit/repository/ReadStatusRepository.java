package com.spirnt.mission.discodeit.repository;

import com.spirnt.mission.discodeit.entity.ReadStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

    @Query("SELECT distinct rs from ReadStatus rs where rs.user.id=:userId")
    List<ReadStatus> findAllByUserId(UUID userId);

    List<ReadStatus> findAllByChannelId(UUID channelId);

    boolean existsByUserIdAndChannelId(UUID userId, UUID channelId);

    @Query("SELECT rs FROM ReadStatus rs WHERE rs.channel.id = :channelId AND rs.notificationEnabled = true")
    List<ReadStatus> findAllByChannelIdAndNotificationEnabledTrue(UUID channelId);
}
