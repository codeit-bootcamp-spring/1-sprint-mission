package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChannelRepository extends JpaRepository<Channel, UUID> {

  List<Channel> findAllByTypeOrIdIn(ChannelType type, List<UUID> ids);

  @Query("SELECT u.id FROM Channel c JOIN c.members u WHERE c.id = :channelId")
  Set<UUID> findMemberIdsByChannelId(@Param("channelId") UUID channelId);

}
