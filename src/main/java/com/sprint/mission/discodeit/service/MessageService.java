package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {
  void createMessage(Message message);

  Optional<Message> readMessage(UUID id);

  List<Message> readAllMessages();

  void updateMessage(UUID id, String content, UUID authorId);

  void deleteMessage(UUID id);
}