package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class JCFChannelRepository implements ChannelRepository {

  private final Map<UUID, Channel> data;

  public JCFChannelRepository() {
    data = new HashMap<>();
  }


  @Override
  public Channel save(Channel channel) {
    data.put(channel.getId(), channel);
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
  }
}
