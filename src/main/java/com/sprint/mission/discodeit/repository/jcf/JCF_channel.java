package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class JCF_channel implements ChannelRepository {
  private final List<Channel> channelList;

  public JCF_channel() {
    this.channelList = new ArrayList<>();
  }
  @Override
  public void creat(Channel channel) {
    channelList.add(channel);
    System.out.println(channelList);
  }

  @Override
  public void delete(UUID channelId) {
    Optional<Channel> getChannel = channelList.stream().filter(channel -> channel.getId().equals(channelId)).findFirst();
    if (getChannel.isEmpty()) {
      throw new IllegalArgumentException("User not found");
    }
    else {
      channelList.remove(getChannel.get());
    }
  }

  @Override
  public void update(UUID channelId, String title) {
    channelList.stream().filter(channel -> channel.getId().equals(channelId))
        .forEach(channel -> channel.updateTitle(title));
  }

  @Override
  public UUID findByTitle(String title) {
    Optional<Channel> channel = channelList.stream().filter(channel_id -> channel_id.getTitle().equals(title)).findFirst();

    if(channel.isPresent()){
      return channel.get().getId();
    }
    else{
      return null;
    }
  }

  @Override
  public List<Channel> findByAll() {
    return channelList.stream()
        .map(Channel::new)
        .collect(Collectors.toList());
  }
}
