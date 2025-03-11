package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  boolean existsById(UUID id);


  Page<Message> findByChannelId(UUID channelId, Pageable pageable);

  @Query("SELECT COUNT(m) FROM Message m WHERE m.channel.id = :channelId")
  Long countByChannelId(@Param("channelId") UUID channelId);

  @EntityGraph(attributePaths = {"author", "author.profile.id", "attachmentIds"})
  @Query("SELECT m FROM Message m "
      + "WHERE m.channel.id = :channelId ORDER BY m.createdAt DESC")
  Page<Message> findFirstMessages(@Param("channelId") UUID channelId, Pageable pageable);

  @EntityGraph(attributePaths = {"author", "author.profile.id", "attachmentIds"})
  @Query("""
          SELECT m FROM Message m
          WHERE m.channel.id = :channelId
          AND m.createdAt < :cursor
          ORDER BY m.createdAt DESC
      """)
  Page<Message> findNextMessages(@Param("channelId") UUID channelId,
      @Param("cursor") Instant cursor, Pageable pageable);


  void deleteByChannelId(UUID id);
}
