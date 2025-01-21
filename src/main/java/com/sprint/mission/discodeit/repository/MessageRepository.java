package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;

public interface MessageRepository {
  Message createMessage(Message message);
  Optional<Message> getMessageById(String id);
  List<Message> getMessagesByChannel(String channelId);
  List<Message> getAllMessages();
  Message updateMessage(Message message);
  void deleteMessage(String id);
}
