package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChannelRepository extends JpaRepository<Channel, UUID> {

  @EntityGraph(attributePaths = {"readStatuses.user", "readStatuses.user.profile",
      "readStatuses.user.userStatus"})
  @Query("SELECT c FROM Channel c WHERE c.id IN "
      + "(SELECT rs.channel.id FROM ReadStatus rs WHERE rs.user.id = :userId)")
  List<Channel> findAllByUserIdWithParticipants(@Param("userId") UUID userId);


}
