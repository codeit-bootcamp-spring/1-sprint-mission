package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class BasicChannelService implements ChannelService {
  private final ChannelRepository channelRepository;
  
  public BasicChannelService(ChannelRepository channelRepository) {
    this.channelRepository = channelRepository;
  }
  
  @Override
  public Channel findChannelById(UUID id) {
    return channelRepository.findChannelById(id).orElseThrow(() ->
        new NoSuchElementException("channel not found: " + id));
  }
  
  @Override
  public Channel findChannelByName(String name) {
    return channelRepository.findChannelByName(name).orElseThrow(() ->
        new NoSuchElementException("channel not found: " + name));
  }
  
  @Override
  public List<Channel> findAllChannels() {
    return channelRepository.findAllChannels();
  }
  
  @Override
  public void createChannel(String name, User owner, List<User> members) {
    Channel channel = new Channel(name, owner, members);
    channelRepository.save(channel);
  }
  
  @Override
  public void updateChannelName(UUID id, String newName) {
    Channel channel = channelRepository.findChannelById(id)
        .orElseThrow(() -> new NoSuchElementException("channel not found: " + id));
    channel.updateName(newName);
    channelRepository.save(channel);
  }
  
  @Override
  public void updateMember(UUID id, List<User> members) {
    Channel channel = channelRepository.findChannelById(id)
        .orElseThrow(() -> new NoSuchElementException("channel not found: " + id));
    channel.updateMembers(members);
    channelRepository.save(channel);
  }
  
  @Override
  public void removeChannel(UUID channelId, User owner) {
    if (channelRepository.findChannelById(channelId)
        .map(channel -> channel.getOwner().equals(owner)).isPresent()) {
      channelRepository.remove(channelId);
    }
    throw new IllegalArgumentException("삭제 권한이 없는 사용자입니다.");
    
  }
}