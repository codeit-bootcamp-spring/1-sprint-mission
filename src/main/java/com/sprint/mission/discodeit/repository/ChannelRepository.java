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

  @Query("SELECT c FROM Channel c LEFT JOIN ReadStatus r ON c.id = r.channel.id WHERE c.type=c.ChannelType.PUBLIC or r.user.id= :userId")
  List<Channel> findAllByUserIdOrPublicType(@Param("userId") UUID userId);
}
