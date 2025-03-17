package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  @EntityGraph(attributePaths = {"author", "author.status", "attachments",
      "attachments.attachment"})
  Slice<Message> findByChannel_IdOrderByCreatedAtDesc(UUID channelId, Pageable pageable);

  @Query(value = "SELECT m FROM Message m " +
      "LEFT JOIN FETCH m.author a " +
      "LEFT JOIN FETCH a.status " +
      "WHERE m.channel.id = :channelId " +
      "ORDER BY m.createdAt DESC",
      countQuery = "SELECT COUNT(m) FROM Message m WHERE m.channel.id = :channelId")
  Slice<Message> findByChannelIdPaged(@Param("channelId") UUID channelId, Pageable pageable);

  /**
   * 커서 기반 페이징 - 특정 시간 이전의 메시지를 조회 cursor가 null이면 최신 메시지부터 조회
   */
  @Query(value = "SELECT m FROM Message m " +
      "LEFT JOIN FETCH m.author a " +
      "LEFT JOIN FETCH a.status " +
      "WHERE m.channel.id = :channelId " +
      "AND (:cursorIsNull = true OR m.createdAt < :cursor) " +
      "ORDER BY m.createdAt DESC")
  List<Message> findByChannelIdAndCursorPaged(
      @Param("channelId") UUID channelId,
      @Param("cursor") Instant cursor,
      @Param("cursorIsNull") boolean cursorIsNull,
      Pageable pageable);

  /**
   * 커서 다음에 데이터가 더 있는지 확인
   */
  @Query(value = "SELECT COUNT(m) > 0 FROM Message m " +
      "WHERE m.channel.id = :channelId " +
      "AND m.createdAt < :lastCreatedAt")
  boolean existsByChannelIdAndCreatedAtLessThan(
      @Param("channelId") UUID channelId,
      @Param("lastCreatedAt") Instant lastCreatedAt);

  /**
   * ID 목록으로 메시지와 모든 관련 데이터를 한번에 조회
   */
  @Query("SELECT DISTINCT m FROM Message m " +
      "LEFT JOIN FETCH m.attachments atts " +
      "LEFT JOIN FETCH atts.attachment " +
      "LEFT JOIN FETCH m.author au " +
      "LEFT JOIN FETCH au.status " +
      "WHERE m.id IN :messageIds " +
      "ORDER BY m.createdAt DESC")
  List<Message> findMessagesWithAllRelationships(@Param("messageIds") List<UUID> messageIds);


  @Query("SELECT DISTINCT m FROM Message m " +
      "LEFT JOIN FETCH m.author a " +
      "LEFT JOIN FETCH a.profile " +
      "LEFT JOIN FETCH a.status " +
      "LEFT JOIN FETCH m.channel " +
      "LEFT JOIN FETCH m.attachments ma " +
      "LEFT JOIN FETCH ma.attachment " +
      "WHERE m.id = :messageId")
  Optional<Message> findWithAllRelationships(@Param("messageId") UUID messageId);
}
