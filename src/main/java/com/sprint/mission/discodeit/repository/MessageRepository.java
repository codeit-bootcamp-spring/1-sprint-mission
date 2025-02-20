package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
  boolean save(Message message);

  Optional<Message> findById(UUID id);

  List<Message> findAll();

  boolean updateOne(UUID id, String content, UUID authorId);

  boolean deleteOne(UUID id);
}
