package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class JCFMessageService implements MessageService {
  private final MessageRepository messageRepository;
  
  public JCFMessageService() {
    this.messageRepository = new JCFMessageRepository();
  }
  
  @Override
  public Message findMessageById(UUID id) {
    return messageRepository.findMessageById(id)
        .orElseThrow(() -> new NoSuchElementException("message not found: " + id));
  }
  
  @Override
  public List<Message> findMessagesBySender(UUID senderId) {
    return messageRepository.findMessagesBySender(senderId);
  }
  
  @Override
  public List<Message> findAllMessages() {
    return messageRepository.findAllMessages();
  }
  
  @Override
  public void sendCommonMessage(Channel channel, User sender, String content) {
    Message message = Message.ofCommon(channel, sender, content);
    messageRepository.save(message);
  }
  
  @Override
  public void sendReplyMessage(Channel channel, User sender, String content) {
    Message message = Message.ofReply(channel, sender, content);
    messageRepository.save(message);
  }
  
  @Override
  public void updateMessage(UUID id, String newContent) {
    messageRepository.findMessageById(id).ifPresentOrElse(m -> m.updateContent(newContent), () -> {
      throw new IllegalArgumentException("message not found: " + id);
    });
  }
  
  @Override
  public void removeMessage(UUID id) {
    messageRepository.remove(id);
  }
}
