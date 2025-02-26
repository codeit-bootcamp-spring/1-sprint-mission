package com.sprint.mission.discodeit.repository.file.serial;

import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.file.FileService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class FileChannelRepository implements ChannelRepository, FileService<Channel> {

  private static final String CHANNEL_PATH = "channel.ser";

  private Map<UUID, Channel> channelMap = new HashMap<>();

  @Override
  public Channel saveChannel(Channel channel) {
    Channel saveChannel = channelMap.put(channel.getId(), channel);

    save(CHANNEL_PATH, channelMap);

    return channelMap.get(channel.getId());
  }

  @Override
  public void removeChannelById(UUID channelId) {
    channelMap = findAllChannel();
    channelMap.remove(channelId);

    save(CHANNEL_PATH, channelMap);
  }

  @Override
  public Channel findChannelById(UUID channelId) {
    channelMap = findAllChannel();
    return channelMap.get(channelId);
  }

  @Override
  public Map<UUID, Channel> findAllChannel() {
    return load(CHANNEL_PATH, channelMap);
  }


  @Override
  public void clearData() {

  }

  @Override
  public void resetData() {

  }

  @Override
  public List<Channel> findAllChannelById(UUID channelId) {
    return null;
  }
}
