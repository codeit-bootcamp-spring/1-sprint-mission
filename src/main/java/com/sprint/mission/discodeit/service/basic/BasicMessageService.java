package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.MessageSendRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final BinaryContentRepository binaryContentRepository;

  @Override
  public void sendCommonMessage(MessageSendRequest messageSendRequest) {
    Message message = Message.ofCommon(messageSendRequest.channelId(),
        messageSendRequest.senderId(),
        messageSendRequest.binaryContentIds(),
        messageSendRequest.content());
    messageRepository.save(message);
    System.out.println("Message sent: " + message.getId());
  }

  @Override
  public void sendReplyMessage(MessageSendRequest messageSendRequest) {
    Message message = Message.ofReply(messageSendRequest.channelId(),
        messageSendRequest.senderId(),
        messageSendRequest.replyToId(),
        messageSendRequest.binaryContentIds(),
        messageSendRequest.content());
    messageRepository.save(message);
    System.out.println("Message sent: " + message.getId());

  }

  @Override
  public MessageSendRequest findById(UUID id) {
    Message message = messageRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("message not found: " + id));
    return new MessageSendRequest(
        message.getSenderId(),
        message.getChannelId(),
        message.getReplyToId(),
        message.getBinaryContentIds(),
        message.getContent());
  }

  @Override
  public List<Message> findBySender(UUID senderId) {
    return messageRepository.findBySender(senderId);
  }

  @Override
  public List<MessageSendRequest> findAllByChannelId(UUID channelId) {
    List<Message> messages = messageRepository.findAllByChannel(channelId);
    return messages.stream()
        .map(m -> new MessageSendRequest(
            m.getSenderId(),
            m.getChannelId(),
            m.getReplyToId(),
            m.getBinaryContentIds(),
            m.getContent()))
        .toList();
  }

  @Override
  public void updateContent(MessageUpdateRequest messageUpdateRequest) {
    Message message = messageRepository.findById(messageUpdateRequest.messageId())
        .orElseThrow(() -> new NoSuchElementException(
            "message not found: " + messageUpdateRequest.messageId()));
    message.updateContent(messageUpdateRequest.newContent());
    messageRepository.save(message);
  }

  @Override
  public void remove(UUID id) {
    Message message = messageRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("message not found with id: " + id));
    if (message.getBinaryContentIds() != null) {
      message.getBinaryContentIds().forEach(binaryContentRepository::remove);
    }
    messageRepository.remove(id);
  }
}
