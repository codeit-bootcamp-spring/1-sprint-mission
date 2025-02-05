package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;

public interface ChannelService {
  Channel findChannelById(UUID id);
  
  Channel findChannelByName(String name);
  
  List<Channel> findAllChannels();
  
  void createChannel(String name, User owner, List<User> members);
  
  void updateChannelName(UUID id, String newName);
  
  void updateMember(UUID id, List<User> members);
  
  void removeChannel(UUID channelId, User owner);
}
