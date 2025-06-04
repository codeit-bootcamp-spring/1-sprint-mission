package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

    @Query("SELECT m FROM Message m "
        + "LEFT JOIN FETCH m.author a "
        + "LEFT JOIN FETCH a.profile "
        + "WHERE m.channel.id=:channelId AND m.createdAt < :createdAt")
    Slice<Message> findAllByChannelIdWithAuthor(@Param("channelId") UUID channelId,
        @Param("createdAt") Instant createdAt,
        Pageable pageable);

    Optional<Message> findFirstByChannelIdOrderByCreatedAtDesc(UUID channelId);

    @Query("SELECT count(m) > 0 FROM Message m WHERE m.id = :id AND m.author.id = :authorId")
    boolean existsByIdAndAuthorId(@Param("id") UUID id, @Param("authorId") UUID authorId);
}
