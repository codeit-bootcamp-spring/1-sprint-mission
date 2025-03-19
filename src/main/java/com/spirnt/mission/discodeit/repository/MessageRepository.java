package com.spirnt.mission.discodeit.repository;

import com.spirnt.mission.discodeit.enity.Message;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {

  Message save(Message message);

  void delete(Message message);

  void deleteById(UUID messageId);

  Optional<Message> findById(UUID messageId);

  // 최신 메시지 size만큼 조회 (첫 페이지)
  Page<Message> findByChannelId(UUID channelId, Pageable pageable);

  // 커서 기반 메시지 size만큼 조회 (createdAt < cursor)
  Page<Message> findByChannelIdAndCreatedAtLessThan(UUID channelId,
      Instant cursor,
      Pageable pageable);


  List<Message> findAllByChannelId(UUID channelId);

  // 존재 검증
  boolean existsById(UUID messageId);

  @Transactional
  @Modifying
  @Query("UPDATE Message m SET m.content = :newContent WHERE m.id = :id")
  int updateById(@Param("id") UUID id, @Param("newContent") String newContent);
}
