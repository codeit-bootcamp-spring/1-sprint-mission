package com.sprint.mission.service;


import com.sprint.mission.dto.request.ChannelDtoForUpdate;
import com.sprint.mission.dto.request.PrivateChannelCreateDTO;
import com.sprint.mission.dto.request.PublicChannelCreateDTO;
import com.sprint.mission.dto.response.ChannelDto;
import com.sprint.mission.entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {

  List<Channel> findAll();

  Channel createPublicChannel(PublicChannelCreateDTO request);

  Channel createPrivateChannel(PrivateChannelCreateDTO request);

  Channel findById(UUID id);

  List<ChannelDto> findAllByUserId(UUID userId);

  Channel update(UUID channelId, ChannelDtoForUpdate dto);

  void delete(UUID channelId);
//    void validateDuplicateName(String name);
}