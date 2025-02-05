package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;

public interface MessageService {
  MessageResponseDto createMessage(CreateMessageDto messageDto);

  Optional<Message> getMessageById(String messageId, Channel channel);

  List<MessageResponseDto> getMessagesByChannel(String channelId);

  MessageResponseDto updateMessage(MessageUpdateDto messageDto);

  void deleteMessage(String messageId);

}
