package com.sprint.mission.service;


import com.sprint.mission.dto.request.PrivateChannelCreateDTO;
import com.sprint.mission.dto.request.PublicChannelCreateDTO;
import com.sprint.mission.dto.response.FindChannelAllDto;
import com.sprint.mission.entity.main.Channel;
import com.sprint.mission.dto.request.ChannelDtoForRequest;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
//
//    Channel create(ChannelDtoForRequest dto);

  List<FindChannelAllDto> findAllByUserId(UUID userId);

  List<Channel> findAll();

  Channel createPublicChannel(PublicChannelCreateDTO request);

  Channel createPrivateChannel(PrivateChannelCreateDTO request);

  Channel findById(UUID id);

  void update(UUID channelId, ChannelDtoForRequest dto);

  void delete(UUID channelId);
//    void validateDuplicateName(String name);
}