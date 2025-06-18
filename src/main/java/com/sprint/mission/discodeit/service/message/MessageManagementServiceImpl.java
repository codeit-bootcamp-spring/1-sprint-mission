package com.sprint.mission.discodeit.service.message;

import com.sprint.mission.discodeit.async.binary_content.BinaryContentStorageWrapperService;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.MessageAttachment;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.UploadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.event.event_entity.BinaryContentUploadEvent;
import com.sprint.mission.discodeit.event.event_entity.NotificationEvent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.channel.ChannelService;
import com.sprint.mission.discodeit.service.user.UserService;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
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
  private final BinaryContentStorageWrapperService binaryContentStorageWrapperService;

  private final ReadStatusService readStatusService;
  private final ApplicationEventPublisher eventPublisher;

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
      withFiles(author, files, message);
    }

    Message sentMessage = messageService.createMessage(message);
    // TODO : 분리
    List<User> usersOfChannelNotificationOn = readStatusService.findAllByChannelId(channelId)
        .stream()
        .filter(status -> status.getUser().getId() != UUID.fromString(authorId))
        .filter(ReadStatus::isNotificationEnabled)
        .map(ReadStatus::getUser)
        .collect(Collectors.toList());

    NotificationEvent event = new NotificationEvent(
        usersOfChannelNotificationOn,
        NotificationType.NEW_MESSAGE,
        UUID.fromString(channelId),
        "새로운 메시지",
        message.getContent() != null
            ? message.getContent().substring(0, Math.min(10, message.getContent().length()))
            : ""
    );

    eventPublisher.publishEvent(event);
    return sentMessage;
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

  private void withFiles(User author, List<MultipartFile> files, Message message) {
    List<BinaryContent> contents = binaryContentMapper.fromMessageFiles(files);
    contents.forEach(content -> content.changeUploadStatus(UploadStatus.WAITING));

    List<BinaryContent> savedContents = binaryContentService.saveBinaryContents(contents, files);

    List<MessageAttachment> attachments = savedContents.stream()
        .map(content -> new MessageAttachment(message, content))
        .toList();

    message.getAttachments().addAll(attachments);

    log.debug("[ATTACHMENTS SAVED]");

    eventPublisher.publishEvent(new BinaryContentUploadEvent(savedContents, files, author));

  }
}
