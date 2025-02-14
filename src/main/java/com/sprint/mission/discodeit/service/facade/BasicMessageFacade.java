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
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.validator.EntityValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.sprint.mission.discodeit.constant.ErrorConstant.DEFAULT_ERROR_MESSAGE;

@Component
@RequiredArgsConstructor
public class BasicMessageFacade implements MessageFacade {

  private final MessageService messageService;
  private final MessageMapper messageMapper;
  private final BinaryContentService binaryContentService;
  private final EntityValidator validator;

  @Override
  public MessageResponseDto createMessage(CreateMessageDto messageDto) {

    User user = validator.findOrThrow(User.class, messageDto.getUserId(), new UserNotFoundException());

    Channel channel = validator.findOrThrow(Channel.class, messageDto.getChannelId(), new ChannelNotFoundException());

    checkIfUserBelongsToPrivateChannel(channel, messageDto);

    Message message = messageService.createMessage(messageMapper.toEntity(messageDto));

    List<BinaryContent> binaryContents = binaryContentService.saveBinaryContentsForMessage(message);

    // TODO : base64Encoding 중복임
    return MessageResponseDto.from(message);
  }

  @Override
  public MessageResponseDto findMessageById(String id) {
    Message message = messageService.getMessageById(id);
    return MessageResponseDto.from(message);
  }

  @Override
  public List<MessageResponseDto> findMessagesByChannel(String channelId) {
    List<Message> channelMessages = messageService.getMessagesByChannel(channelId);
    return channelMessages.stream()
        .map(MessageResponseDto::from).toList();
  }

  @Override
  public MessageResponseDto updateMessage(MessageUpdateDto messageDto) {

    User user = validator.findOrThrow(User.class, messageDto.getUserId(), new UserNotFoundException());
    Message message = validator.findOrThrow(Message.class, messageDto.getMessageId(), new MessageNotFoundException());

    if (!message.getUserUUID().equals(user.getUUID())) {
      throw new InvalidOperationException(DEFAULT_ERROR_MESSAGE);
    }

    List<MultipartFile> incomingFiles = messageDto.getBinaryContent();

    List<BinaryContent> savedFiles = binaryContentService.updateBinaryContentForMessage(message, user.getUUID(), incomingFiles);

    message.setBinaryContents(savedFiles);

    messageService.updateMessage(message, messageDto.getContent());

    return MessageResponseDto.from(message);
  }

  @Override
  public void deleteMessage(String messageId) {
    binaryContentService.deleteByMessageId(messageId);
    messageService.deleteMessage(messageId);
  }

  private void checkIfUserBelongsToPrivateChannel(Channel channel, CreateMessageDto messageDto) {
    if (channel.getIsPrivate()) {
      channel.getParticipatingUsers().stream().filter(id -> id.equals(messageDto.getUserId())).findAny().orElseThrow(
          () -> new InvalidOperationException(DEFAULT_ERROR_MESSAGE)
      );
    }
  }
}
