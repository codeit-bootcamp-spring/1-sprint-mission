package com.sprint.mission.discodeit.service.facade.message;

import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;

import java.util.List;

public interface MessageMasterFacade {
  MessageResponseDto createMessage(CreateMessageDto messageDto, String channelId);
  MessageResponseDto findMessageById(String id);
  List<MessageResponseDto> findMessagesByChannel(String channelId);
  MessageResponseDto updateMessage(String messageId, MessageUpdateDto messageDto);
  void deleteMessage(String messageId);
}
