package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class JCFMessageRepository implements MessageRepository {

  private static volatile JCFMessageRepository instance;

  private final Map<String, Message> data;

  private JCFMessageRepository() {
    this.data = new ConcurrentHashMap<>();
  }

  public static JCFMessageRepository getInstance() {
    if (instance == null) {
      synchronized (JCFMessageRepository.class) {
        if (instance == null) {
          instance = new JCFMessageRepository();
        }
      }
    }
    return instance;
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
