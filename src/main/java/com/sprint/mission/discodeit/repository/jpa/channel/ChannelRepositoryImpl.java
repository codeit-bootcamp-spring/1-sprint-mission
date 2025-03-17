package com.sprint.mission.discodeit.repository.jpa.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ChannelRepositoryImpl implements ChannelRepository {

  private final ChannelJpaRepository channelRepository;

  @Override
  public Channel saveChannel(Channel channel) {
    return channelRepository.save(channel);
  }

  @Override
  public Optional<Channel> findChannelById(UUID channelId) {
    return channelRepository.findById(channelId);
  }

  @Override
  public List<Channel> findAllChannels() {
    return channelRepository.findAll();
  }

  @Override
  public List<Channel> findAccessibleChannels(User user) {
    return null;
  }

  @Override
  public void removeChannel(UUID channelId) {
    channelRepository.deleteById(channelId);
  }
}
