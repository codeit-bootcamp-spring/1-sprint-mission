package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.SendMessageDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
  @Qualifier("file")
  private final MessageRepository messageRepository;
  @Qualifier("file")
  private final BinaryContentRepository binaryContentRepository;
  
  @Override
  public void sendCommonMessage(SendMessageDto sendMessageDto) {
    Message message = Message.ofCommon(sendMessageDto.channelId(),
        sendMessageDto.senderId(),
        sendMessageDto.binaryContent(),
        sendMessageDto.content());
    messageRepository.save(message);
  }
  
  @Override
  public void sendReplyMessage(SendMessageDto sendMessageDto) {
    Message message = Message.ofReply(sendMessageDto.channelId(),
        sendMessageDto.senderId(),
        sendMessageDto.replyToId(),
        sendMessageDto.binaryContent(),
        sendMessageDto.content());
    messageRepository.save(message);
  }
  
  @Override
  public Message findById(UUID id) {
    return messageRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("message not found: " + id));
  }
  
  @Override
  public List<Message> findBySender(UUID senderId) {
    return messageRepository.findBySender(senderId);
  }
  
  @Override
  public List<Message> findAllByChannelId(UUID channelId) {
    return messageRepository.findAllByChannel(channelId);
  }
  
  @Override
  public void updateContent(UpdateMessageDto updateMessageDto) {
    Message message = messageRepository.findById(updateMessageDto.messageId())
        .orElseThrow(() -> new NoSuchElementException("message not found: " + updateMessageDto.messageId()));
    message.updateContent(updateMessageDto.newContent());
    messageRepository.save(message);
  }
  
  @Override
  public void remove(UUID id) {
    Message message = messageRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("message not found with id: " + id));
    if (message.getBinaryContent() != null) {
      message.getBinaryContent().forEach(b -> binaryContentRepository.remove(b.getId()));
    }
    messageRepository.remove(id);
  }
}
