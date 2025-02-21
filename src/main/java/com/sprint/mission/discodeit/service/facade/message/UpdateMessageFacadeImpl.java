package com.sprint.mission.discodeit.service.facade.message;

import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.MessageNotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.validator.EntityValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateMessageFacadeImpl implements UpdateMessageFacade{

  private final MessageService messageService;
  private final MessageMapper messageMapper;
  private final BinaryContentService binaryContentService;
  private final EntityValidator validator;
  private final BinaryContentMapper binaryContentMapper;


  @Override
  public MessageResponseDto updateMessage(String messageId, MessageUpdateDto messageDto) {

    Message message = validator.findOrThrow(Message.class, messageId, new MessageNotFoundException());

    messageService.updateMessage(message, messageDto.newContent());

    return messageMapper.toResponseDto(message);
  }
}
