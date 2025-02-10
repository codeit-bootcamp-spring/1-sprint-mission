package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@Qualifier("jcf")
public class JCFChannelRepository implements ChannelRepository {
  private final List<Channel> data = new ArrayList<>();
  
  @Override
  public void save(Channel channel) {
    data.add(channel);
  }
  
  @Override
  public Optional<Channel> findById(UUID id) {
    return data.stream()
        .filter(c -> c.getId().equals(id))
        .findFirst();
  }
  
  @Override
  public Optional<Channel> findByName(String name) {
    return data.stream()
        .filter(c -> c.getName().equals(name))
        .findFirst();
  }
  
  @Override
  public List<Channel> findAll() {
    return new ArrayList<>(data);
  }
  
  @Override
  public void remove(UUID id) {
    findById(id).ifPresent(data::remove);
  }
}
