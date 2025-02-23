package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
  void save(Message message);
  
  Optional<Message> findById(UUID id);
  
  List<Message> findBySender(UUID senderId);
  
  List<Message> findAllByChannel(UUID channelId);
  
  void remove(UUID id);
}
