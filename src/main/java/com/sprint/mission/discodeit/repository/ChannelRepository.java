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

  @Query("select c from Channel c LEFT JOIN ReadStatus r ON c.id = r.channel.id WHERE c.type = :type or r.user.id = :userId")
  List<Channel> findAllByUserIdOrType(
      @Param("userId") UUID userId,
      @Param("type") Channel.ChannelType type
  );
}
