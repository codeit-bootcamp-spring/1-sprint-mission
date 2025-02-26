package com.sprint.mission.discodeit.service.facade.message;

import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MessageMasterFacadeImpl implements MessageMasterFacade {


  private final CreateMessageFacade createMessageFacade;
  private final FindMessageFacade findMessageFacade;
  private final UpdateMessageFacade updateMessageFacade;
  private final DeleteMessageFacade deleteMessageFacade;

  @Override
  public MessageResponseDto createMessage(CreateMessageDto messageDto, String channelId) {
    return createMessageFacade.createMessage(messageDto, channelId);
  }

  @Override
  public MessageResponseDto findMessageById(String id) {
    return findMessageFacade.findMessageById(id);
  }

  @Override
  public List<MessageResponseDto> findMessagesByChannel(String channelId) {
    return findMessageFacade.findMessagesByChannel(channelId);
  }

  @Override
  public MessageResponseDto updateMessage(String messageId, MessageUpdateDto messageDto) {
    return updateMessageFacade.updateMessage(messageId, messageDto);
  }

  @Override
  public void deleteMessage(String messageId) {
    deleteMessageFacade.deleteMessage(messageId);
  }

}
