package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@ConditionalOnProperty(name = "app.repository.type", havingValue = "jcf")
public class JCFChannelRepository implements ChannelRepository{

  private final Map<String, Channel> data = new ConcurrentHashMap<>();


  @Override
  public Channel save(Channel channel) {
    data.put(channel.getId(), channel);
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
    data.put(channel.getId(), channel);
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
