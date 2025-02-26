package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@ConditionalOnProperty(name = "app.repository.type", havingValue = "jcf")
public class JCFMessageRepository implements MessageRepository{

  private final Map<String, Message> data = new ConcurrentHashMap<>();


  @Override
  public Message create(Message message) {
    data.put(message.getId(), message);
    return message;
  }

  @Override
  public Optional<Message> findById(String id) {
    return Optional.ofNullable(data.get(id));
  }

  @Override
  public List<Message> findByChannel(String channelId) {
    List<Message> messages = data.values().stream()
        .filter(m -> m.getChannelId().equals(channelId))
        .toList();
    return messages;
  }

  @Override
  public List<Message> findAll() {
    return new ArrayList<>(data.values());
  }

  @Override
  public Message update(Message message) {
    data.put(message.getId(), message);
    return message;
  }

  @Override
  public void delete(String id) {
    data.remove(id);
  }

  @Override
  public void deleteByChannel(String channelId) {
    data.values().removeIf(message -> message.getChannelId().equals(channelId));
  }

  @Override
  public Message findLatestChannelMessage(String channelId){
    return data.values().stream()
        .filter(m -> m.getChannelId().equals(channelId))
        .max(Comparator.comparing(Message::getCreatedAt))
        .orElse(null);
  }

  @Override
  public void clear() {
    data.clear();
  }
}
