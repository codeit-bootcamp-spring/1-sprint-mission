package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.entity.ChatChannel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.MessageValidationException;
import com.sprint.mission.discodeit.service.MessageServiceV2;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFMessageServiceV2 implements MessageServiceV2<ChatChannel> {

  private static volatile JCFMessageServiceV2 messageRepository;
  private UserService userService;
  private final Map<String, List<Message>> data;

  private JCFMessageServiceV2(UserService userService) {
    this.userService = userService;
    data = new HashMap<>();
  }

  public static JCFMessageServiceV2 getInstance(UserService userService) {
    if (messageRepository == null) {
      synchronized (JCFMessageServiceV2.class) {
        if (messageRepository == null) {
          messageRepository = new JCFMessageServiceV2(userService);
        }
      }
    }
    return messageRepository;
  }

  @Override
  public Message createMessage(String userId, Message message, ChatChannel chatChannel) throws MessageValidationException {
    if (!checkUserExists(userId)) throw new MessageValidationException();
    data.putIfAbsent(chatChannel.getUUID(), new ArrayList<>());
    data.get(chatChannel.getUUID()).add(message);
    return message;
  }

  private boolean checkUserExists(String userId) {
    return userService.readUserById(userId).isPresent();
  }

  @Override
  public Optional<Message> getMessageById(String messageId, ChatChannel chatChannel) {
    return data.get(chatChannel.getUUID())
        .stream()
        .filter(m -> m.getUUID().equals(messageId))
        .findFirst();
  }

  @Override
  public List<Message> getMessagesByChannel(ChatChannel chatChannel) {
    if (!data.containsKey(chatChannel.getUUID())) throw new IllegalArgumentException("존재하지 않는 채널입니다.");
    return Collections.unmodifiableList(new ArrayList<>(data.get(chatChannel.getUUID())));
  }

  @Override
  public Message updateMessage(ChatChannel chatChannel, String messageId, MessageUpdateDto updatedMessage) {
    return data.get(chatChannel.getUUID())
        .stream()
        .filter(m -> m.getUUID().equals(messageId))
        .findFirst()
        .map(m -> {
          updatedMessage.getContent().ifPresent(m::setContent);
          updatedMessage.getContentUrl().ifPresent(m::setContentImage);
          m.setIsEdited();
          return m;
        }).orElse(null);
  }

  @Override
  public boolean deleteMessage(String messageId, ChatChannel chatChannel) {
    List<Message> messages = data.get(chatChannel.getUUID());
    if (messages != null) {
      return messages.removeIf(m -> m.getUUID().equals(messageId));
    }
    return false;
  }
}

