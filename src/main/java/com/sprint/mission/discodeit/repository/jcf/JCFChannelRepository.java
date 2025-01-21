package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BaseChannel;
import com.sprint.mission.discodeit.entity.ChatChannel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;

public class JCFChannelRepository implements ChannelRepository {

  private final Map<String, BaseChannel> data;
  public JCFChannelRepository(){
    this.data = new HashMap<>();
  }
  @Override
  public BaseChannel save(BaseChannel baseChannel) {
    data.put(baseChannel.getUUID(), baseChannel);
    return baseChannel;
  }

  @Override
  public Optional<BaseChannel> findById(String id) {
    return Optional.ofNullable(data.get(id));
  }

  @Override
  public List<BaseChannel> findAll() {
    return new ArrayList<>(data.values());
  }

  @Override
  public BaseChannel update(BaseChannel channel) {
    data.put(channel.getUUID(), channel);
    return channel;
  }

  @Override
  public void delete(String id) {
    data.remove(id);
  }

  @Override
  public void clear() {
    data.clear();
  }
}
