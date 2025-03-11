package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChannelRepository extends JpaRepository<Channel, UUID> {

  boolean existsById(UUID id);


  @EntityGraph(attributePaths = {"readStatuses", "messages", "readStatuses.user"})
  @Query("""
      select c from Channel c
      where c.type='PUBLIC'
      or c.id in (
      select rs.channel.id from ReadStatus  rs where rs.user.id= :userId
      )
      """)
  List<Channel> findAllByUserId(UUID userId);

  @Query("select c from Channel c left join fetch c.readStatuses where c.id= : channelId")
  Optional<Channel> findWithReadStatus(@Param("channelId") UUID channelId);
}
