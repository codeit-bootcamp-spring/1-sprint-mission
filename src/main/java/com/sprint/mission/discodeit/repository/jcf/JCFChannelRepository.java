package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import java.util.*;

public class JCFChannelRepository implements ChannelRepository {

  private final Map<UUID, Channel> channels;

  public JCFChannelRepository() {
    this.channels = new HashMap<>();
  }

  @Override
  public boolean existsName(String channelName) {
    return this.channels.values().stream()
        .anyMatch(user -> user.getChannelName().equals(channelName));
  }

  @Override
  public Channel save(Channel channel) {
    this.channels.put(channel.getId(), channel);
    return channel;
  }

  @Override
  public Optional<Channel> findById(UUID id) {
    return Optional.ofNullable(this.channels.get(id));
  }

  @Override
  public List<Channel> findAll() {
    return this.channels.values().stream()
        .toList();
  }

  @Override
  public boolean existsId(UUID channelId) {
    return this.channels.containsKey(channelId);
  }

  @Override
  public void delete(UUID channelId) {
    this.channels.remove(channelId);
  }

}