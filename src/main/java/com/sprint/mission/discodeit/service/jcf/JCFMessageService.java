/*
package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChatChannel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.MessageValidationException;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFMessageService implements MessageService {

  private static volatile JCFMessageService messageRepository;
  private UserService userService;
  private ChannelService channelService;
  private final Map<String, List<Message>> data;

  private JCFMessageService(UserService userService, ChannelService channelService) {
    this.userService = userService;
    this.channelService = channelService;
    data = new HashMap<>();
  }

  public static JCFMessageService getInstance(UserService userService, ChannelService channelService) {
    if (messageRepository == null) {
      synchronized (JCFMessageService.class) {
        if (messageRepository == null) {
          messageRepository = new JCFMessageService(userService, channelService);
        }
      }
    }
    return messageRepository;
  }

  @Override
  public MessageResponseDto createMessage(CreateMessageDto messageDto) {
    if (!checkUserExists(userId)) throw new MessageValidationException();
    if (!checkChannelExists(chatChannel)) throw new MessageValidationException();
    data.putIfAbsent(chatChannel.getId(), new ArrayList<>());
    data.get(chatChannel.getId()).add(message);
    return message;
  }

  private boolean checkChannelExists(Channel chatChannel){
    return channelService.getChannelById(chatChannel.getId()) != null;
  }

  private boolean checkUserExists(String userId) {
    return userService.findUserById(userId).isPresent();
  }

  @Override
  public Optional<Message> getMessageById(String messageId, ChatChannel chatChannel) {
    return data.get(chatChannel.getId())
        .stream()
        .filter(m -> m.getId().equals(messageId))
        .findFirst();
  }

  @Override
  public List<Message> getMessagesByChannel(ChatChannel chatChannel) {
    if (!data.containsKey(chatChannel.getId())) throw new IllegalArgumentException("존재하지 않는 채널입니다.");
    return Collections.unmodifiableList(new ArrayList<>(data.get(chatChannel.getId())));
  }

  @Override
  public Message updateMessage(ChatChannel chatChannel, String messageId, MessageUpdateDto updatedMessage) {
    return data.get(chatChannel.getId())
        .stream()
        .filter(m -> m.getId().equals(messageId))
        .findFirst()
        .map(m -> {
          synchronized (m) {
            updatedMessage.getContent().ifPresent(m::setContent);
            updatedMessage.getContentUrl().ifPresent(m::setContentImage);
            m.setIsEdited();
          }
          return m;
        }).orElse(null);
  }

  @Override
  public boolean deleteMessage(String messageId, ChatChannel chatChannel) {
    List<Message> messages = data.get(chatChannel.getId());
    if (messages != null) {
      return messages.removeIf(m -> m.getId().equals(messageId));
    }
    return false;
  }
}

*/
