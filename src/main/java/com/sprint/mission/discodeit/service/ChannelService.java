package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channelDto.ChannelDto;
import com.sprint.mission.discodeit.dto.channelDto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channelDto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channelDto.PublicChannelUpdateRequest;
import java.util.List;
import java.util.UUID;

public interface ChannelService {

  ChannelDto createPublicChannel(PublicChannelCreateRequest request);

  ChannelDto createPrivateChannel(PrivateChannelCreateRequest request);

  ChannelDto findById(UUID channelId);

  List<ChannelDto> findAllByUserId(UUID userId);

  ChannelDto updatePublicChannel(UUID channelId, PublicChannelUpdateRequest request);

  void delete(UUID channelId);
}