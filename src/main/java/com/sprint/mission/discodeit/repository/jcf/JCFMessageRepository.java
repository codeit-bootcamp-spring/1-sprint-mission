package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;

public class JCFMessageRepository implements MessageRepository {

  private final Map<String, Message> data;


  public JCFMessageRepository() {
    this.data = new HashMap<>();
  }

  @Override
  public Message create(Message message) {
    data.put(message.getUUID(), message);
    return message;
  }

  @Override
  public Optional<Message> findById(String id) {
    return Optional.ofNullable(data.get(id));
  }

  @Override
  public List<Message> findByChannel(String channelId) {
    List<Message> messages = data.values().stream()
        .filter(m -> m.getChannelUUID().equals(channelId))
        .toList();
    return messages;
  }

  @Override
  public List<Message> findAll() {
    return new ArrayList<>(data.values());
  }

  @Override
  public Message update(Message message) {
    data.put(message.getUUID(), message);
    return message;
  }

  @Override
  public void delete(String id) {
    data.remove(id);
  }

  @Override
  public void clear() {
    data.clear();
  }
}
