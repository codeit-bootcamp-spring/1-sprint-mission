package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JCFMessageRepository implements MessageRepository {
  List<Message> data = new ArrayList<>();
  
  @Override
  public Optional<Message> findMessageById(UUID id) {
    return data.stream()
        .filter(m -> m.getId().equals(id))
        .findFirst();
  }
  
  @Override
  public List<Message> findMessagesBySender(UUID senderId) {
    return data.stream()
        .filter(m -> m.getSender().getId().equals(senderId))
        .toList();
  }
  
  @Override
  public List<Message> findAllMessages() {
    return data;
  }
  
  @Override
  public void save(Message message) {
    data.add(message);
  }
  
  @Override
  public void remove(UUID id) {
    findMessageById(id).ifPresent(data::remove);
  }
}
