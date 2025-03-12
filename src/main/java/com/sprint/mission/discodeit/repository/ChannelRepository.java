package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChannelRepository extends JpaRepository<Channel, UUID> {

  //delete
  boolean delete(UUID id);

  //유저가 속한 채널만 가져오는 쿼리
  //Channels과 ReadStatuses를 fetch 조인 하여 user_id가 같은 channel들만 가져옴
  @Query("SELECT DISTINCT c FROM Channel c " +
      "JOIN FETCH c.users rs " +
      "WHERE rs.user.id = :userId")
  List<Channel> findChannelsWithReadStatusByUserId(@Param("userId") UUID userId);

  //private 채널이면서 channel Id가 같은 채널을 조회
  //Channels과 ReadStatuses, Users를 fetch 조인
  //해당 channel에 속한 모든 participants까지 가져올 수 있도록 한다.
  @Query("SELECT DISTINCT c FROM Channel c " +
      "JOIN FETCH c.users rs " +
      "JOIN FETCH rs.user u " +
      "LEFT JOIN FETCH u.userStatus " +
      "WHERE c.id = :channelId AND c.type = 'PRIVATE'")
  Optional<Channel> findPrivateChannelWithParticipantsById(@Param("channelId") UUID channelId);
  
}
