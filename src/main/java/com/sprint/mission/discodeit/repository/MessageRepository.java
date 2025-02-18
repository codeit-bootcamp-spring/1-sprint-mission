package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
  Message save(Message paramMessage);

  Optional<Message> findById(UUID paramUUID);

  List<Message> findAll();

  List<Message> findAllByChannelId(UUID paramUUID);

  void deleteById(UUID paramUUID);
}
