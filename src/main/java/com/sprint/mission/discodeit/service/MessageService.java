package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageDto;
import java.util.List;
import java.util.UUID;

public interface MessageService {
  MessageDto createMessage(MessageDto paramMessageDto);
  MessageDto findMessageById(UUID paramUUID);
  List<MessageDto> findMessagesByChannelId(UUID paramUUID);
  MessageDto updateMessage(UUID paramUUID, MessageDto paramMessageDto);
  void deleteMessage(UUID paramUUID);
}
