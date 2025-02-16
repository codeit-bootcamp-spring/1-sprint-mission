package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReadStatusRepository {
    // 저장
    void save(ReadStatus readStatus);

    // 조회
    Optional<ReadStatus> findById(UUID id);
    // 사용자가 특정 채널에서 마지막으로 읽은 메세지 확인
    Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId);
    // 사용자가 읽은 모든 채널 조회
    List<ReadStatus> findByUserId(UUID userId);
    // 특정 채널에서 모든 사용자의 읽음 상태 조회
    List<ReadStatus> findByChannelId(UUID channelId);

    // 삭제
    void deleteById(UUID id);
}
