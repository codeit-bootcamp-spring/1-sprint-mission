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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
  public MessageResponseDto createMessage(CreateMessageDto messageDto, List<MultipartFile> files) {

    User user = validator.findOrThrow(User.class, messageDto.authorId(), new UserNotFoundException());
    Channel channel = validator.findOrThrow(Channel.class, messageDto.channelId(), new ChannelNotFoundException());

    channelService.validateUserAccess(channel, user.getUUID());
    Message message = messageMapper.toEntity(messageDto, messageDto.channelId(), binaryContentMapper);

    List<BinaryContent> contents = binaryContentMapper.fromMessageFiles(files, user.getUUID(), channel.getUUID(), message.getUUID());
    List<String> ids = contents.stream().map(BinaryContent::getUUID).toList();
    message.addBinaryContents(ids);

    messageService.createMessage(message);
    binaryContentService.saveBinaryContentsForMessage(message.getUUID(), contents);
    return messageMapper.toResponseDto(message);
  }
}
