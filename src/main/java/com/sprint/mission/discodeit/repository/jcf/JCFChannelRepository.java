package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;

import java.util.*;

public class JCFChannelRepository implements ChannelRepository {
  // 1. 자료구조 상수 선언 2. 생성자로 객체 생성
  private final Map<UUID, Channel> data;
  public JCFChannelRepository() {
    this.data = new HashMap<>();
  }

  @Override
  public Channel save(Channel channel){
      return data.put(channel.getId(), channel);
  }

  @Override
  public Optional<Channel> findById(UUID channelId){
    return Optional.ofNullable(data.get(channelId));
  }

  @Override
  public List<Channel> findAll(){
    return data.values().stream().toList();
  }

  @Override
  public boolean existsById(UUID id) {
    return this.data.containsKey(id);
  }

  @Override
  public void deleteById(UUID channelId){
    data.remove(channelId);
  }
}
