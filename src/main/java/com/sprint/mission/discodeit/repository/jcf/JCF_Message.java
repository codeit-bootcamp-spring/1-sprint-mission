package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class JCF_Message implements MessageRepository {

  private final List<Message> messageList;

  public JCF_Message() {
    messageList = new ArrayList<>();
  }

  @Override
  public void creat(Message message) {
    messageList.add(message);
  }

  @Override
  public void delete(UUID messageId) {
    Optional<Message> getMessage = messageList.stream()
        .filter(message -> message.getId().equals(messageId)).findFirst();
    if (getMessage.isEmpty()) {
      throw new IllegalArgumentException("Message not found");
    } else {
      messageList.remove(getMessage.get());
    }
  }

  @Override
  public void update(UUID messageId, String updateMessage) {
    messageList.stream().filter(message -> message.getId().equals(messageId))
        .forEach(messageContent -> messageContent.updateMessage(updateMessage)
        );
  }

  @Override
  public Message findById(UUID messageId) {
    Optional<Message> getMessage = messageList.stream()
        .filter(message -> message.getId().equals(messageId))
        .map(Message::new)
        .findFirst();
    if (getMessage.isEmpty()) {
      throw new IllegalArgumentException("Message not found");
    } else {
      return getMessage.get();
    }

  }

  @Override
  public List<Message> findByAll() {
    return messageList.stream()
        .map(Message::new)
        .collect(Collectors.toList());
  }
}
