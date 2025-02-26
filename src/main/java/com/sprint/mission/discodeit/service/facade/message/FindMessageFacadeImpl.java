package com.sprint.mission.discodeit.service.facade.message;

import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FindMessageFacadeImpl implements FindMessageFacade{

  private final MessageService messageService;
  private final MessageMapper messageMapper;

  @Override
  public MessageResponseDto findMessageById(String id) {
    Message message = messageService.getMessageById(id);
    return messageMapper.toResponseDto(message);
  }

  @Override
  public List<MessageResponseDto> findMessagesByChannel(String channelId) {
    List<Message> channelMessages = messageService.getMessagesByChannel(channelId);
    return channelMessages.stream()
        .map(messageMapper::toResponseDto)
        .toList();
  }
}
