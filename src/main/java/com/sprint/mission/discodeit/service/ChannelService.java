package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelDTO;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

  ChannelDTO create(PublicChannelCreateRequest publicChannelCreateRequest);

  ChannelDTO create(PrivateChannelCreateRequest privateChannelCreateRequest);

  ChannelDTO find(UUID channelId);

  List<ChannelDTO> findAllByUserId(UUID userId);

  List<ChannelDTO> findPublicAll();

  ChannelDTO update(UUID channelId, PublicChannelUpdateRequest publicChannelUpdateRequest);

  void delete(UUID channelId, UUID adminId);

  List<UUID> getParticipantIds(UUID channelId);
}
