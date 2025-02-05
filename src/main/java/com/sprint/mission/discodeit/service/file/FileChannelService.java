package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import java.util.*;

public class FileChannelService implements ChannelService {
  Map<UUID, Channel> channelMap = new HashMap<>();
  private ChannelRepository channelRepository;
  private MessageService messageService;
  private UserService userService;
  
  private void saveChannelToCsv(Channel channel) {
    channelRepository.save(channel);
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
    Channel newChannel = new Channel(name, owner, members);
    channelMap.put(newChannel.getId(), newChannel);
    saveChannelToCsv(newChannel);
  }
  
  @Override
  public void updateChannelName(UUID id, String newName) {
    Channel channel = channelRepository.findChannelById(id)
        .orElseThrow(() -> new NoSuchElementException("channel not found: " + id));
    channel.updateName(newName);
    saveChannelToCsv(channel);
  }
  
  @Override
  public void updateMember(UUID id, List<User> members) {
    Channel channel = channelRepository.findChannelById(id)
        .orElseThrow(() -> new NoSuchElementException("channel not found: " + id));
    channel.updateMembers(members);
    saveChannelToCsv(channel);
  }
  
  @Override
  public void removeChannel(UUID channelId, User user) {
    channelRepository.remove(channelId);
  }
  
}
