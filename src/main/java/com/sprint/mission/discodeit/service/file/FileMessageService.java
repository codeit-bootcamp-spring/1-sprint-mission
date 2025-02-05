package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.*;

public class FileMessageService implements MessageService {
  Map<UUID, Message> messageMap = new HashMap<>();
  private MessageRepository messageRepository;
  
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
    Message newMessage = Message.ofCommon(channel, sender, content);
    messageMap.put(newMessage.getId(), newMessage);
    messageRepository.save(newMessage);
  }
  
  @Override
  public void sendReplyMessage(Channel channel, User sender, String content) {
    Message newMessage = Message.ofReply(channel, sender, content);
    messageMap.put(newMessage.getId(), newMessage);
    messageRepository.save(newMessage);
  }
  
  @Override
  public void updateMessage(UUID id, String newContent) {
    Message message = messageRepository.findMessageById(id)
        .orElseThrow(() -> new NoSuchElementException("message not found: " + id));
    message.updateContent(newContent);
    messageRepository.save(message);
  }
  
  @Override
  public void removeMessage(UUID id) {
    messageRepository.remove(id);
  }
}
