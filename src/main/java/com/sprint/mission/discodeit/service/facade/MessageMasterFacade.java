package com.sprint.mission.discodeit.service.facade;

import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;

import java.util.List;

public interface MessageMasterFacade {
  MessageResponseDto createMessage(CreateMessageDto messageDto);
  MessageResponseDto findMessageById(String id);
  List<MessageResponseDto> findMessagesByChannel(String channelId);
  MessageResponseDto updateMessage(MessageUpdateDto messageDto);
  void deleteMessage(String messageId);
}
