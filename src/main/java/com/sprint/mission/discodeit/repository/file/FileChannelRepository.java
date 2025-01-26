package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.util.FileIO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileChannelRepository implements ChannelRepository {
  private static final File file = new File("resources/channels.ser");
  private UserRepository userRepository;
  private MessageRepository messageRepository;
  private FileIO fileIO;
  private List<Channel> channels = new ArrayList<>();
  
  private void loadAllChannels() {
    fileIO.initFile(file);
    List<Serializable> serializables = fileIO.loadFile(file);
    channels = serializables.stream()
        .filter(Channel.class::isInstance)
        .map(Channel.class::cast)
        .toList();
  }
  
  @Override
  public Optional<Channel> findChannelById(UUID id) {
    return channels.stream().filter(c -> c.getId().equals(id)).findFirst();
  }
  
  @Override
  public Optional<Channel> findChannelByName(String name) {
    return channels.stream().filter(c -> c.getName().equals(name)).findFirst();
  }
  
  @Override
  public List<Channel> findAllChannels() {
    return channels.stream().toList();
  }
  
  @Override
  public void save(Channel channel) {
    try {
      fileIO.saveToFile(file, channel);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
  
  @Override
  public void updateRepository(Channel channel) {
  
  }
  
  @Override
  public void remove(UUID id) {
    Channel channel = findChannelById(id).orElse(null);
    fileIO.removeObjectFromFile(file, channel);
  }
  
}
