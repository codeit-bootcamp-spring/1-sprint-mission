package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import java.util.List;
import java.util.UUID;

public interface ChannelService {

  UUID createPublicChannel(PublicChannelCreateRequest publicChannelDto);

  UUID createPrivateChannel(PrivateChannelCreateRequest privateChannelDto);

  ChannelDto findById(UUID id);

  ChannelDto findByName(String name);

  List<ChannelDto> findAllByUserId(UUID userId);

  void updateName(UUID id, String newName);

  void updateMember(PrivateChannelUpdateRequest channelDto);

  void remove(UUID channelId, UUID ownerId);
}
