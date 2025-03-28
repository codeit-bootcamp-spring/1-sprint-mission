package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

  // 해당 유저의 모든 읽음 상태 검색
  List<ReadStatus> findAllByUserId(UUID userId);

  // 해당 채널의 모든 읽음 상태 검색
  @Query("SELECT r FROM ReadStatus r "
      + "JOIN FETCH r.user u "    // N+1 방지
      + "JOIN FETCH u.status "    // N+1 방지
      + "LEFT JOIN FETCH u.profile "    // N+1 방지, 없으면 null
      + "WHERE r.channel.id = :channelId")
  List<ReadStatus> findAllByChannelIdWithUser(@Param("channelId") UUID channelId);

  // 유저 id와 채널 id 조합이 존재하는지 확인
  Boolean existsByUserIdAndChannelId(UUID userId, UUID channelId);

  // 해당 채널 읽음 상태 모두 삭제
  void deleteAllByChannelId(UUID channelId);
}
