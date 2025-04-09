package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, UUID> {


  //channelId로 message삭제
  void deleteByChannelId(UUID channelId);

  //채널에 해당하는 메시지 리스트 반환
  List<Message> findByChannelId(UUID channelId);


  @Query("SELECT m FROM Message m "
      + "LEFT JOIN FETCH m.author a "
      + "JOIN FETCH a.status "
      + "LEFT JOIN FETCH a.profile "
      + "WHERE m.channel.id=:channelId AND m.createdAt < :createdAt")
  Slice<Message> findAllByChannelIdWithAuthor(@Param("channelId") UUID channelId,
      @Param("createdAt") Instant createdAt,
      Pageable pageable);

}
