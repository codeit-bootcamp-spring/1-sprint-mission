package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

  //한 userId의 모든 readStatus반환
  List<ReadStatus> findAllByUserId(UUID userId);

  //LatestTime을 ChannelId에 대하여 반환
  Instant findLatestTimeByChannelId(UUID channeId);

  void deleteByChannelId(UUID id);

  List<UUID> findAllUserIdByChannelId(UUID uuid);

  boolean existsByChannelId(UUID uuid);
  

}
