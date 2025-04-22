package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {
    @Query("select max(m.createdAt) from Message m where m.channel.id = :channelId")
    Instant findLastMessageAtByChannelId(@Param("channelId") UUID channelId);

    @Query("select m from Message m where m.channel.id = :channelId order by m.createdAt desc")
    Slice<Message> findMessagesFirstPage(@Param("channelId") UUID channelId, Pageable pageable);

    @Query("select m from Message m where m.channel.id = :channelId and m.id < :cursor order by m.createdAt desc")
    Slice<Message> findMessagesNextPage(@Param("channelId") UUID channelId, @Param("cursor") UUID cursor, Pageable pageable);
}
