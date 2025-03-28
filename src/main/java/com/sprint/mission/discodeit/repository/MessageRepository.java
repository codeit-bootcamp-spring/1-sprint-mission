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

public interface MessageRepository extends JpaRepository<Message, UUID> {

  @Query("SELECT m FROM Message m "     // 메시지 조회
      + "LEFT JOIN FETCH m.author a "   // N+1 문제 방지 (null 허용)
      + "JOIN FETCH a.status "          // N+1 문제 방지 (null 비허용)
      + "LEFT JOIN FETCH a.profile "    // N+1 문제 방지 (null 허용)
      + "WHERE m.channel.id = :channelId AND m.createdAt < :createdAt")
    // Slice<Message>
    // 페이징된 데이터를 반환하지만, 전체 개수를 조회하지 않는 방식
    // count 쿼리를 실행하지 않고, 다음 페이지가 있는지만 확인
    // 무한 스크롤 방식에서 주로 사용
  Slice<Message> findAllByChannelIdWithAuthor(@Param("channelId") UUID channelId,
      @Param("createdAt") Instant createdAt, Pageable pageable);

  // 해당 채널 중 가장 최근 메시지의 생성일 검색
  @Query("SELECT m.createdAt "
      + "FROM Message m "
      + "WHERE m.channel.id = :channelId "
      + "ORDER BY m.createdAt DESC LIMIT 1")
  Optional<Instant> findLastMessageAtByChannelId(@Param("channelId") UUID channelId);

  // 해당 채널 메시지 모두 삭제
  void deleteAllByChannelId(UUID channelId);
}