package com.spirnt.mission.discodeit.repository;

import com.spirnt.mission.discodeit.entity.Message;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {


    // 최신 메시지 size만큼 조회 (첫 페이지)
    @Query(
        "SELECT m from Message m LEFT JOIN FETCH m.author LEFT JOIN FETCH m.author.profile JOIN FETCH m.channel LEFT JOIN FETCH m.attachments "
            + "WHERE m.channel.id = :channelId")
    Slice<Message> findByChannelId(@Param("channelId") UUID channelId, Pageable pageable);

    // 커서 기반 메시지 size만큼 조회 (createdAt < cursor)
    @Query(
        "SELECT m from Message m LEFT JOIN FETCH m.author LEFT JOIN FETCH m.author.profile JOIN FETCH m.channel LEFT JOIN FETCH m.attachments "
            + "WHERE m.channel.id = :channelId "
            + "AND (m.createdAt < :cursor)")
    Slice<Message> findByChannelIdAndCreatedAtLessThan(@Param("channelId") UUID channelId,
        @Param("cursor") Instant cursor,
        Pageable pageable);

    List<Message> findAllByChannelId(UUID channelId);
}
