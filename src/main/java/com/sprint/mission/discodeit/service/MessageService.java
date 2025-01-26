package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;

public interface MessageService {
  Message findMessageById(UUID id);
  
  List<Message> findMessagesBySender(UUID senderId);
  
  List<Message> findAllMessages();
  
  void sendCommonMessage(Channel channel, User sender, String content);
  
  void sendReplyMessage(Channel channel, User sender, String content);
  
  void updateMessage(UUID id, String newContent);
  
  void removeMessage(UUID id);
}
