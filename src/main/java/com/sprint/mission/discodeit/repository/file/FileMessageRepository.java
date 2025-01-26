package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.util.FileIO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.*;

public class FileMessageRepository implements MessageRepository {
  private final static File file = new File("resources/messages.ser");
  private UserRepository userRepository;
  private FileIO fileIO;
  private List<Message> messages = new ArrayList<>();
  
  private void loadAllMessages() {
    fileIO.initFile(file);
    List<Serializable> serializables = fileIO.loadFile(file);
    messages = serializables.stream()
        .filter(Message.class::isInstance)
        .map(Message.class::cast)
        .toList();
  }
  
  @Override
  public Optional<Message> findMessageById(UUID id) {
    return messages.stream().filter(m -> m.getId().equals(id)).findFirst();
  }
  
  @Override
  public List<Message> findMessagesBySender(UUID senderId) {
    User sender = userRepository.findUserById(senderId)
        .orElseThrow(() -> new NoSuchElementException("user not found: " + senderId));
    return messages.stream().filter(m -> m.getSender().equals(sender)).toList();
  }
  
  @Override
  public List<Message> findAllMessages() {
    return messages.stream().toList();
  }
  
  @Override
  public void save(Message message) {
    try {
      fileIO.saveToFile(file, message);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
  
  @Override
  public void remove(UUID id) {
    Message message = findMessageById(id).orElse(null);
    fileIO.removeObjectFromFile(file, message);
  }
  
}
