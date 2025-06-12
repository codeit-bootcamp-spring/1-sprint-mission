package com.sprint.mission.discodeit.repository.jpa;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

  List<ReadStatus> findAllByChannel(Channel channel);

  List<ReadStatus> findAllByUser_Id(UUID userId);

  Boolean existsByUserIdAndChannelId(UUID userId, UUID channelId);

  //테스트 하려고 추가한 메서드
  List<ReadStatus> findAllByChannel_Id(UUID channelId);

  @Query("select rs.user.id from ReadStatus rs " +
          "where rs.channel.id = :channelId and rs.notificationEnabled = true")
  List<UUID> findUserIdsByChannelIdAndNotificationEnabledTrue(@Param("channelId") UUID channelId);
}
