package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;

public interface MessageService {
  Message createMessage(Message message);
  Message updateMessage(Message message, String content);

  Message getMessageById(String messageId);

  List<Message> getMessagesByChannel(String channelId);



  void deleteMessage(String messageId);

}
