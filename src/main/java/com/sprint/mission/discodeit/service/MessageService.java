package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.SendMessageDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageDto;
import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.UUID;

public interface MessageService {
  void sendCommonMessage(SendMessageDto sendMessageDto);
  
  void sendReplyMessage(SendMessageDto sendMessageDto);
  
  Message findById(UUID id);
  
  List<Message> findBySender(UUID senderId);
  
  List<Message> findAllByChannelId(UUID channelId);
  
  void updateContent(UpdateMessageDto updateMessageDto);
  
  void remove(UUID id);
}
