package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

  List<ReadStatus> findAllByUserId(UUID userId); //한 id의 모든 readStatus반환

  Instant findLatestTimeByChannelId(UUID channeId);

  void deleteByChannelId(UUID id);

  List<UUID> findAllUserIdByChannelId(UUID uuid);

  boolean existByChannelId(UUID uuid);

//  @Override
//  public List<ReadStatus> findAllByUserId(UUID userId) {
//    List<ReadStatus> readStatusListByUserId = readStatusMap.values().stream()
//        .filter(readStatus -> readStatus.getUserId().equals(userId))
//        .toList();
//    if (readStatusListByUserId.isEmpty()) {
//      throw new IllegalArgumentException("해당 유저에 대한 객체가 존재하지 않습니다.");
//    }
//    return readStatusListByUserId;
//  }
//
//
//  @Override
//  public List<UUID> findAllUserIdByChannelId(UUID channelId) {
//    List<UUID> readStatusListByChannelId = readStatusMap.values().stream()
//        .filter(readStatus -> readStatus.getChannelId().equals(channelId))
//        .map(ReadStatus::getUserId)
//        .toList();
//    if (readStatusListByChannelId.isEmpty()) {
//      throw new IllegalArgumentException("해당 채널에 대한 객체가 존재하지 않습니다.");
//    }
//    return readStatusListByChannelId;
//  }
//
//  @Override
//  public Instant findLatestTimeByChannelId(UUID channeId) {
//    return
//        readStatusMap.values().stream()
//            .filter(readStatus -> readStatus.getChannelId().equals(channeId))
//            .map(ReadStatus::getChannelLastReadTimes)
//            .max(Comparator.naturalOrder()) //가장 최신 시간
//            .orElseThrow(() -> new IllegalArgumentException("해당 객체를 찾을 수 없습니다."));
//
//  }
//
//  @Override
//  public boolean existByChannelId(UUID uuid) {
//    return readStatusMap.values().stream()
//        .anyMatch(readStatus -> readStatus.getChannelId().equals(uuid));
//  }
//
//
//
//  @Override
//  public void deleteByChannelId(UUID channelId) {
//
//    boolean removed = readStatusMap.values().removeIf(readStatus -> readStatus.getChannelId().equals(channelId));
//    if (!removed) {
//      throw new IllegalArgumentException("해당 채널 ID에 해당하는 객체를 삭제할 수 없습니다.");
//    }
//  }


}
