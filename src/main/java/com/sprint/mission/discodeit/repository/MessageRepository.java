package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  Page<Message> findAllByChannelId(UUID channelId, Pageable pageable);

  Optional<Message> findFirstByChannelIdOrderByCreatedAtDesc(UUID channelId); // 가장 최근 메시지 조회

  void deleteAllByChannelId(UUID channelId);
}
