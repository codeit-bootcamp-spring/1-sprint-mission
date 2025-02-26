package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "jcf")
public class JCFChannelRepository implements ChannelRepository {

  private final HashMap<String, Channel> data;

  public JCFChannelRepository() {
    this.data = new HashMap<>();
  }

  @Override
  public Channel save(Channel channel) {
    data.put(channel.getId(), channel);
    return channel;
  }

  @Override
  public boolean delete(String channelId) {
    return data.remove(channelId) != null;
  }

  @Override
  public Channel findById(String id) {
    return data.get(id);
  }

  @Override
  public List<Channel> findByParticipantId(String userId) {
    return data.values().stream().filter(channel ->
        channel.getChannelType() == ChannelType.PUBLIC ||
            (channel.getChannelType() == ChannelType.PRIVATE &&
                channel.getUserSet().contains(userId))).toList();
  }

  @Override
  public List<Channel> findAll() {
    return data.values().stream().toList();
  }

}
