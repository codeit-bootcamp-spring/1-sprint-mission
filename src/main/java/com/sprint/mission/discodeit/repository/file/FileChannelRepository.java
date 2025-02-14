package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
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
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileChannelRepository implements ChannelRepository {
  private final Path DIRECTORY = Paths.get("repository-data", "channels");
  private final String EXTENSION = ".ser";
  private final FileIO fileIO;
  private final UserRepository userRepository;
  private final MessageRepository messageRepository;
  
  private Path resolvePath(UUID id) {
    return DIRECTORY.resolve(id + EXTENSION);
  }
  
  @Override
  public void save(Channel channel) {
    File file = resolvePath(channel.getId()).toFile();
    fileIO.saveToFile(file, channel);
  }
  
  @Override
  public Optional<Channel> findById(UUID id) {
    File file = resolvePath(id).toFile();
    return Optional.ofNullable(fileIO.loadFromFile(file, Channel.class));
  }
  
  @Override
  public Optional<Channel> findByName(String name) {
    return findAll().stream().filter(c -> c.getName().equals(name)).findFirst();
  }
  
  @Override
  public List<Channel> findAll() {
    return fileIO.loadAllFromDirectory(DIRECTORY, EXTENSION, Channel.class);
  }
  
  @Override
  public void remove(UUID id) {
    File file = resolvePath(id).toFile();
    if (file.exists()) {
      file.delete();
    }
  }
}
