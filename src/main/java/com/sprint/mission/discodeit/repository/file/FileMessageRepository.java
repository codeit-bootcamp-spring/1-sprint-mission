package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.util.FileIO;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileMessageRepository implements MessageRepository {
  private final Path DIRECTORY = Paths.get("repository-data", "messages");
  private final String EXTENSION = ".ser";
  private final FileIO fileIO;
  private final UserRepository userRepository;
  
  private Path resolvePath(UUID id) {
    return DIRECTORY.resolve(id + EXTENSION);
  }
  
  private List<Message> loadAll() {
    return fileIO.loadAllFromDirectory(DIRECTORY, EXTENSION, Message.class);
  }
  
  @Override
  public void save(Message message) {
    File file = resolvePath(message.getId()).toFile();
    fileIO.saveToFile(file, message);
  }
  
  @Override
  public Optional<Message> findById(UUID id) {
    return loadAll().stream()
        .filter(m -> m.getId().equals(id))
        .findFirst();
  }
  
  @Override
  public List<Message> findBySender(UUID senderId) {
    User sender = userRepository.findById(senderId)
        .orElseThrow(() -> new NoSuchElementException("user not found with id: " + senderId));
    return loadAll().stream()
        .filter(m -> m.getSenderId().equals(senderId))
        .toList();
  }
  
  @Override
  public List<Message> findAllByChannel(UUID channelId) {
    return loadAll().stream()
        .filter(m -> m.getChannelId().equals(channelId))
        .toList();
  }
  
  @Override
  public void remove(UUID id) {
    File file = resolvePath(id).toFile();
    if (file.exists()) {
      file.delete();
    }
  }
  
}
