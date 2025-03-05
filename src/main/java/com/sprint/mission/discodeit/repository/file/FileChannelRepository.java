package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.util.SerializationUtil;
import java.util.HashMap;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
@Repository
public class FileChannelRepository implements ChannelRepository {

  private HashMap<UUID, Channel> data;
  private SerializationUtil<UUID, Channel> util;

  public FileChannelRepository(SerializationUtil<UUID, Channel> util) {
    this.util = util;
    this.data = util.loadData();
  }


  @Override
  public Channel save(Channel channel) {
    this.data.put(channel.getId(), channel);
    util.saveData(this.data);
    return channel;
  }

  @Override
  public Optional<Channel> findById(UUID channelId) {
    return Optional.ofNullable(data.get(channelId));
  }

  @Override
  public List<Channel> findAll() {
    return data.values().stream().toList();
  }

  @Override
  public boolean existsById(UUID id) {
    return this.data.containsKey(id);
  }

  @Override
  public void deleteById(UUID channelId) {
    data.remove(channelId);
    util.saveData(this.data);
  }
}
