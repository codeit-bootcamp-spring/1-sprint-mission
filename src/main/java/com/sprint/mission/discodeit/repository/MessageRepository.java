package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {

  Message save(Message message);

  List<Message> getAllMessages();

  Optional<Message> getMessageById(UUID uuid);

  void deleteById(UUID uuid);
}
