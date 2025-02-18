package com.sprint.mission.discodeit.service.facade.message;

import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.validator.EntityValidator;
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
  private final BinaryContentMapper binaryContentMapper;
  private final BinaryContentService binaryContentService;
  private final ChannelService channelService;
  private final EntityValidator validator;
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
