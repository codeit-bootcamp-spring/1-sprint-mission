package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.dto.channel.FindChannelDto;
import com.sprint.mission.discodeit.dto.channel.UpdatePrivateChannelDto;
import java.util.List;
import java.util.UUID;

public interface ChannelService {
  void createPublicChannel(UUID ownerId, String name, String description);
  
  void createPrivateChannel(CreatePrivateChannelDto privateChannelDto);
  
  FindChannelDto findById(UUID id);
  
  FindChannelDto findByName(String name);
  
  List<FindChannelDto> findAllByUserId(UUID userId);
  
  void updateName(UUID id, String newName);
  
  void updateMember(UpdatePrivateChannelDto channelDto);
  
  void remove(UUID channelId, UUID ownerId);
}
