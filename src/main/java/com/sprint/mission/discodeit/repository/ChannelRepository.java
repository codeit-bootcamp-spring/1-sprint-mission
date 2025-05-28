package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, UUID> {

  @Query("""
      select distinct c
      from Channel c
      left join fetch c.owner
      left join fetch c.members m
      where
          c.channelType = 'PUBLIC'
          or c.owner.id = :userId
          or m.user.id = :userId
      """)
  List<Channel> findAllByUserId(@Param("userId") UUID userId);
}
