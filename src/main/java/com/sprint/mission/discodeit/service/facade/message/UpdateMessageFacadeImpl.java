package com.sprint.mission.discodeit.service.facade.message;

import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.InvalidOperationException;
import com.sprint.mission.discodeit.exception.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.validator.EntityValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

import static com.sprint.mission.discodeit.constant.ErrorConstant.DEFAULT_ERROR_MESSAGE;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateMessageFacadeImpl implements UpdateMessageFacade{

  private final MessageService messageService;
  private final MessageMapper messageMapper;
  private final BinaryContentService binaryContentService;
  private final EntityValidator validator;


  @Override
  public MessageResponseDto updateMessage(String messageId, MessageUpdateDto messageDto) {

    User user = validator.findOrThrow(User.class, messageDto.getUserId(), new UserNotFoundException());
    Message message = validator.findOrThrow(Message.class, messageId, new MessageNotFoundException());

    if (!message.getUserId().equals(user.getUUID())) {
      throw new InvalidOperationException(DEFAULT_ERROR_MESSAGE);
    }

    List<MultipartFile> incomingFiles = messageDto.getBinaryContent();

    List<BinaryContent> savedFiles = incomingFiles == null
        || incomingFiles.isEmpty()
        ? Collections.emptyList()
        : binaryContentService.updateBinaryContentForMessage(message, user.getUUID(), incomingFiles);

    message.addBinaryContents(savedFiles);

    messageService.updateMessage(message, messageDto.getContent());

    return messageMapper.toResponseDto(message);
  }
}
