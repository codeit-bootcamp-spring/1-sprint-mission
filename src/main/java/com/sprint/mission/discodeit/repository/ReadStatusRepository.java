package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.readstatus.ReadStatus;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface ReadStatusRepository {
    ReadStatus save(ReadStatus readStatus);

    ReadStatus findByUserId(UUID userId); //사용자 별 채널 읽기 상태

    List<ReadStatus> findByChannelId(UUID channelId); //채널에서 사용자별 읽기 상태

    Set<UUID> findUsersByChannelId(UUID channelId);

    Optional<ReadStatus> findByUserIdAndChannelId(UUID userId, UUID channelId);

    void delete(UUID id);
}
