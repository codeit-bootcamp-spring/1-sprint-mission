package com.sprint.mission.discodeit.service.facade;

import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.InvalidOperationException;
import com.sprint.mission.discodeit.exception.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.facade.message.CreateMessageFacade;
import com.sprint.mission.discodeit.service.facade.message.DeleteMessageFacade;
import com.sprint.mission.discodeit.service.facade.message.FindMessageFacade;
import com.sprint.mission.discodeit.service.facade.message.UpdateMessageFacade;
import com.sprint.mission.discodeit.validator.EntityValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.sprint.mission.discodeit.constant.ErrorConstant.DEFAULT_ERROR_MESSAGE;

@Component
@RequiredArgsConstructor
public class BasicMessageFacade implements MessageMasterFacade {

  private final MessageService messageService;
  private final MessageMapper messageMapper;
  private final BinaryContentMapper binaryContentMapper;
  private final BinaryContentService binaryContentService;
  private final ChannelService channelService;
  private final EntityValidator validator;

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
