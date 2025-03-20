package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.status.read.ReadStatus;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

  @EntityGraph(attributePaths = {"user", "channel"})
  ReadStatus findFirstByChannelOrderByLastReadAtDesc(Channel channel);

  @EntityGraph(attributePaths = {"user", "user.profile", "user.status", "channel"})
  @Query("select r from ReadStatus r join fetch r.channel where r.user.id = :userId")
  List<ReadStatus> findAllChannelsInUser(@Param("userId") UUID userId);

  @EntityGraph(attributePaths = {"user.profile", "user.status", "channel"})
  List<ReadStatus> findAllByChannel(Channel channel);

  List<ReadStatus> findAllByUser_Id(UUID userId);

  Optional<ReadStatus> findReadStatusByUser_IdAndChannel_Id(UUID userId, UUID channelId);
}
