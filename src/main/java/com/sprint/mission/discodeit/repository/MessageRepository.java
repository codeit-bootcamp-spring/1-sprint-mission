package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    @Query("SELECT m FROM Message m "
        + "LEFT JOIN FETCH m.author a "
        + "LEFT JOIN FETCH a.profile "
        + "WHERE m.channel.id=:channelId AND m.createdAt < :createdAt")
    Slice<Message> findAllByChannelIdWithAuthor(@Param("channelId") UUID channelId,
        @Param("createdAt") Instant createdAt,
        Pageable pageable);

    List<Message> findByChannelId(UUID channelId);

    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END " +
        "FROM Message m WHERE m.id = :messageId AND m.author.id = :userId")
    boolean isAuthor(@Param("postId") UUID messageId, @Param("userId") UUID userId);
}
