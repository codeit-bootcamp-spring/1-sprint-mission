package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;

import java.util.*;

public class JCFMessageRepository implements MessageRepository {

  Map<UUID, Message> messages;

  public JCFMessageRepository() {
    this.messages = new HashMap<>();
  }

  @Override
  public Message saveMessage(Message message) {
    messages.put(message.getId(), message);
    return message;
  }

  @Override
  public Optional<Message> findMessageById(UUID id) {
    return Optional.ofNullable(messages.get(id));
  }

  @Override
  public Collection<Message> findAllMessages() {
    return messages.values();
  }

  @Override
  public List<Message> findMessagesByChannelId(UUID channelId) {
    return messages.values().stream()
        .filter(message -> message.getChannelId().equals(channelId))
        .toList();
  }

  @Override
  public void deleteMessageById(UUID id) {
    messages.remove(id);
  }
}
