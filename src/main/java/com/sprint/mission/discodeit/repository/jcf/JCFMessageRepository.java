package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import java.util.*;

public class JCFMessageRepository implements MessageRepository {
  private final Map<UUID, Message> data;
  public JCFMessageRepository() {
    this.data = new HashMap<>();
  }

  @Override
  public Message save(Message message){
    this.data.put(message.getId(), message);
    return message;
  }

  @Override
  public Optional<Message> findById(UUID messageId){
    return Optional.ofNullable(this.data.get(messageId));
  }

  // TODO : 채널과의 관계 공부 (채널 딜리트)
  @Override
  public List<Message> findAllByChannelId(UUID channelId){
    return this.data.values().stream().filter(message -> message.getChannelId().equals(channelId)).toList();
  }
  @Override
  public boolean existsById(UUID messageId) {
    return this.data.containsKey(messageId);
  }

  @Override
  public void deleteById(UUID messageId) {
    this.data.remove(messageId);
  }

  @Override
  public void deleteAllByChannelId(UUID channelId) {
    this.findAllByChannelId(channelId)
            .forEach(message -> this.deleteById(message.getId()));
  }
}
