package com.sprint.mission.discodeit.repository.jpa;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  List<Message> findAllByChannel(Channel channel);

  @EntityGraph(attributePaths = {"channel", "author"})
  Page<Message> findAllByChannel_Id(UUID channelId, Pageable pageable);

  @EntityGraph(attributePaths = {"channel", "author"})
  Page<Message> findAllByChannel_IdAndCreatedAtBefore(UUID channelId, Instant cursor,
      Pageable pageable);

  //테스트를위해서 직접 값을 업데이트 - 이렇게 테스트를 위해서 사용하는게 맞을까?
  @Modifying
  @Query("update Message m set m.createdAt = :createdAt where m.id = :id")
  void forceUpdateCreatedAt(@Param("id") UUID id, @Param("createdAt") Instant createdAt);
}
