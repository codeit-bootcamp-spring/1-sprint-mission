package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
public class JCFMessageRepository implements MessageRepository {
  List<Message> data = new ArrayList<>();
  
  @Override
  public void save(Message message) {
    data.add(message);
  }
  
  @Override
  public Optional<Message> findById(UUID id) {
    return data.stream()
        .filter(m -> m.getId().equals(id))
        .findFirst();
  }
  
  @Override
  public List<Message> findBySender(UUID senderId) {
    List<Message> messages = data.stream()
        .filter(m -> m.getSenderId().equals(senderId))
        .toList();
    return new ArrayList<>(messages);
  }
  
  @Override
  public List<Message> findAllByChannel(UUID channelId) {
    return data.stream()
        .filter(m -> m.getChannelId().equals(channelId))
        .toList();
  }
  
  @Override
  public void remove(UUID id) {
    findById(id).ifPresent(data::remove);
  }
}
