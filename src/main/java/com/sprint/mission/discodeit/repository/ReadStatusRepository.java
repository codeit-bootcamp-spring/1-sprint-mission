package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

    // 특정 유저가 구독 중인 채널 ID 조회
    @Query("select rs.user.id FROM ReadStatus rs where rs.channel.id=:channelId")
    List<UUID> findChannelIdsByUserId(@Param("channelId") UUID channelId);

    // 특정 채널의 모든 유저 ID 조회
    @Query("SELECT rs.user.id FROM ReadStatus rs WHERE rs.channel.id = :channelId")
    List<UUID> findUserIdsByChannelId(@Param("channelId") UUID channelId);

    void deleteByChannelId(UUID id);

    boolean existsByUserIdAndChannelId(UUID userId, UUID channelId);

    List<ReadStatus> findAllByUserId(UUID userId);

    List<ReadStatus> findAllByChannelId(UUID channelId);
}
