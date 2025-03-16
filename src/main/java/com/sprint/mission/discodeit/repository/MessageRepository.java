package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  Message save(Message message);

  Optional<Message> findById(UUID id);

  List<Message> findByChannelId(UUID channelId);

  List<Message> findByUserId(UUID userId);

  boolean existsId(UUID id);

  void delete(UUID id);
}
