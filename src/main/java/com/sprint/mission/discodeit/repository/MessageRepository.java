package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  Message save(Message message);

  @EntityGraph(attributePaths = {"user", "channel"})
  Optional<Message> findById(UUID messageId);

  @EntityGraph(attributePaths = {"user", "channel"})
  Page<Message> findAllByChannelId(UUID channelId,
      Pageable pageable); // 채널 ID가 있어야 채널에 있는 메세지를 삭제할 수 있음

  boolean existsById(UUID messageId);

  void deleteById(UUID messageId);

  void deleteAllByChannelId(UUID channelId);
}
