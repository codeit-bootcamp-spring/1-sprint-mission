package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  List<Message> findByChannelId(String channelId);

  Optional<Message> findByAuthorId(String authorId);

  List<Message> findByContentContains(String content);

  //가장 최근 메세지 1개 조회
  @Query("SELECT m FROM Message m WHERE m.channel.id = :channelId ORDER BY m.createdAt DESC")
  Optional<Message> findLatestMessageByChannelId(@Param("channelId") UUID channelId);

  //채널 ID에 해당하는 메시지를 페이징하여 조회. 생성일시 기준 내림차순(최신순)으로 정렬.
  @Query("SELECT m FROM Message m WHERE m.channel.id = :channelId ORDER BY m.createdAt DESC")
  Slice<Message> findByChannelIdOrderByCreatedAtDesc(
      @Param("channelId") UUID channelId,
      Pageable pageable
  );

  @Query(value = "SELECT DISTINCT m FROM Message m " +
      "JOIN FETCH m.author a " +
      "LEFT JOIN FETCH a.userStatus " +
      "WHERE m.channel.id = :channelId " +
      "ORDER BY m.createdAt DESC",
      countQuery = "SELECT COUNT(m) FROM Message m WHERE m.channel.id = :channelId")
  @QueryHints()
  Page<Message> findByChannelIdWithAuthorPaged(@Param("channelId") UUID channelId,
      Pageable pageable);

  @Query("SELECT m FROM Message m " +
      "JOIN FETCH m.author a " +
      "LEFT JOIN FETCH a.userStatus " +
      "WHERE m.channel.id = :channelId " +
      "ORDER BY m.createdAt DESC")
  List<Message> findByChannelIdWithAuthor(@Param("channelId") UUID channelId);
}
