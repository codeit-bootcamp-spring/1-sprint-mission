package com.sprint.mission.discodeit.service.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.file.repository.FileChannelRepository;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class FileChannel implements FileChannelRepository {
  private static final String FILE_PATH = "src/main/java/com/sprint/mission/discodeit/service/file/data/channel/channel.json";
  private static final ObjectMapper objectMapper = new ObjectMapper();

  private List<Channel> readAllChannels() {
    File file = new File(FILE_PATH);
    if (!file.exists() || file.length() == 0)
      return new ArrayList<>();
    try {
      return objectMapper.readValue(file, new TypeReference<List<Channel>>() {
      });
    } catch (IOException e) {
      e.printStackTrace();
      return new ArrayList<>();
    }
  }

  @Override
  public void creat(String name) {
    Channel channel = new Channel(name);
    try {
      List<Channel> channelList = readAllChannels();
      channelList.add(channel);
      saveToFile(channelList);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

  @Override
  public void delete(UUID channelId) {
    try {
      List<Channel> channels = readAllChannels();
      channels.removeIf(channel -> channel.getId().equals(channelId));
      saveToFile(channels);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void update(UUID channelId, String title) {
    List<Channel> channelList = readAllChannels();

    boolean updated = channelList.stream()
        .filter(channel -> channel.getId().equals(channelId))
        .findFirst()
        .map(channel -> {
          channel.updateTitle(title);
          return true;
        })
        .orElse(false);

    if (updated) {
      saveToFile(channelList);
    } else {
      throw new IllegalArgumentException("Channel with ID not found.");
    }
  }

  private void saveToFile(List<Channel> channels) {
    try {
      objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PATH), channels);
    } catch (Exception e) {
      System.out.println(e);
    }
  }

  @Override
  public UUID findByTitle(String title) {
    List<Channel> channelList = readAllChannels();
    Optional<Channel> channel = channelList.stream().filter(channel1 -> channel1.getTitle().equals(title))
        .findFirst();
    if (channel.isPresent()) {
      return channel.get().getId();
    } else {
      throw new IllegalArgumentException("There is no channel with that name.");
    }
  }

  @Override
  public List<Channel> findByAll() {
    List<Channel> channelList = readAllChannels();
    return channelList.stream()
        .map(Channel::new)
        .collect(Collectors.toList());
  }

  @Override
  public void addMessage(UUID messageId, UUID channelId) {
    List<Channel> channelList = readAllChannels();
    boolean updated = channelList.stream()
        .filter(channel -> channel.getId().equals(channelId))
        .findFirst()
        .map(channel -> {
          channel.addMessage(messageId);
          return true;
        })
        .orElse(false);

    if (updated) {
      saveToFile(channelList);
    }

  }
}
