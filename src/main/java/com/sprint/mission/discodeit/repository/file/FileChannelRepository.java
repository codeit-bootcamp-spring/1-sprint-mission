package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Repository
public class FileChannelRepository implements ChannelRepository {

  private final Path DIRECTORY;
  private final Path FILE_PATH;
  private final Map<UUID, Channel> data;

  public FileChannelRepository(
      @Value("${discodeit.repository.file.storage-path}") String storagePath) {
    this.DIRECTORY = Paths.get(System.getProperty("user.dir"), storagePath, "Channel");
    this.FILE_PATH = DIRECTORY.resolve("channel.ser");

    if (Files.notExists(DIRECTORY)) {
      try {
        Files.createDirectories(DIRECTORY);
      } catch (IOException e) {
        throw new RuntimeException("Failed to create directory: " + DIRECTORY, e);
      }
    }
    this.data = loadDataFromFile();
  }


  @Override
  public Channel save(Channel channel) {
    data.put(channel.getId(), channel);
    saveDataToFile();
    return channel;
  }

  @Override
  public Optional<Channel> getChannelById(UUID id) {
    return Optional.ofNullable(this.data.get(id));
  }

  @Override
  public List<Channel> getAllChannels() {
    return this.data.values().stream().toList();
  }

  @Override
  public boolean existsById(UUID id) {
    return this.data.containsKey(id);
  }


  @Override
  public void deleteChannel(UUID id) {
    if (!this.data.containsKey(id)) {
      throw new NoSuchElementException("Channel with id" + id + " not found");
    }
    data.remove(id);
    saveDataToFile();
  }

  // 데이터를 파일에 저장
  private void saveDataToFile() {
    try (ObjectOutputStream oos = new ObjectOutputStream(
        new FileOutputStream(FILE_PATH.toFile()))) {
      oos.writeObject(data);
    } catch (IOException e) {
      throw new RuntimeException("Failed to save data to file: " + e.getMessage(), e);
    }
  }

  // 파일에서 데이터를 로드
  private Map<UUID, Channel> loadDataFromFile() {
    File file = FILE_PATH.toFile();
    if (!file.exists()) {
      return new HashMap<>();
    }

    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
      return (Map<UUID, Channel>) ois.readObject();
    } catch (IOException | ClassNotFoundException e) {
      throw new RuntimeException("Failed to load data from file: " + e.getMessage(), e);
    }
  }
}
