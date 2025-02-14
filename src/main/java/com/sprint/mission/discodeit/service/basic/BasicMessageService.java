package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.InvalidOperationException;
import com.sprint.mission.discodeit.exception.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.validator.EntityValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

import static com.sprint.mission.discodeit.constant.ErrorConstant.DEFAULT_ERROR_MESSAGE;

@Slf4j
@Service
@ConditionalOnProperty(name = "app.service.type", havingValue = "basic")
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;

  @Override
  public Message createMessage(Message message) {
    return messageRepository.create(message);
  }

  @Override
  public Message updateMessage(Message message, String content) {
    if (content != null && !content.isEmpty()) {
      message.setContent(content);
    }

    message.setIsEdited();
    return messageRepository.update(message);
  }

  @Override
  public Message getMessageById(String messageId) {
    return messageRepository.findById(messageId).orElseThrow(MessageNotFoundException::new);
  }

  @Override
  public List<Message> getMessagesByChannel(String channelId) {
    return messageRepository.findByChannel(channelId);
  }

  @Override
  public Message getLatestMessageByChannel(String channelId) {
    return messageRepository.findLatestChannelMessage(channelId).orElse(null);
  }

  @Override
  public Map<String, Instant> getLatestMessageForChannels(List<Channel> channels) {
    List<String> channelIds = channels.stream().map(Channel::getUUID).toList();
    return channelIds.stream()
        .collect(Collectors.toMap(
            id -> id,
            id -> messageRepository.findLatestChannelMessage(id)
                .map(Message::getCreatedAt)
                .orElse(Instant.EPOCH)
        ));
  }


  @Override
  public void deleteMessage(String messageId) {
    messageRepository.delete(messageId);
  }
}
