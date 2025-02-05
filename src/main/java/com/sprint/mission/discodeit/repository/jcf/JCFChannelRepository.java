package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@ConditionalOnProperty(name = "app.repository.type", havingValue = "jcf")
public class JCFChannelRepository implements ChannelRepository {


  private static volatile JCFChannelRepository instance;


  private final Map<String, Channel> data;

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
  public Channel save(Channel channel) {
    data.put(channel.getUUID(), channel);
    return channel;
  }

  @Override
  public Optional<Channel> findById(String id) {
    return Optional.ofNullable(data.get(id));
  }

  @Override
  public List<Channel> findAll() {
    return new ArrayList<>(data.values());
  }

  @Override
  public Channel update(Channel channel) {
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
