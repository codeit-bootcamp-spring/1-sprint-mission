package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.PublicChannelUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

  ChannelDto create(PublicChannelCreateRequest publicChannelCreateRequest);

  ChannelDto create(PrivateChannelCreateRequest privateChannelCreateRequest);

  ChannelDto find(UUID channelId);

  List<ChannelDto> findAllByUserId(UUID userId);

  List<ChannelDto> findPublicAll();

  ChannelDto update(UUID channelId, PublicChannelUpdateRequest publicChannelUpdateRequest);

  void delete(UUID channelId, UUID adminId);

  List<UUID> getParticipantIds(UUID channelId);
}
