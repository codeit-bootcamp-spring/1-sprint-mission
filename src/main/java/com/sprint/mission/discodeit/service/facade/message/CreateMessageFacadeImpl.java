package com.sprint.mission.discodeit.service.facade.message;

import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.validator.EntityValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateMessageFacadeImpl implements CreateMessageFacade{


  private final MessageService messageService;
  private final MessageMapper messageMapper;
  private final BinaryContentMapper binaryContentMapper;
  private final BinaryContentService binaryContentService;
  private final ChannelService channelService;
  private final EntityValidator validator;
  @Override
  public MessageResponseDto createMessage(CreateMessageDto messageDto, String channelId) {
    User user = validator.findOrThrow(User.class, messageDto.getUserId(), new UserNotFoundException());
    Channel channel = validator.findOrThrow(Channel.class, channelId, new ChannelNotFoundException());

    channelService.validateUserAccess(channel, user.getUUID());

    Message message = messageMapper.toEntity(messageDto, channelId);
    messageService.createMessage(message);

    List<BinaryContent> binaryContents = Optional.ofNullable(messageDto.getMultipart())
        .filter(files -> !files.isEmpty())
        .map(files -> binaryContentMapper.fromMessageFiles(
            files, messageDto.getUserId(), channelId, message.getUUID()
        ))
        .orElse(Collections.emptyList());

    message.setBinaryContents(binaryContents);

    binaryContentService.saveBinaryContentsForMessage(message.getUUID(), binaryContents);

    // 이후 DB 생기면 삭제
    messageService.updateMessage(message, message.getContent());

    return messageMapper.toResponseDto(message);
  }
}
