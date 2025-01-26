package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

public class JCFMessageService implements MessageService {
  private final List<Message> data;

  public JCFMessageService() {
    this.data = new ArrayList<>();
  }

  @Override
  public void createMessage(Message message) {
    data.add(message);
  }

  @Override
  public Optional<Message> readMessage(UUID id) {
    return data.stream()
        .filter(message -> message.getId().equals(id))
        .findFirst();
  }

  @Override
  public List<Message> readAllMessages() {
    return new ArrayList<>(data);
  }

  @Override
  public void updateMessage(UUID id, String content, UUID authorId) {
    readMessage(id).ifPresent(message -> message.update(content, authorId));
  }

  @Override
  public void deleteMessage(UUID id) {
    data.removeIf(message -> message.getId().equals(id));
  }
}