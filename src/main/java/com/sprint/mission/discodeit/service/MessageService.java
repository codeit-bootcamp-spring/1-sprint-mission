package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.MessageSendRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.UUID;

public interface MessageService {
  void sendCommonMessage(MessageSendRequest messageSendRequest);
  
  void sendReplyMessage(MessageSendRequest messageSendRequest);
  
  MessageSendRequest findById(UUID id);
  
  List<Message> findBySender(UUID senderId);
  
  List<MessageSendRequest> findAllByChannelId(UUID channelId);
  
  void updateContent(MessageUpdateRequest messageUpdateRequest);
  
  void remove(UUID id);
}
