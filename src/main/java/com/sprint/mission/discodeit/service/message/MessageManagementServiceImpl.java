package com.sprint.mission.discodeit.service.message;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.MessageAttachment;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.channel.ChannelService;
import com.sprint.mission.discodeit.service.user.UserService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@Service
@RequiredArgsConstructor
public class MessageManagementServiceImpl implements MessageManagementService {

  private final UserService userService;
  private final ChannelService channelService;
  private final MessageService messageService;

  private final MessageMapper messageMapper;
  private final BinaryContentMapper binaryContentMapper;
  private final BinaryContentService binaryContentService;

  @Override
  @Transactional
  public Message createMessage(String content, String channelId, String authorId,
      List<MultipartFile> files) {

    Channel channel = channelService.findChannelById(channelId);
    log.debug("[FOUND CHANNEL] : [ID : {}]", channelId);
    User author = userService.findUserById(authorId);
    log.debug("[FOUND AUTHOR] : [ID : {}]", authorId);
    channelService.validateUserAccess(channel, author);
    log.debug("[USER VALIDATED FOR CHANNEL] : [CHANNEL_ID: {}] , [AUTHOR_ID : {}]", channelId,
        authorId);

    Message message = messageMapper.toEntity(content);
    message.addChannel(channel);
    message.addAuthor(author);

    if (files != null && !files.isEmpty()) {
      log.debug("[ATTACHMENTS FOUND] : [CHANNEL_ID : {}]", channelId);
      withFiles(files, message);
    }

    return messageService.createMessage(message);
  }

  @Override
  public Message findSingleMessage(String messageId) {
    return messageService.getMessageById(messageId);
  }

  @Override
  public Page<Message> findMessagesByChannel(String channelId, Instant cursor, Pageable pageable) {
    Page<Message> channelMessages =
        messageService.getMessagesByChannelWithCursor(channelId, cursor,
            pageable);

    List<UUID> authorIds = channelMessages.getContent().stream()
        .map(message -> message.getAuthor().getId())
        .distinct()
        .collect(Collectors.toList());

    List<User> users = userService.findByAllIn(authorIds);

    return channelMessages;
  }

  @Override
  @Transactional
  public Message updateMessage(String messageId, String newContent) {
    Message message = messageService.getMessageById(messageId);
    messageService.updateMessage(message, newContent);

    log.debug("[UPDATED MESSAGE] : [ID : {}]", messageId);

    return message;
  }

  @Override
  @Transactional
  public void deleteMessage(String messageId) {
    messageService.deleteMessage(messageId);
  }

  private void withFiles(List<MultipartFile> files, Message message) {
    List<BinaryContent> contents = binaryContentMapper.fromMessageFiles(files);

    binaryContentService.saveBinaryContents(contents, files);

    List<MessageAttachment> attachments = contents.stream()
        .map(content -> new MessageAttachment(message, content))
        .toList();

    message.getAttachments().addAll(attachments);

    log.debug("[ATTACHMENTS SAVED]");

  }
}
