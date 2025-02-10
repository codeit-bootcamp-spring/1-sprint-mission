package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;

import java.util.List;

public interface MessageService {
  MessageResponseDto createMessage(CreateMessageDto messageDto);

  MessageResponseDto getMessageById(String messageId);

  List<MessageResponseDto> getMessagesByChannel(String channelId);

  MessageResponseDto updateMessage(MessageUpdateDto messageDto);

  void deleteMessage(String messageId);

}
