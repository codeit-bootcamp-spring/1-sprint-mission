package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.BaseChannel;
import com.sprint.mission.discodeit.entity.ChatChannel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class JCFChannelRepository implements ChannelRepository {


  private static volatile JCFChannelRepository instance;


  private final Map<String, BaseChannel> data;

  private JCFChannelRepository() {
    this.data = new ConcurrentHashMap<>();
  }

  public static JCFChannelRepository getInstance() {
    if (instance == null) {
      synchronized (JCFChannelRepository.class) {
        if (instance == null) {
          instance = new JCFChannelRepository();
        }
      }
    }
    return instance;
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
